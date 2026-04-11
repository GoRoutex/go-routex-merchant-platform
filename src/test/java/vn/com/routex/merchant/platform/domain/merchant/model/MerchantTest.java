package vn.com.routex.merchant.platform.domain.merchant.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantTest {

    @Test
    void shouldCalculateMerchantProfitAndPlatformCommissionFromTicketPrice() {
        Merchant merchant = Merchant.builder()
                .commissionRate(new BigDecimal("15.00"))
                .build();

        BigDecimal ticketPrice = new BigDecimal("100000");

        assertEquals(new BigDecimal("15000.00"), merchant.calculatePlatformCommissionAmount(ticketPrice));
        assertEquals(new BigDecimal("85000.00"), merchant.calculateMerchantProfitAmount(ticketPrice));
        assertEquals(new BigDecimal("85.00"), merchant.calculateMerchantProfitRate());
    }

    @Test
    void shouldTreatMissingCommissionRateAsZero() {
        Merchant merchant = Merchant.builder().build();

        BigDecimal ticketPrice = new BigDecimal("100000");

        assertEquals(new BigDecimal("0.00"), merchant.calculatePlatformCommissionAmount(ticketPrice));
        assertEquals(new BigDecimal("100000.00"), merchant.calculateMerchantProfitAmount(ticketPrice));
        assertEquals(new BigDecimal("100.00"), merchant.calculateMerchantProfitRate());
    }

    @Test
    void shouldRejectNegativeTicketPrice() {
        Merchant merchant = Merchant.builder()
                .commissionRate(new BigDecimal("15.00"))
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> merchant.calculateMerchantProfitAmount(new BigDecimal("-1")));
    }
}
