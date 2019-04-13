package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import lombok.extern.slf4j.Slf4j;
import yx.pay.common.wechat.msg.IProcessMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class ProcessInTextMsg implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InTextMsg inTextMsg = (InTextMsg)inMsg;
		log.info("ProcessInTextMsg");
	}
}
