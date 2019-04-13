package yx.pay.common.wechat.msg;

import com.jfinal.weixin.sdk.msg.in.InMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理消息接口
 * @author Administrator
 * add by lifeng
 */
public interface IProcessMsg {
	public void processInMsg(HttpServletRequest request, HttpServletResponse response, InMsg inMsg);

}
