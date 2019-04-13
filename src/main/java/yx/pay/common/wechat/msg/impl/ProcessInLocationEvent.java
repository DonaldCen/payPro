package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInLocationEvent implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InLocationEvent msg = (InLocationEvent)inMsg;
		OutTextMsg outMsg = new OutTextMsg(msg);
		outMsg.setContent("processInLocationEvent() 方法测试成功");
		ReaderUtils.render(request,response,outMsg);
	}

}
