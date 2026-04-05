package vn.com.routex.merchant.platform.application.command.customer;

import java.math.BigDecimal;

public record CustomerMembershipView(
        String userId,
        BigDecimal tripPoints,
        Integer totalTrips,
        BigDecimal totalSpent,
        String currentTierId,
        String currentBadge,
        Integer priorityLevel,
        BigDecimal pointMultiplier,
        Integer discountPercent
) {
}