package com.paypal.transaction_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class transferRequest {
    private Long senderId;
    private Long receiverId;
    private Double amount;
}
