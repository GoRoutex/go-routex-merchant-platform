package vn.com.routex.merchant.platform.application.command.authorities;

import lombok.Builder;

import java.util.Set;

@Builder
public record SetPermissionResult(
        String roleId,
        Set<String> authorities
) {
}
