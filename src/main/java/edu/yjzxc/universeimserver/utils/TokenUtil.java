package edu.yjzxc.universeimserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME = 1 * 1000;

    private static final String TOKEN_SECRET = "C18z-01ds-2341-Sc1e54adf-D5g21DD-13fff-55DD-ee5";

    public static String buildToken(String identity, String otherContent) {
        // 过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 密钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // 这里为什么要try catch？
        return JWT.create()
                .withHeader(headers)
                .withClaim("identity", identity)
                .withClaim("otherContent", otherContent)
                // ip地址
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public static boolean verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            // 续签 写一个enum？
            // 越想越头痛。。。
            // http太难保证安全了
            // 后面搞个https把
            // 暂时就这样把，cid和ip验证其实都不完全靠谱

            return true;
        } catch (JWTDecodeException e) {
            return false;
        }
    }

}
