package com.parking.ticket_service.repository.uploader;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryUploader {
    Cloudinary cloudinary;

    @Async("taskExecutor")
    public void asyncUploadBase64Image(String base64Data, String folder, String name) throws IOException {
        cloudinary.uploader()
                .upload(base64Data, ObjectUtils.asMap(
                        "folder", folder,
                        "public_id", name));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadBase64Image(String base64Data, String folder, String name) throws IOException {
        return cloudinary.uploader()
                .upload(base64Data, ObjectUtils.asMap(
                        "folder", folder,
                        "public_id", name));
    }
}
