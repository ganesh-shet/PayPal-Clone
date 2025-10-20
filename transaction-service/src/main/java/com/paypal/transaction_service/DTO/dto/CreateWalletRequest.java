package com.paypal.transaction_service.DTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateWalletRequest {
    private Long userId;
    private String currency;
}
