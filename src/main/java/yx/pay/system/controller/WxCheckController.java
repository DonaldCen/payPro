package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yx.pay.common.utils.UrlUtil;
import yx.pay.common.wechat.Button;
import yx.pay.common.wechat.ViewButton;
import yx.pay.common.wechat.kit.AccessTokenUtil;
import yx.pay.system.domain.Menu;
import yx.pay.system.domain.wx.WxConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping({"/"})
public class WxCheckController {

    @Autowired
    private AccessTokenUtil tokenUtil;
    @Autowired
    private WxConfig wxConfig;

    @RequestMapping({"MP_verify_TWD7pen1yswv10ba.txt"})
    public void getTxt(HttpServletResponse response) throws Exception {
        File file = ResourceUtils.getFile("classpath:MP_verify_au8scmNSoBdeSF8h.txt");
        String content = readTxt(file);
        log.info(content);
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(content.getBytes());
        out.flush();
        out.close();
    }

    public String readTxt(File file) throws IOException {
        String s = "";
        InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        StringBuffer content = new StringBuffer();
        while ((s = br.readLine()) != null) {
            content = content.append(s);
        }
        return content.toString();
    }

    @GetMapping("addMenu")
    private void addWeixinMenu() {
        Menu menu = new Menu();
        List<Button> buttonList = new ArrayList<Button>();
        Button viewButton = new ViewButton();
        viewButton.setUrl("");
        viewButton.setName("测试");
        viewButton.setType("view");
    }
    @RequestMapping(value="payPage")
    public String jump(){
        return "test";
    }

    @RequestMapping(value="toPay")
    public void toPay(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"+"appid="+wxConfig.getAppId()+"&";
        url+="redirect_uri="+ UrlUtil.getContextPath(request) +"/payPage&";
        url+="response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        try {
            String enurl = java.net.URLDecoder.decode(url,"utf-8");
            System.out.println("llq url=================:"+enurl);
            response.sendRedirect(enurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
