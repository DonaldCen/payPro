package yx.pay.common.servlet;

import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.event.*;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import lombok.extern.slf4j.Slf4j;
import yx.pay.common.wechat.kit.SignatureCheckKit;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;
import yx.pay.common.wechat.msg.impl.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(urlPatterns = "/wx")
public class ServerVlidationServlet extends HttpServlet {
    private static final String encoding = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "text/plain; charset="
            + encoding;

    /**
     * 校验地址跟微信服务器是否打通
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configServer(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String inMsgString = ReaderUtils.getInMsgString(request);
        IProcessMsg processMsg = null;
        InMsg msg = ReaderUtils.getInMsgXml(inMsgString);
        if (msg instanceof InTextMsg){
            processMsg = new ProcessInTextMsg();
        }else if (msg instanceof InImageMsg){
            processMsg = new ProcessInImageMsg();
        }else if (msg instanceof InVoiceMsg){
            processMsg = new ProcessInVoiceMsg();
        }else if (msg instanceof InVideoMsg){
            processMsg = new ProcessInVideoMsg();
        }else if (msg instanceof InLocationMsg){
            processMsg = new ProcessInLocationMsg();
        }else if (msg instanceof InLinkMsg){
            processMsg = new ProcessInLinkMsg();
        }else if (msg instanceof InFollowEvent){
            processMsg = new ProcessInFollowEvent();
        }else if (msg instanceof InQrCodeEvent){
            processMsg = new ProcessInQrCodeEvent();
        }else if (msg instanceof InLocationEvent){
            processMsg = new ProcessInLocationEvent();
        }else if (msg instanceof InMenuEvent){
            processMsg = new ProcessInMenuEvent();
        }else if (msg instanceof InSpeechRecognitionResults){
            processMsg = new ProcessInSpeechRecognitionResults();
        }else if(msg instanceof InTemplateMsgEvent){
            return;
        }else{
            log.error("未能识别的消息类型。 消息 xml 内容为：\n" +inMsgString);
            return;
        }
        //处理
        processMsg.processInMsg(request, response, msg);
    }
    public void configServer(HttpServletRequest request, HttpServletResponse response) {
        // 通过 echostr 判断请求是否为配置微信服务器回调所需的 url 与 token
        String echostr = request.getParameter("echostr");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        boolean isOk = SignatureCheckKit.me.checkSignature(signature, timestamp, nonce);
        if (isOk)
            ReaderUtils.renderText(response, echostr, DEFAULT_CONTENT_TYPE);
        else
            log.error("验证失败：configServer");
    }
}
