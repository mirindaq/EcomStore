package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.upload.UploadRequest;

import java.util.List;


public interface UploadService {
    List<String> upload(UploadRequest uploadRequest);
}
