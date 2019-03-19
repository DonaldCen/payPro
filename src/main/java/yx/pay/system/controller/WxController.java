package yx.pay.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.utils.QrCodeUtil;
import yx.pay.common.utils.WxUtil;
import yx.pay.common.utils.XMLUtil;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.WxConfig;
import yx.pay.system.service.OrderInfoService;
import yx.pay.system.service.WxPayService;

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
    private WxUtil wxUtil;

    /**
     * 指定地方，生成url,然后存储，然后展示
     *
     */
    @PostMapping("generateQrCode")
    public FebsResponse generateQrCode(@RequestBody JSONObject jsonParam) {
        int userId = (Integer) jsonParam.get("userId");
        try {
            wxPayService.generateQrCodeImages(userId);
        } catch (Exception e) {
            log.error("generateQrCode error..", e);
            return new FebsResponse().fail(e.getMessage());
        }
        return new FebsResponse().success();
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
                int productId = Integer.parseInt(id);
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
