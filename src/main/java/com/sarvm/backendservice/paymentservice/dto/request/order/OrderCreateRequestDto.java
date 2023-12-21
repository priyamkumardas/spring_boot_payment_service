package com.sarvm.backendservice.paymentservice.dto.request.order;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;


@Data
public class OrderCreateRequestDto {

    private Map<String, String> notes;

    @NotNull(message = "Amount must be passed in the request")
    private Double amount;

    @NotNull(message = "Currency must be passed in the request")
    private String currency;

    @NotNull(message = "Receipt_id must be passed in the request")
    private String receipt_id;

    @NotNull(message = "Please mention whether to save record or not")
    private boolean persist;

    @NotNull(message = "Service name must be present")
    private String service;

    private boolean partial_payment;
    private Double first_payment_min_amount;

}
