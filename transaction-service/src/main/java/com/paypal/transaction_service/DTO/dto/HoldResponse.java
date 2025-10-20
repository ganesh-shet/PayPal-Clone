package com.paypal.transaction_service.DTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoldResponse {
    private String holdReference;
    private Long amount;
    private String status;
}
