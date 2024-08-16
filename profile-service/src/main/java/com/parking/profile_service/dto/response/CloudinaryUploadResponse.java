package com.parking.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryUploadResponse {

    String signature;
    String format;
    String resource_type;
    String secure_url;
    String created_at;
    String asset_id;
    String version_id;
    String type;
    long version;
    String url;
    String public_id;
    String[] tags;
    String folder;
    String api_key;
    long bytes;
    int width;
    int height;
    String etag;
    boolean placeholder;
}
