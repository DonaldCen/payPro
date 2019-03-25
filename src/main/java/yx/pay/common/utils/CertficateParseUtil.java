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
                certificateJsonDataArray=jo.getJSONArray("encrypt_certificate");
                for(int j=0;j<certificateJsonDataArray.size();j++){
                    JSONObject tempJsonObject=certificateJsonDataArray.getJSONObject(j);
                    if(tempJsonObject.containsKey("algorithm")){
                        algorithm=jo.getString("algorithm");
                    }
                    if(tempJsonObject.containsKey("nonce")){
                        nonce=jo.getString("nonce");
                    }
                    if(tempJsonObject.containsKey("associated_data")){
                        associated_data=jo.getString("associated_data");
                    }
                    if(tempJsonObject.containsKey("ciphertext")){
                        ciphertext=jo.getString("ciphertext");
                    }
                }
            }
        }
    }catch(Exception e){

    }

}

}
