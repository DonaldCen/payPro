/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package yx.pay.common.wechat.api;

import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.ApiResult;

/**
 * 
 * @author lifeng
 * 获取OAuthApi
 */
public class OAuthApi {
	
	private static String getOpenId = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	/**
	 * 获取openId
	 * @param appid
	 * @param secret
	 * @param code
	 * @return ApiResult
	 */
	public static ApiResult getOpenId(String appid, String secret, String code) {
		String url = getOpenId.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		String jsonResult = HttpKit.get(url);
		return new ApiResult(jsonResult);
	}
}


