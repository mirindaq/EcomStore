package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.request.customer.CustomerAddRequest;
import vn.com.ecomstore.dtos.response.customer.CustomerResponse;
import vn.com.ecomstore.entities.Customer;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.time.LocalDate.class, java.util.Collections.class})
public interface CustomerMapper {
    @Mapping(target = "roles", expression = "java(Collections.singletonList(roleName))")
    CustomerResponse toResponse(Customer customer, @Context String roleName);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "active", constant = "true")
    Customer toCustomer(CustomerAddRequest request);
}
