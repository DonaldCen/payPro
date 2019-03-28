package yx.pay.common.utils;
import java.io.FileNotFoundException;
import java.io.FileReader;

import  com.alibaba.fastjson.*;
/**
 * Created by 0151717 on 2019/3/24.
 */
public class CertficateParseUtil {
//    public String certificate;//整个证书内容
    public String serial_no;// 序列号
    public String effective_time;// 生效日期串
    public String expire_time;// 失效日期串
    public JSONArray certificateJsonDataArray;
    public String algorithm;
    public String nonce;
    public String associated_data;
    public String ciphertext;//密文

    public String getSerial_no() {
        return serial_no;
    }

    public String getEffective_time() {
        return effective_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public JSONArray getCertificateJsonDataArray() {
        return certificateJsonDataArray;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getNonce() {
        return nonce;
    }

    public String getAssociated_data() {
        return associated_data;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    @Override
    public String toString() {
        return "CertficateParseUtil{" +
                "serial_no='" + serial_no + '\'' +
                ", effective_time='" + effective_time + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", certificateJsonDataArray=" + certificateJsonDataArray +
                ", algorithm='" + algorithm + '\'' +
                ", nonce='" + nonce + '\'' +
                ", associated_data='" + associated_data + '\'' +
                ", ciphertext='" + ciphertext + '\'' +
                '}';
    }

    public void  certificateParse(String certificate){
    //获取Data对象
    try{
        JSONObject jsonObj = JSON.parseObject(certificate);
        JSONArray jsonDataArray = jsonObj.getJSONArray("data");
        if(jsonDataArray.isEmpty() || jsonDataArray.size()==0)  return ;
        for (int i = 0; i < jsonDataArray.size(); i++) {//依次灌入数据
            JSONObject jo = jsonDataArray.getJSONObject(i);
            if(jo.containsKey("serial_no")){
                serial_no=jo.getString("serial_no");
            }
            if(jo.containsKey("effective_time")){
                effective_time=jo.getString("effective_time");
            }
            if(jo.containsKey("expire_time")){
                expire_time=jo.getString("expire_time");
            }
            if(jo.containsKey("encrypt_certificate")){
                JSONObject  tempJsonObject=jo.getJSONObject("encrypt_certificate");
                    if(tempJsonObject.containsKey("algorithm")){
                        algorithm=tempJsonObject.getString("algorithm");
                    }
                    if(tempJsonObject.containsKey("nonce")){
                        nonce=tempJsonObject.getString("nonce");
                    }
                    if(tempJsonObject.containsKey("associated_data")){
                        associated_data=tempJsonObject.getString("associated_data");
                    }
                    if(tempJsonObject.containsKey("ciphertext")){
                        ciphertext=tempJsonObject.getString("ciphertext");
                    }

            }
        }
    }catch(Exception e){

    }



}

}
