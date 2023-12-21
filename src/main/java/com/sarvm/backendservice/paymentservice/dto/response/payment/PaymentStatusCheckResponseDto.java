package com.sarvm.backendservice.paymentservice.dto.response.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PaymentStatusCheckResponseDto {

    @NotNull(message = "status cannot be null in PaymentStatusCheckResponseDto")
    private String status;

    @NotNull(message = "payment_id cannot be null in PaymentStatusCheckResponseDto")
    private String payment_id;

    @NotNull(message = "order_id cannot be null in PaymentStatusCheckResponseDto")
    private String order_id;

}
