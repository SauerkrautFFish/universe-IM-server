package edu.yjzxc.universeimserver.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String token = request.getHeader("access_token");
        System.out.println(request.getServletPath());
        if(!Objects.isNull(token)) {
            String zxcId = TokenUtil.verify(token);
            if(null != zxcId) {
                request.setAttribute("zxcId", zxcId);
                return true;
            }
        }

        String respMsg = JSONObject.toJSONString(CommonResponse.status(ResponseEnum.TOKEN_ISSUE));

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(respMsg);
        return false;
    }
}
