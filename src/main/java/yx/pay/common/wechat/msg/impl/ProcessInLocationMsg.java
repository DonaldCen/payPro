package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

;

public class ProcessInLocationMsg implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		
		InLocationMsg msg = (InLocationMsg)inMsg;
		OutTextMsg outMsg = new OutTextMsg(inMsg);
		outMsg.setContent("已收到地理位置消息:" +
							"\nlocation_X = " + msg.getLocation_X() +
							"\nlocation_Y = " + msg.getLocation_Y() + 
							"\nscale = " + msg.getScale() +
							"\nlabel = " + msg.getLabel());
		ReaderUtils.render(request,response,outMsg);
	}

}
