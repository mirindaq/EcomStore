package vn.com.ecomstore.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.ecomstore.dtos.request.customer.CustomerAddRequest;
import vn.com.ecomstore.dtos.response.customer.CustomerResponse;
import vn.com.ecomstore.entities.Customer;

@Mapper(componentModel = "spring", imports = {java.time.LocalDate.class, java.util.Collections.class})
public interface CustomerMapper {
    @Mapping(target = "roles", expression = "java(Collections.singletonList(roleName))")
    CustomerResponse toResponse(Customer customer, @Context String roleName);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "registerDate", expression = "java(LocalDate.now())")
    @Mapping(target = "active", constant = "true")
    Customer toCustomer(CustomerAddRequest request);
}
