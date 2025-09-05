package vn.com.ecomstore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.entities.Attribute;
import vn.com.ecomstore.mappers.AttributeMapper;
import vn.com.ecomstore.repositories.AttributeRepository;
import vn.com.ecomstore.services.AttributeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @Override
    public  List<AttributeResponse> getAttributesActive(){
        return attributeRepository.findByStatus(true).stream()
                .map(attributeMapper::toResponse)
                .toList();

    }
}
