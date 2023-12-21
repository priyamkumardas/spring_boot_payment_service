package com.sarvm.backendservice.paymentservice.dto.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {

    @NotNull
    private String order_id;

    @NotNull
    private String payment_gateway_name = "RAZORPAY";
}
