package com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(
        name = "Sarvm_Payment_Details"
)
public class SarvmPgPayment {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "payment gateway name must be provided")
    private String paymentGatewayName;

    @ManyToOne
    @JoinColumn(name = "sarvm_pg_order_id")
    private SarvmPgOrder sarvmPgOrder;

    @Column(name = "pg_payment_id")
    private String pgPaymentId;

    @Column(name = "pg_order_id")
    private String pgOrderId;

    @Column
    private String paymentStatus;

    @Column(name = "pg_status")
    private String pgStatus;

    @Column(name = "service_name")
    private String serviceName;

    private String method; // upi, credit card, debit card, cash, netbanking, wallet

    private Double amount;

    @Column(name = "base_amount")
    private Double baseAmount; // The converted payment amount used to calculate fees
    // and settlements. Represented in smallest unit of the base_currency.
    // This attribute is currently only present if the currency is non-INR.

    @Column(name = "base_currency")
    private String baseCurrency; // The conversion currency used to calculate fees and settlements.
    // This currently defaults to INR, and is present only if the currency is non-INR.

    private String currency;

    @Column(name = "invoice_id")
    private String invoiceId;

    private String vpa;

    private String wallet;

    @Column(name = "card_id")
    private String cardId;

    private String bank;

    private String description;

    @Column(name = "amount_refunded")
    private Double amountRefunded;

    @Column(name = "refund_status")
    private String refundStatus;

    private Boolean captured;

    private Boolean international;

    private String email;

    private String contact; // phone number of the user

    private Double fee;

    private Double tax;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "acquirer_description")
    private String errorDescription;

    @Column(name = "error_source")
    private String errorSource;

    @Column(name = "acquirer_step")
    private String errorStep;

    @Column(name = "error_reason")
    private String errorReason;

    private String notes;

    @Column(name = "created_at")
    private Date createdAt; // payment creation time

    private String name; // name of the card holder

    private Integer last4; // last 4 diit of card number

    private String network; // The card network. Possible values:
    // AMerican Express, Visa, Mastercard, Rupay, Maestro

    private String type; // card type string The card type.
    // Possible values: credit debit prepaid unknown

    private String issuer;

    private Boolean emi;

    @Column(name = "sub_type")
    private String subType;

    @Column(name = "acquirer_data")
    private String acquirerData;

}
