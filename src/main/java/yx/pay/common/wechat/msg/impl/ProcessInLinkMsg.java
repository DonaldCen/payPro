package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInLinkMsg implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InLinkMsg msg = (InLinkMsg)inMsg;
		
		OutNewsMsg outMsg = new OutNewsMsg(msg);
		outMsg.addNews("链接消息已成功接收", "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)" , "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0", msg.getUrl());
		ReaderUtils.render(request,response,outMsg);
	}

}
