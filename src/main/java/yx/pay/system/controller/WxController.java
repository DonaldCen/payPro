package yx.pay.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.utils.IPUtil;
import yx.pay.common.utils.WxUtil;
import yx.pay.common.utils.XMLUtil;
import yx.pay.system.domain.wx.OrderInfoVo;
import yx.pay.system.service.OrderInfoService;
import yx.pay.system.service.ProductService;
import yx.pay.system.service.WxPayService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/wx")
public class WxController {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private WxUtil wxUtil;

    /**
     * 指定地方，生成url,然后存储，然后展示
     * param:
     *  merchantId: xx
     *  productId:  xx
     */
    @PostMapping("generateQrCode")
    public FebsResponse generateQrCode(@RequestBody JSONObject jsonParam) {
        int merchantId = (Integer) jsonParam.get("merchantId");
        int productId = (Integer) jsonParam.get("productId");
        try {
            wxPayService.generateQrCodeImages(merchantId,productId);
        } catch (Exception e) {
            log.error("generateQrCode error..", e);
            return new FebsResponse().fail(e.getMessage());
        }
        return new FebsResponse().success();
    }

    @PostMapping("pay")
    public FebsResponse pay(@RequestBody OrderInfoVo orderInfoVo,HttpServletRequest request) throws Exception {
        orderInfoVo.setIp(IPUtil.getIpAddr(request));
        return orderInfoService.createOrderInfo(orderInfoVo);
    }

    @RequestMapping(value="weixin_notify",method=RequestMethod.POST)
    public void weixin_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        Map<String, String> resultMap = WXPayUtil.xmlToMap(sb.toString());
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = resultMap.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = resultMap.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        if(wxUtil.isTenpaySign("UTF-8",packageParams)){
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                String orderNo = (String) packageParams.get("out_trade_no");
                log.info("微信订单号{}付款成功",orderNo);
                //这里 根据实际业务场景 做相应的操作
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                log.info("支付失败,错误信息：{}",packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }else {
            log.info("通知签名验证失败");
        }
    }


    /**
     * 模式一支付回调URL(生成二维码见 qrCodeUtil)
     * 商户支付回调URL设置指引：进入公众平台-->微信支付-->开发配置-->扫码支付-->修改
     * @param request
     * @param response
     *
     */
    @RequestMapping(value="bizpayurl",method= RequestMethod.POST)
    public void bizpayurl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("模式一支付回调URL");
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        getResponse(request,packageParams);
        log.info("return data:[{}]", JSON.toJSON(packageParams));
        //判断签名是否正确
        if (wxUtil.isTenpaySign("UTF-8", packageParams)) {
            String id = String.valueOf(packageParams.get("product_id"));
            if(StringUtils.isNotBlank(id)){


            }
        }else{
            log.info("签名错误");
        }
    }

    private void getResponse(HttpServletRequest request,SortedMap<Object, Object> packageParams) throws JDOMException, IOException {
        //读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        //解析xml成map
        Map<String, String> map = XMLUtil.doXMLParse(sb.toString());
        //过滤空 设置 TreeMap
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = map.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
    }


}
