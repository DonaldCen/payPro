package yx.pay.common.utils;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getContextPath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://")
                .append(request.getServerName())
                .append(request.getContextPath());
        String path = sb.toString();
        return path;
    }
}
