package com.sgc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class JwtUtils {

    /**
     * @param payload 有效载荷，主体数据
     * @param secret  密钥，服务器保存
     * @return jwt令牌
     */
    public static String encode(String user, Map<String, Object> payload, String secret) {
        JWTCreator.Builder builder = JWT.create();
        //添加数据
        builder.withAudience(user);
        //过期时间 TODO
        builder.withExpiresAt(Instant.now().plusSeconds(18000));
        //颁发时间
        builder.withIssuedAt(Instant.now());
        //颁发人
        builder.withIssuer("admin");
        //令牌编号
        builder.withJWTId(UUID.randomUUID().toString());

        //主题
        builder.withSubject("jwt");

        //何时之前不生效
        builder.withNotBefore(Instant.now());

        /*payload.forEach((k, v) -> {
            builder.withClaim(k, v.toString());
        });*/

        builder.withPayload(payload);

        //加密签名
        return builder.sign(Algorithm.HMAC256(secret));
    }

    /**
     * 解密jwt
     *
     * @param token  jwt加密字符串
     * @param secret 密钥
     * @return 解密之后的jwt
     */
    public static DecodedJWT decode(String token, String secret) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }


    public static void main(String[] args) {

        PasswordEncryptor pe = new StrongPasswordEncryptor();
        String pwd = "123456";


    }
}
