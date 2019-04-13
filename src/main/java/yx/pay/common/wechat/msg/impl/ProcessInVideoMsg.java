package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInVideoMsg implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InVideoMsg msg = (InVideoMsg)inMsg;
		OutTextMsg outMsg = new OutTextMsg(msg);
		outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + msg.getMediaId());
		ReaderUtils.render(request,response,outMsg);
	}

}
