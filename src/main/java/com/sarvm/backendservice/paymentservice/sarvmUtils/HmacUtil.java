package com.sarvm.backendservice.paymentservice.sarvmUtils;

import org.apache.commons.codec.digest.HmacUtils;

public class HmacUtil {

    public static String hmacWithApacheCommons(String algorithm, String data, String key) {
        String hmac = new HmacUtils(algorithm, key).hmacHex(data);

        return hmac;
    }
}
