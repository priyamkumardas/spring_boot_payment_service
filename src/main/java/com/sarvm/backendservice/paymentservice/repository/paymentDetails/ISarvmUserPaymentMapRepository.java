package com.sarvm.backendservice.paymentservice.repository.paymentDetails;

import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmUserPaymentMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISarvmUserPaymentMapRepository extends CrudRepository<SarvmUserPaymentMap, Long> {

    SarvmUserPaymentMap findByPgOrderId(String pgOrderId);
}
