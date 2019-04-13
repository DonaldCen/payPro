package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import lombok.extern.slf4j.Slf4j;
import yx.pay.common.wechat.msg.IProcessMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProcessInFollowEvent implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InFollowEvent msg = (InFollowEvent)inMsg;
		log.info("ProcessInFollowEvent");
	}

}
