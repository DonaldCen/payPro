package yx.pay.common.wechat.msg;

import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.msg.OutMsgXmlBuilder;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ReaderUtils {
    private static final String encoding = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "text/plain; charset="
            + encoding;

    private static final String XML_CONTENT_TYPE = "text/xml; charset="
            + encoding;

    /**
     * 在接收到微信服务器的 InMsg 消息后后响应 OutMsg 消息
     */
    public static void render(HttpServletRequest request, HttpServletResponse response, OutMsg outMsg) {
        String outMsgXml = OutMsgXmlBuilder.build(outMsg);
        renderText(response,outMsgXml, XML_CONTENT_TYPE);
    }

    public static void renderText(HttpServletResponse response, String text ,String contentType) {
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might
            // not implement
            // Cache-Control and
            // might only implement
            // Pragma: no-cache
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType(contentType==null?DEFAULT_CONTENT_TYPE:contentType);
            response.setCharacterEncoding(encoding);
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static InMsg getInMsgXml(String inMsgString) {
        return InMsgParaser.parse(inMsgString);
    }

    public static String getInMsgString(HttpServletRequest request) {
        String inMsgString = HttpKit.readIncommingRequestData(request);
        log.info(inMsgString);
        return inMsgString;
    }
}
