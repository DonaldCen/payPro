package yx.pay.common.wechat.kit;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class SignatureCheckKit {

    public static final SignatureCheckKit me = new SignatureCheckKit();

    private static String token = "yxPay";

    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String array[] = {token, timestamp, nonce};
        Arrays.sort(array);
        String tempStr = new StringBuilder().append(array[0] + array[1] + array[2]).toString();
        tempStr = EncryptionKit.sha1Encrypt(tempStr);
        log.info("tempStr="+tempStr);
        return tempStr.equalsIgnoreCase(signature);
    }
}
