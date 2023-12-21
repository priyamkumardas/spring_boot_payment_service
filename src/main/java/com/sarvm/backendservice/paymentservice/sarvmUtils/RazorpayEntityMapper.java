package com.sarvm.backendservice.paymentservice.sarvmUtils;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.response.razorpayOrderObjectAttr;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.response.RazorpayPaymentObjectAttr;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sarvm.backendservice.paymentservice.sarvmUtils.DataTypeUtils.formatDecimal;

public class RazorpayEntityMapper {

    @Autowired
    public static ModelMapper modelMapper;

    public static SarvmPgOrder razorpayToOrderEntity(Order orderDto, SarvmPgOrder orderEntity, String pg_name){
        orderEntity.setAmount(formatDecimal(orderDto.get(razorpayOrderObjectAttr.AMOUNT)) * Double.valueOf(0.01));
        orderEntity.setAmountDue(formatDecimal(orderDto.get(razorpayOrderObjectAttr.AMOUNT_DUE)) * Double.valueOf(0.01));
        orderEntity.setAmountPaid(formatDecimal(orderDto.get(razorpayOrderObjectAttr.AMOUNT_PAID)) * Double.valueOf(0.01));
        orderEntity.setPgOrderId(orderDto.get(razorpayOrderObjectAttr.ID));
        orderEntity.setOrderStatus(orderDto.get(razorpayOrderObjectAttr.STATUS));
        orderEntity.setNotes(orderDto.get(razorpayOrderObjectAttr.NOTES).toString());
        orderEntity.setCurrency(orderDto.get(razorpayOrderObjectAttr.CURRENCY));
        orderEntity.setCreationTime((Date)orderDto.get(razorpayOrderObjectAttr.CREATED_AT));
        orderEntity.setPaymentGatewayName(pg_name);
        orderEntity.setPartialPayment(orderDto.get(razorpayOrderObjectAttr.PARTIAL_PAYMENT));
        orderEntity.setReceiptId(orderDto.get(razorpayOrderObjectAttr.RECEIPT));
        orderEntity.setAttempts(orderDto.get(razorpayOrderObjectAttr.ATTEMPTS));
        return orderEntity;
    }

    public static SarvmPgPayment fillPgPaymentValues(Payment payment, SarvmPgPayment sarvmPgPayment) {
        Map<String,String> statusMap = razorpayStatusToSarvmStatusMapper();

        if(payment.get(RazorpayPaymentObjectAttr.STATUS)!=null && !payment.get(RazorpayPaymentObjectAttr.STATUS).equals(JSONObject.NULL)){
            sarvmPgPayment.setPaymentStatus(statusMap.get(payment.get(RazorpayPaymentObjectAttr.STATUS)));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ID)!=null && !payment.get(RazorpayPaymentObjectAttr.ID).equals(JSONObject.NULL)){
            sarvmPgPayment.setPgPaymentId(payment.get(RazorpayPaymentObjectAttr.ID));
        }

        if(payment.get(RazorpayPaymentObjectAttr.STATUS)!=null && !payment.get(RazorpayPaymentObjectAttr.STATUS).equals(JSONObject.NULL)){
            sarvmPgPayment.setPgStatus(payment.get(RazorpayPaymentObjectAttr.STATUS));
        }

        if(payment.get(RazorpayPaymentObjectAttr.AMOUNT)!=null && !payment.get(RazorpayPaymentObjectAttr.AMOUNT).equals(JSONObject.NULL)){
            sarvmPgPayment.setAmount(formatDecimal(payment.get(razorpayOrderObjectAttr.AMOUNT)) * Double.valueOf(0.01));
        }

        if(payment.get(RazorpayPaymentObjectAttr.TAX)!=null && !payment.get(RazorpayPaymentObjectAttr.TAX).equals(JSONObject.NULL)){
            sarvmPgPayment.setTax(formatDecimal(payment.get(RazorpayPaymentObjectAttr.TAX))* Double.valueOf(0.01));
        }

