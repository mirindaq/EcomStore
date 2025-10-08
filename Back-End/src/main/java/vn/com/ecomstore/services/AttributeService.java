package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import vn.com.ecomstore.dtos.response.attribute.AttributeValueResponse;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.entities.Attribute;

import java.util.List;

public interface AttributeService {
    List<AttributeResponse> getAttributesActive();

    Attribute getAttributeEntityById(Long id);
}
