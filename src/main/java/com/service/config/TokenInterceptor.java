package com.service.config;

import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.user.controller.controller.LoginController;
import com.service.user.controller.domian.UserIp;
import com.service.user.dao.entity.DUser;
import com.service.user.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserIp userIp;
    @Autowired
    private LoginController loginController;
    @Autowired
    private UserMapper userMapper;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = this.getIpAddress(request);
        String token = request.getHeader("token");
        boolean result = false;
        for (String ips : userIp.getIp()) {
            if (ips.equals(ip)) {
                result = true;
                break;
            }
        }
        if (!result) {
            this.handleFalseResponse(response, "IP未认证", 0);
            return false;
        }
        DUser dUser = userMapper.getUserBy(token);
        long NowTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(dUser) || NowTime - dUser.getTokenTime() > 2592000) {
            this.handleFalseResponse(response, "重新登入", 3);
            return false;
        }

        return result;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        System.out.println(ip);
        return ip;

    }

    private void handleFalseResponse(HttpServletResponse response, String msg, Integer code)
            throws Exception {
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("msg",msg)
 ;       response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(object.toJSONString());
        response.getWriter().flush();
    }
}
