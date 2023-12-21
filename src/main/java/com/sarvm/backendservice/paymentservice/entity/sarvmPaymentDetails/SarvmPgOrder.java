package com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(
        name = "Sarvm_Payment_Order_Details",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pgOrderId"})
)
public class SarvmPgOrder {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "razorpay_order_id cannot be null in table")
    @Column(name = "pgOrderId")
    private String pgOrderId;

    @NotNull(message = "payment gateway name must be provided")
    private String paymentGatewayName;

    @NotNull(message = "amount cannot be null in table")
    private Double amount;

    private Boolean partialPayment;

    private Double amountPaid;

    private Double amountDue;

    @NotNull(message = "receipt_id cannot be null in table")
    private String receiptId;

    @NotNull(message = "currency cannot be null in table")
    private String currency;

    @NotNull(message = "status cannot be null in table")
    private String orderStatus;

    private String notes;

    @NotNull(message = "created_at cannot be null in table")
    private Date creationTime;

    @Column(name="service_name")
    private String serviceName;

    private Integer attempts;
}
