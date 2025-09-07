package vn.com.ecomstore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.ecomstore.dtos.request.upload.UploadRequest;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/uploads")
@RequiredArgsConstructor
public class UploadController {
    private final vn.com.ecomstore.services.UploadService uploadService;

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<vn.com.ecomstore.dtos.response.base.ResponseSuccess<List<String>>> upload(@ModelAttribute UploadRequest uploadRequest) {
        return ResponseEntity.ok(new vn.com.ecomstore.dtos.response.base.ResponseSuccess<>(HttpStatus.OK,
                "Upload image success", uploadService.upload(uploadRequest)));
    }

}