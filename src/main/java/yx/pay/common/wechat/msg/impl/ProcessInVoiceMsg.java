package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInVoiceMsg implements IProcessMsg {

	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InVoiceMsg msg = (InVoiceMsg)inMsg;
		OutVoiceMsg outMsg = new OutVoiceMsg(msg);
		// 将刚发过来的语音再发回去
		outMsg.setMediaId(msg.getMediaId());
		ReaderUtils.render(request,response,outMsg);
	}
}
