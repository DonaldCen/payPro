package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import lombok.extern.slf4j.Slf4j;
import yx.pay.common.wechat.msg.IProcessMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProcessInMenuEvent implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InMenuEvent msg = (InMenuEvent)inMsg;
		String eventKey = msg.getEventKey();
		log.info(eventKey);
	}
}