        if(payment.get(RazorpayPaymentObjectAttr.FEE)!=null && !payment.get(RazorpayPaymentObjectAttr.FEE).equals(JSONObject.NULL)){
            sarvmPgPayment.setFee(formatDecimal(payment.get(RazorpayPaymentObjectAttr.FEE))* Double.valueOf(0.01));
        }

        if(payment.get(RazorpayPaymentObjectAttr.NOTES)!=null && !payment.get(RazorpayPaymentObjectAttr.NOTES).equals(JSONObject.NULL)){
            sarvmPgPayment.setNotes(payment.get(RazorpayPaymentObjectAttr.NOTES).toString());
        }

        if(payment.get(RazorpayPaymentObjectAttr.CONTACT)!=null && !payment.get(RazorpayPaymentObjectAttr.CONTACT).equals(JSONObject.NULL)){
            sarvmPgPayment.setContact(payment.get(RazorpayPaymentObjectAttr.CONTACT));
        }

        if(payment.get(RazorpayPaymentObjectAttr.EMAIL)!=null && !payment.get(RazorpayPaymentObjectAttr.EMAIL).equals(JSONObject.NULL)){
            sarvmPgPayment.setEmail(payment.get(RazorpayPaymentObjectAttr.EMAIL));
        }

        if(payment.get(RazorpayPaymentObjectAttr.VPA)!=null && !payment.get(RazorpayPaymentObjectAttr.VPA).equals(JSONObject.NULL)){
            sarvmPgPayment.setVpa(payment.get(RazorpayPaymentObjectAttr.VPA));
        }

        if(payment.get(RazorpayPaymentObjectAttr.WALLET)!=null && (!payment.get(RazorpayPaymentObjectAttr.WALLET).equals(JSONObject.NULL))){
            sarvmPgPayment.setWallet(payment.get(RazorpayPaymentObjectAttr.WALLET));
        }

        if(payment.get(RazorpayPaymentObjectAttr.BANK)!=null && !payment.get(RazorpayPaymentObjectAttr.BANK).equals(JSONObject.NULL)){
            sarvmPgPayment.setBank(payment.get(RazorpayPaymentObjectAttr.BANK));
        }

        if(payment.get(RazorpayPaymentObjectAttr.CARD_ID)!=null && !payment.get(RazorpayPaymentObjectAttr.CARD_ID).equals(JSONObject.NULL)){
            sarvmPgPayment.setCardId(payment.get(RazorpayPaymentObjectAttr.CARD_ID));
        }

        if(payment.get(RazorpayPaymentObjectAttr.DESCRIPTION)!=null && !payment.get(RazorpayPaymentObjectAttr.DESCRIPTION).equals(JSONObject.NULL)){
            sarvmPgPayment.setDescription(payment.get(RazorpayPaymentObjectAttr.DESCRIPTION));
        }

        if(payment.get(RazorpayPaymentObjectAttr.CAPTURED)!=null && !payment.get(RazorpayPaymentObjectAttr.CAPTURED).equals(JSONObject.NULL)){
            sarvmPgPayment.setCaptured(payment.get(RazorpayPaymentObjectAttr.CAPTURED));
        }

        if(payment.get(RazorpayPaymentObjectAttr.AMOUNT_REFUNDED)!=null && !payment.get(RazorpayPaymentObjectAttr.AMOUNT_REFUNDED).equals(JSONObject.NULL)){
            sarvmPgPayment.setAmountRefunded((Double)formatDecimal(payment.get(RazorpayPaymentObjectAttr.AMOUNT_REFUNDED))* Double.valueOf(0.01));
        }

