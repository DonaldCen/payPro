package yx.pay.system.service;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
public interface WxPayService {
    /**
     * 生成二维码图片
     */
    void generateQrCodeImages(int userId,int productId);

}
