package com.ipl.prediction.iplprediction.config;

import com.ipl.prediction.iplprediction.entity.IplUser;
import com.ipl.prediction.iplprediction.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String userId = request.getHeader("X-User-Id");
        if (userId == null) {
            userId = (String) request.getSession().getAttribute("userId");
        }
        if (userId != null) {
            IplUser user = userRepository.findByUserId(userId);
            if (user != null && Boolean.TRUE.equals(user.getIsAdmin())) {
                return true;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Admin access required\"}");
        return false;
    }
}