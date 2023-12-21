package com.sarvm.backendservice.paymentservice.repository.paymentDetails;

import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ISarvmOrderRepository extends CrudRepository<SarvmPgOrder, Long> {
    SarvmPgOrder findByPgOrderId(String pgOrderId);

    @Query("FROM SarvmPgOrder where notes like :userId")
    List<SarvmPgOrder> getPaymentStatus(String userId);

    List<SarvmPgOrder> findAllByCreationTimeBetween(Date start, Date end);

//    @Query("select spod from sarvm_payment_order_details spod left outer join sarvm_user_payment_map supm on spod.pg_order_id = supm.pg_order_id where supm.pg_order_id is null")
    @Query("From SarvmPgOrder spod left outer join SarvmUserPaymentMap supm on spod.pgOrderId = supm.pgOrderId where supm.pgOrderId is null")
    List<SarvmPgOrder> findAllWhichNotPresentInThirdTable();
}
