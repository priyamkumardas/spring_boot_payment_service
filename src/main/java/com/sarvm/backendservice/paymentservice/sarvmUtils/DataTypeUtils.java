package com.sarvm.backendservice.paymentservice.sarvmUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DataTypeUtils {

    public static DecimalFormat df = new DecimalFormat("0.00");

    public static Double formatDecimal(Integer value) {
        Double amount = Double.valueOf(value);
        df.setRoundingMode(RoundingMode.UP);
        amount = (Double) ((Double)amount);
        df.format(amount);
        return amount;
    }
}
