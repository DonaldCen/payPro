package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInImageMsg implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InImageMsg msg = (InImageMsg)inMsg;
		OutImageMsg outMsg = new OutImageMsg(msg);
		// 将刚发过来的图片再发回去
		outMsg.setMediaId(msg.getMediaId());
		ReaderUtils.render(request,response,outMsg);
	}

}
