package com.sarvm.backendservice.paymentservice.repository.paymentDetails;

import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ISarvmPaymentRepository extends JpaRepository<SarvmPgPayment, Long> {

    List<SarvmPgPayment> findAllByPgOrderId(String pgOrderId);

    List<SarvmPgPayment> findAllByPgOrderIdAndPaymentStatus(String pgOrderId, String paymentStatus);

    Page<SarvmPgPayment> findAllByPaymentStatusOrPaymentStatusOrPaymentStatus(String status1, String status2,
                                                                              String status3, Pageable pageable);

    Optional<SarvmPgPayment> findFirstByPgPaymentId(String id);

    SarvmPgPayment findByPgOrderId(String pgOrderId);

    List<SarvmPgPayment> findAllByCreatedAtBetweenAndPaymentStatus(Date timestamp20MinBefore, Date currentTimestamp, String paymentStatus);

    List<SarvmPgPayment> findAllByCreatedAtBetween(Date currentTime, Date before20MinTime);

    @Query("select spd from SarvmPgPayment spd join SarvmUserPaymentMap supm on spd.pgOrderId = supm.pgOrderId where supm.userId = :userId")
    List<SarvmPgPayment> findByUserId(String userId);
}
