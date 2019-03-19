package yx.pay.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class HttpContextUtil {

	private HttpContextUtil(){

	}
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}
	@Bean
	public RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}
}
