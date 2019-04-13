package yx.pay.common.wechat.msg.impl;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import yx.pay.common.wechat.msg.IProcessMsg;
import yx.pay.common.wechat.msg.ReaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessInSpeechRecognitionResults implements IProcessMsg {

	@Override
	public void processInMsg(HttpServletRequest request,
                             HttpServletResponse response, InMsg inMsg) {
		InSpeechRecognitionResults msg = (InSpeechRecognitionResults)inMsg;
		String content = "语音识别结果： " + msg.getRecognition();
		OutTextMsg outMsg= new OutTextMsg(ReaderUtils.getInMsgXml(ReaderUtils.getInMsgString(request)));
		outMsg.setContent(content);
		ReaderUtils.render(request,response,outMsg);
	}

}
