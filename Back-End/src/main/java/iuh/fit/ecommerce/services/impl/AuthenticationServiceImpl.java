package iuh.fit.ecommerce.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.JwtException;
import iuh.fit.ecommerce.configurations.jwt.JwtUtil;
import iuh.fit.ecommerce.dtos.request.authentication.LoginRequest;
import iuh.fit.ecommerce.dtos.request.authentication.RegisterRequest;
import iuh.fit.ecommerce.dtos.response.authentication.LoginResponse;
import iuh.fit.ecommerce.dtos.response.authentication.RefreshTokenResponse;
import iuh.fit.ecommerce.dtos.response.user.UserProfileResponse;
import iuh.fit.ecommerce.entities.*;
import iuh.fit.ecommerce.enums.TokenType;
import iuh.fit.ecommerce.exceptions.ErrorCode;
import iuh.fit.ecommerce.exceptions.custom.ConflictException;
import iuh.fit.ecommerce.exceptions.custom.ResourceNotFoundException;
import iuh.fit.ecommerce.exceptions.custom.UnauthorizedException;
import iuh.fit.ecommerce.mappers.UserMapper;
import iuh.fit.ecommerce.repositories.*;
import iuh.fit.ecommerce.services.AuthenticationService;
import iuh.fit.ecommerce.utils.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RankingRepository rankingRepository;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SecurityUtils securityUtils;
    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final UserMapper userMapper;

    @Override
    public LoginResponse staffLogin(LoginRequest loginRequest) {
        return loginProcess(
                loginRequest,
                staffRepository::findByEmail,
                staffRepository::save
        );
    }

    @Override
    public LoginResponse userLogin(LoginRequest loginRequest) {
        return loginProcess(
                loginRequest,
                customerRepository::findByEmail,
                customerRepository::save
        );
    }

    @Override
    public UserProfileResponse getProfile() {
        User user = securityUtils.getCurrentUser();
        UserProfileResponse response = userMapper.toUserProfileResponse(user);
        if (user instanceof Customer customer) {
            Double spending = customer.getTotalSpending();
            response.setTotalSpending(spending != null ? spending : 0.0);
        }
        return response;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BadCredentialsException(ErrorCode.PASSWORD_MISMATCH.getMessage());
        }

        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        if (customerRepository.existsByPhone(registerRequest.getPhone())) {
            throw new ConflictException(ErrorCode.PHONE_ALREADY_REGISTERED);
        }

        Customer customer = Customer.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .active(true)
                .totalSpending(0.0)
                .ranking(rankingRepository.findByName("S-NEW"))
                .userRoles(new ArrayList<>())
                .build();

        addRoleCustomer(customer);

        customerRepository.save(customer);
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
           throw new BadCredentialsException(ErrorCode.REFRESH_TOKEN_NOT_FOUND_IN_COOKIES.getMessage());
        }

        if (!jwtUtil.validateJwtToken(refreshToken, TokenType.REFRESH_TOKEN)) {
            throw new JwtException(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
        }
        String email = jwtUtil.getUserNameFromJwtToken(refreshToken, TokenType.REFRESH_TOKEN);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));

        RefreshToken dbToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!dbToken.getUser().getId().equals(user.getId())) {
            throw new BadCredentialsException(ErrorCode.REFRESH_TOKEN_NOT_BELONGS_TO_USER.getMessage());
        }

        if (dbToken.getRevoked()) {
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        if (dbToken.getExpiryDate().isBefore(LocalDate.now())) {
            throw new JwtException(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        if (!user.getActive()) {
            throw new UnauthorizedException(ErrorCode.USER_DISABLED);
        }

        String accessToken = jwtUtil.generateAccessToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .build();

    }

    @Override
    public void logout(HttpServletRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(currentUser.getId());
            tokens.forEach(t -> {
                t.setRevoked(true);
            });
            refreshTokenRepository.saveAll(tokens);
            return;
        }

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    if (!token.getUser().getId().equals(currentUser.getId())) {
                        throw new AccessDeniedException(ErrorCode.TOKEN_NOT_BELONGS_TO_USER.getMessage());
                    }
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });

    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }

    @Override
    public String generateAuthUrl(String loginType, String redirectUri) {
        if ("google".equalsIgnoreCase(loginType)) {
            OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(loginType);
            OAuth2ClientProperties.Provider provider = oAuth2ClientProperties.getProvider().get(loginType);

            // Use redirect_uri from request if provided (for mobile), otherwise use from config (for web)
            String finalRedirectUri = (redirectUri != null && !redirectUri.isEmpty()) 
                    ? redirectUri 
                    : registration.getRedirectUri();

            String scope = "openid profile email";
            String responseType = "code";

            return UriComponentsBuilder.fromHttpUrl(provider.getAuthorizationUri())
                    .queryParam("client_id", registration.getClientId())
                    .queryParam("redirect_uri", finalRedirectUri)
                    .queryParam("response_type", responseType)
                    .queryParam("scope", scope)
                    .build()
                    .toUriString();
        }
        throw new IllegalArgumentException(ErrorCode.UNSUPPORTED_LOGIN_TYPE.getMessage());
    }

    @Override
    public LoginResponse socialLoginCallback(String loginType, String code, String redirectUri) throws IOException {
        String accessToken;
        if ("google".equalsIgnoreCase(loginType)) {
            // Use redirect_uri from request if provided (for mobile), otherwise use from config (for web)
            String finalRedirectUri = (redirectUri != null && !redirectUri.isEmpty()) 
                    ? redirectUri 
                    : this.redirectUri;
            
            accessToken = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    finalRedirectUri).execute().getAccessToken();
        } else {
            throw new IllegalArgumentException(ErrorCode.UNSUPPORTED_LOGIN_TYPE.getMessage());
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        });

        Map<String, Object> userInfo = new ObjectMapper().readValue(
                restTemplate.getForEntity(userInfoUri, String.class).getBody(),
                new TypeReference<>() {}
        );

        String email = userInfo.get("email").toString();

        Customer customer = customerRepository.findByEmail(email).orElse(null);

        if (customer == null) {
            customer = new Customer();
            customer.setEmail(email);
            customer.setFullName(userInfo.get("name").toString());
            customer.setActive(true);
            customer.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
//            customer.setRegisterDate(LocalDate.now());
            addRoleCustomer(customer);
            customer = customerRepository.save(customer);
        }

        if (!customer.getActive()) {
            throw new UnauthorizedException(ErrorCode.USER_DISABLED);
        }

        return loginSocial(customer);
    }


    private <T extends User> LoginResponse loginProcess(
            LoginRequest loginRequest,
            Function<String, Optional<T>> findByEmail,
            Function<T, T> saveUser) {

        T user = findByEmail.apply(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(ErrorCode.INVALID_PASSWORD.getMessage());
        }

        if (!user.getActive()) {
            throw new DisabledException(ErrorCode.USER_DISABLED.getMessage());
        }

        String token = jwtUtil.generateAccessToken(user);
        String refreshTokenStr  = jwtUtil.generateRefreshToken(user);
        LocalDate expiryDate = jwtUtil.getExpirationDateFromToken(refreshTokenStr, TokenType.REFRESH_TOKEN);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .expiryDate(expiryDate)
                .deviceInfo(loginRequest.getDeviceInfo())
                .revoked(false)
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);
        saveUser.apply(user);

        List<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName().toUpperCase())
                .toList();

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenStr)
                .roles(roles)
                .email(user.getEmail())
                .build();
    }

    private LoginResponse loginSocial(Customer customer) {
        String accessToken = jwtUtil.generateAccessToken(customer);
        String refreshTokenStr = jwtUtil.generateRefreshToken(customer);
        LocalDate expiryDate = jwtUtil.getExpirationDateFromToken(refreshTokenStr, TokenType.REFRESH_TOKEN);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .expiryDate(expiryDate)
                .deviceInfo("social-login")
                .revoked(false)
                .user(customer)
                .build();

        refreshTokenRepository.save(refreshToken);

        List<String> roles = customer.getUserRoles()
                .stream()
                .map(r -> r.getRole().getName().toUpperCase())
                .toList();

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .roles(roles)
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .build();
    }

    private void addRoleCustomer(Customer customer) {
        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));

        UserRole userRole = new UserRole();
        userRole.setUser(customer);
        userRole.setRole(role);

        customer.getUserRoles().add(userRole);
    }


}
