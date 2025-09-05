package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import vn.com.ecomstore.dtos.response.attribute.AttributeValueResponse;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;

import java.util.List;

public interface AttributeService {
    List<AttributeResponse> getAttributesActive();
}
