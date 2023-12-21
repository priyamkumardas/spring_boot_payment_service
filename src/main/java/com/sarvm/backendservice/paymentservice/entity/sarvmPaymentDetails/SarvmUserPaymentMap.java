package com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(
        name = "Sarvm_User_Payment_Map"
)
public class SarvmUserPaymentMap {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pg_order_id")
    private String pgOrderId;

    private String userId;

    @NotNull(message = "created_at cannot be null in table")
    private Date creationTime;
}