        if(payment.get(RazorpayPaymentObjectAttr.METHOD)!=null && !payment.get(RazorpayPaymentObjectAttr.METHOD).equals(JSONObject.NULL)){
            sarvmPgPayment.setMethod(payment.get(RazorpayPaymentObjectAttr.METHOD));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ERROR_STEP)!=null && !payment.get(RazorpayPaymentObjectAttr.ERROR_STEP).equals(JSONObject.NULL)){
            sarvmPgPayment.setErrorStep(payment.get(RazorpayPaymentObjectAttr.ERROR_STEP));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ERROR_SOURCE)!=null && !payment.get(RazorpayPaymentObjectAttr.ERROR_SOURCE).equals(JSONObject.NULL)){
            sarvmPgPayment.setErrorSource(payment.get(RazorpayPaymentObjectAttr.ERROR_SOURCE));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ERROR_DECSCRIPTION)!=null && !payment.get(RazorpayPaymentObjectAttr.ERROR_DECSCRIPTION).equals(JSONObject.NULL)){
            sarvmPgPayment.setErrorDescription(payment.get(RazorpayPaymentObjectAttr.ERROR_DECSCRIPTION));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ERROR_CODE)!=null && !payment.get(RazorpayPaymentObjectAttr.ERROR_CODE).equals(JSONObject.NULL)){
            sarvmPgPayment.setErrorCode(payment.get(RazorpayPaymentObjectAttr.ERROR_CODE));
        }

        if(payment.get(RazorpayPaymentObjectAttr.CURRENCY)!=null && !payment.get(RazorpayPaymentObjectAttr.CURRENCY).equals(JSONObject.NULL)){
            sarvmPgPayment.setCurrency(payment.get(RazorpayPaymentObjectAttr.CURRENCY));
        }

        if(payment.get(RazorpayPaymentObjectAttr.INVOICE_ID)!=null && !payment.get(RazorpayPaymentObjectAttr.INVOICE_ID).equals(JSONObject.NULL)){
            sarvmPgPayment.setInvoiceId(payment.get(RazorpayPaymentObjectAttr.INVOICE_ID));
        }

        if(payment.get(RazorpayPaymentObjectAttr.INTERNATIONAL)!=null && !payment.get(RazorpayPaymentObjectAttr.INTERNATIONAL).equals(JSONObject.NULL)){
            sarvmPgPayment.setInternational(payment.get(RazorpayPaymentObjectAttr.INTERNATIONAL));
        }

        if(payment.get(RazorpayPaymentObjectAttr.CREATED_AT)!=null && !payment.get(RazorpayPaymentObjectAttr.CREATED_AT).equals(JSONObject.NULL)){
            sarvmPgPayment.setCreatedAt((Date)payment.get(RazorpayPaymentObjectAttr.CREATED_AT));
        }

        if(payment.get(RazorpayPaymentObjectAttr.ACQUIRER_DATA)!=null && !payment.get(RazorpayPaymentObjectAttr.ACQUIRER_DATA).equals(JSONObject.NULL)){
            sarvmPgPayment.setAcquirerData(payment.get(RazorpayPaymentObjectAttr.ACQUIRER_DATA).toString());
        }

        if(payment.get(RazorpayPaymentObjectAttr.ERROR_REASON)!=null && !payment.get(RazorpayPaymentObjectAttr.ERROR_REASON).equals(JSONObject.NULL)){
            sarvmPgPayment.setErrorReason(payment.get(RazorpayPaymentObjectAttr.ERROR_REASON));
        }

        if(payment.get(RazorpayPaymentObjectAttr.REFUND_STATUS)!=null && !payment.get(RazorpayPaymentObjectAttr.REFUND_STATUS).equals(JSONObject.NULL)){
            sarvmPgPayment.setRefundStatus(payment.get(RazorpayPaymentObjectAttr.REFUND_STATUS));
        }

        return sarvmPgPayment;
    }

    public static Map<String,String> razorpayStatusToSarvmStatusMapper(){
        Map<String,String> map = new HashMap<>();
        List<String> statusList = List.of("created","authorized","captured","refunded","failed");
        map.put("created", PaymentStatus.PG_INIT.toString());
        map.put("authorized", PaymentStatus.PG_PENDING.toString());
        map.put("captured", PaymentStatus.PG_CAPTURED.toString());
        map.put("refunded", PaymentStatus.PG_REFUNDED.toString());
        map.put("failed", PaymentStatus.PAYMENT_FAILED.toString());
        return map;
    }

}
