package com.parking.profile_service.mapper;

import com.parking.profile_service.dto.response.CloudinaryUploadResponse;
import com.parking.profile_service.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AvatarMapper {


    void toAvatar(CloudinaryUploadResponse cloudinaryUploadResponse, @MappingTarget Avatar avatar);
}
