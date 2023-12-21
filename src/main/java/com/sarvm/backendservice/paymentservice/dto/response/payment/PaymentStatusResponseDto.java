package com.sarvm.backendservice.paymentservice.dto.response.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PaymentStatusResponseDto {

    private String payment_id;
    private String order_id;

    @NotNull
    private String payment_status;

    private String error_msg;
    private String success_msg;

}
