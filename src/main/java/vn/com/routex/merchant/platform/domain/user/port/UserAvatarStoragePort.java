package vn.com.routex.merchant.platform.domain.user.port;

public interface UserAvatarStoragePort {

    record UploadResult(String key, String url) {}

    UploadResult uploadAvatar(String userId, byte[] bytes, String contentType, String originalFilename);
}

