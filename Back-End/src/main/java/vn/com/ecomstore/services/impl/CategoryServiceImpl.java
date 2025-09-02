package vn.com.ecomstore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ecomstore.dtos.request.category.AttributeAddRequest;
import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.entities.Attribute;
import vn.com.ecomstore.entities.Category;
import vn.com.ecomstore.repositories.AttributeRepository;
import vn.com.ecomstore.repositories.CategoryRepository;
import vn.com.ecomstore.services.CategoryService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryAddRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists");
        }

        List<AttributeAddRequest> attrsReq = Optional.ofNullable(request.getAttributes()).orElse(List.of());


        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .status(Boolean.TRUE.equals(request.getStatus()))
                .build();


        LinkedHashMap<String, String> dedup = new LinkedHashMap<>();
        for (AttributeAddRequest ar : attrsReq) {
            if (ar != null && ar.getName() != null) {
                String key = ar.getName().trim();
                if (!key.isEmpty()) {
                    dedup.putIfAbsent(key.toLowerCase(), key);
                }
            }
        }

        categoryRepository.save(category);

        List<Attribute> attrs = dedup.values().stream()
                .map(name -> Attribute.builder()
                        .name(name)
                        .category(category)
                        .build())
                .toList();


        attributeRepository.saveAll(attrs);

        category.setAttributes(attrs);
        return CategoryResponse.convertFromEntity(category);
    }
}
