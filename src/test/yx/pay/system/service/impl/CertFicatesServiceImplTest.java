package yx.pay.system.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.service.CertFicatesService;

import static org.junit.Assert.*;
import yx.pay.common.utils.EncryptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * Created by 0151717 on 2019/3/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CertFicatesServiceImplTest {
    @Autowired
    public CertFicatesService certFicatesService;
    @Autowired
    public MerchantServerConfig merchantServerConfig;
    @Test
    public void testGetCertFicates() throws Exception {
        System.out.println("-----------test-------------");
        certFicatesService.getCertFicates();
        System.out.println("-----------dddddddddddddddddd-------------");

    }
/*
    @Test
    public void decryptCertSNTest() {
        try {
            String content = certFicatesService.decryptCertSN("associatedData", "nonce", "cipherText", "apiv3Key");
            String encrypt = EncryptionUtils.rsaEncrypt("我的身份证", content);
            System.out.println("身份证的密文了:"+encrypt);
           // log.info("身份证的密文了 {}",encrypt);
        } catch (Exception e) {
            System.out.println("解密异常啦:"+e);
           // log.error("解密异常啦 {}", e);
        }
    }
    */
    @Test
    public void rsaEncryptTest(){
        try {
            String content="xxxx";
            String encrypt = EncryptionUtils.rsaEncrypt("我的身份证xxx", merchantServerConfig.getCertPath());
            System.out.println("身份证的密文了:"+encrypt);
    } catch (Exception e) {
        System.out.println("解密异常啦:"+e);
        // log.error("解密异常啦 {}", e);
    }

    }
}