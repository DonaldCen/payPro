package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import yx.pay.common.wechat.msg.IProcessMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInQrCodeEvent implements IProcessMsg {
	
	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {

	}
}
