package yx.pay.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yx.pay.system.domain.wx.WxConfig;
import yx.pay.system.service.WxPayService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Slf4j
@Component
public class QrCodeUtil {
    private static final String QR_CODE_PIC_PREFIX = "QR_CODE_PIC_";

    @Autowired
    private WxConfig config;

    /*
     * 定义二维码的宽高
     */
    private static int WIDTH=300;
    private static int HEIGHT=300;
    private static String FORMAT="png";//二维码格式
    //生成二维码
    public  void createQrCode(String content,String url){
        //定义二维码参数
        Map hints=new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置容错等级
        hints.put(EncodeHintType.MARGIN, 2);//设置边距默认是5
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path path = new File(url).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, path);//写到指定路径下
        } catch (Exception e) {
            log.error("create qrCode error..",e);
        }

    }
    public String getQrCodePicName(String userId){
        StringBuffer sb = new StringBuffer();
        final String separator = File.separator;
        sb.append(config.getQrCodeUrl()).append(separator);
        sb.append(QR_CODE_PIC_PREFIX).append(userId);
        sb.append(".png");
        return sb.toString();
    }
    //读取二维码
    public static void readZxingQrCode(){
        MultiFormatReader reader = new MultiFormatReader();
        File file = new File("E:\\qr.png");
        try {
            BufferedImage image = ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Map hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
            Result result= reader.decode(binaryBitmap,hints);
            System.out.println("解析结果:"+result.toString());
            System.out.println("二维码格式:"+result.getBarcodeFormat());
            System.out.println("二维码文本内容:"+result.getText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String qrcode = "weixin://wxpay/bizpayurl?appid=wx2421b1c4370ec43b" +
                "&mch_id=10000100" +
                "&nonce_str=f6808210402125e30663234f94c87a8c&product_id=1&time_stamp=1415949957&sign=512F68131DD251DA4A45DA79CC7EFE9D";
//        readZxingQrCode();
    }

}
