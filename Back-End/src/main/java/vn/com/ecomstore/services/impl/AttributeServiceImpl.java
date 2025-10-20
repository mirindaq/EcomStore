package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import vn.com.ecomstore.entities.Attribute;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.mappers.AttributeMapper;
import vn.com.ecomstore.repositories.AttributeRepository;
import vn.com.ecomstore.services.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Attribute getAttributeEntityById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found with id = " + id));
    }
}
