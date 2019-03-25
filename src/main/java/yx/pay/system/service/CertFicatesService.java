package yx.pay.system.service;



/**
 * 平台序证书获取
 *   Created by 0151717 on 2019/3/23.
 */
public interface CertFicatesService {
    String getCertFicates();
    /**
     * 平台证书解密
     * @param associatedData
     * @param nonce
     * @param cipherText
     * @param apiv3Key
     * @return
     */
    String decryptCertSN(String associatedData, String nonce, String cipherText, String apiv3Key)throws Exception;

    /**
     * 敏感内容加密,返回密文
     * @param content
     * @return
     * @throws Exception
     */
    String encryptPkcs1padding(String content) throws  Exception;
    /**
     * 解析证书
     */

}