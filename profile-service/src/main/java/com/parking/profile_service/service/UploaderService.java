package com.parking.profile_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.profile_service.dto.request.AvatarUpdateRequest;
import com.parking.profile_service.dto.response.CloudinaryUploadResponse;
import com.parking.profile_service.entity.Avatar;
import com.parking.profile_service.enums.ECloudinary;
import com.parking.profile_service.mapper.AvatarMapper;
import com.parking.profile_service.repository.AvatarRepository;
import com.parking.profile_service.repository.uploader.CloudinaryUploader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class UploaderService {
    AvatarRepository avatarRepository;
    CloudinaryUploader cloudinaryUploader;
    AvatarMapper avatarMapper;
    ObjectMapper objectMapper;

    public void customerUpdateAvatar(AvatarUpdateRequest request) {
        CloudinaryUploadResponse cloudinaryUploadResponse = cloudinaryUpload(request.getImage(), ECloudinary.FOLDER_AVATAR);

        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Avatar avatar = new Avatar();
        avatarMapper.toAvatar(cloudinaryUploadResponse, avatar);
        avatar.setUid(uid);

        avatarRepository.save(avatar);
    }

    CloudinaryUploadResponse cloudinaryUpload(String image, ECloudinary eCloudinary) {
        Map<String, Object> upload = null;

        try {
            upload = cloudinaryUploader.uploadBase64Image(image, eCloudinary.getValue());
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        return objectMapper.convertValue(upload, CloudinaryUploadResponse.class);
    }

}
