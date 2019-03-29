package yx.pay.common.utils;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.security.cert.X509Certificate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;


/**
 * Created by 0151717 on 2019/3/23.
 */


public class EncryptionUtils {
    private static final String CIPHER_PROVIDER = "SunJCE";
    private static final String TRANSFORMATION_PKCS1Paddiing = "RSA/ECB/PKCS1Padding";
    private static final String CHAR_ENCODING = "UTF-8";//固定值，无须修改
    //数据加密方法
    private static byte[] encryptPkcs1padding(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(TRANSFORMATION_PKCS1Paddiing, CIPHER_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }
    //加密后的秘文，使用base64编码方法
    private static String encodeBase64(byte[] bytes) throws Exception {
        return Base64.getEncoder().encodeToString(bytes);
    }
    /**
     *  对敏感内容（入参Content）加密
     *  path 为平台序列号接口解密后的密钥 pem 路径
     */
    public static String rsaEncrypt(String Content, String path) throws Exception {
        final byte[] PublicKeyBytes = Files.readAllBytes(Paths.get(path));
        X509Certificate certificate = X509Certificate.getInstance(PublicKeyBytes);
        PublicKey publicKey = certificate.getPublicKey();
        return encodeBase64(encryptPkcs1padding(publicKey, Content.getBytes(CHAR_ENCODING)));
    }

    /**
     * 为了自己方便，多加个个传内容的，因为我解密后并没有保存到文件里，而是自己重新解密
     * 要问为什么？
     * 需求有多个服务商号，没办法
     * @param Content
     * @param certStr
     * @return
     * @throws Exception
     */

    public static String rsaEncryptByCert(String Content, String certStr) throws Exception {
        X509Certificate certificate = X509Certificate.getInstance(certStr.getBytes());
        PublicKey publicKey = certificate.getPublicKey();
        return encodeBase64(encryptPkcs1padding(publicKey, Content.getBytes(CHAR_ENCODING)));
    }

    /**
     * 根据pem公钥路径获取公钥字符串
     *
     * @param cerPath 路径
     * @return 公钥字符串
     */
    /*
    public static String getPubKeyContentString(String cerPath) {
        // 读取本机存放的PKCS8证书文件
        String currentPath = Thread.currentThread().getContextClassLoader().getResource(cerPath).getPath();
        try {
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader(currentPath));

            StringBuffer publickey = new StringBuffer();
            String line;

            while (null != (line = br.readLine())) {
                if ((line.contains("BEGIN PUBLIC KEY") || line.contains("END PUBLIC KEY")))
                    continue;
                publickey.append(line);
            }
            return publickey.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
*/
    /**
     * 将公钥字符串转换为公钥：
     * 根据公钥字符串获取公钥
     * @param key 公钥字符串
     * @return 返回公钥
     * @throws Exception
     */
    /*
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        //keyBytes = (new Base64().decode(key));
        keyBytes=key.getBytes();//注意，未测试
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    */
    /**
     * 获取加密结果
     * @param data 加密内容
     * @param cerPath 证书路径
     * @return 加密后的字符串
     */
    /*
    public static String getDataWithPem(String data,String cerPath) {
        //获取pubkey字符串内容
        String key=getPubKeyContentString(cerPath);

        PublicKey publicKey = null;
        Cipher cipher = null;
        try {
            publicKey = getPublicKey(key);
            cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] test = cipher.doFinal(data.getBytes("utf-8"));
            byte[] bytes = Base64.encodeBase64(test);
            String s = new String(bytes, "utf-8");
            System.out.println("加密结果【" + s);
            return s;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}