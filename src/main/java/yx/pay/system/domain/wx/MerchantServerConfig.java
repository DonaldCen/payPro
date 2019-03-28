package yx.pay.system.domain.wx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 0151717 on 2019/3/23.
 */

@ConfigurationProperties(prefix="merchant")
@Component
public class MerchantServerConfig {
    private String merchantId;//商户ID
    private String apiKey;//商户API密钥
    private String certPath;//证书文件路径
    private String apiv3Key;
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getApiv3Key() {
        return apiv3Key;
    }

    public void setApiv3Key(String apiv3Key) {
        this.apiv3Key = apiv3Key;
    }
}
