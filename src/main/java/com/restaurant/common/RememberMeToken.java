package com.restaurant.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

/**
 * “记住我”Cookie 的签名工具。
 *
 * Cookie 中只保存用户名和过期时间，并使用 HMAC 签名，避免客户端伪造或篡改。
 * 签名密钥可通过系统属性 app.remember-me.secret 覆盖。
 */
public final class RememberMeToken {

    public static final String COOKIE_NAME = "rememberMe";

    public static final int MAX_AGE_SECONDS = 7 * 24 * 60 * 60;

    private static final long VALID_MILLIS = MAX_AGE_SECONDS * 1000L;

    private static final byte[] SECRET = System
            .getProperty("app.remember-me.secret", UUID.nameUUIDFromBytes("canteen-remember-me".getBytes(StandardCharsets.UTF_8)).toString())
            .getBytes(StandardCharsets.UTF_8);

    private RememberMeToken() {
    }

    public static String create(String username) {
        String payload = username + "|" + (System.currentTimeMillis() + VALID_MILLIS);
        String encoded = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encoded + "." + sign(encoded);
    }

    public static String verify(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        int dot = token.lastIndexOf('.');
        if (dot <= 0 || dot == token.length() - 1) {
            return null;
        }
        String encoded = token.substring(0, dot);
        if (!MessageDigest.isEqual(sign(encoded).getBytes(StandardCharsets.UTF_8),
                token.substring(dot + 1).getBytes(StandardCharsets.UTF_8))) {
            return null;
        }
        try {
            String payload = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
            int separator = payload.lastIndexOf('|');
            if (separator <= 0) {
                return null;
            }
            long expiresAt = Long.parseLong(payload.substring(separator + 1));
            if (expiresAt < System.currentTimeMillis()) {
                return null;
            }
            String username = payload.substring(0, separator);
            return username.isEmpty() ? null : username;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET, "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return "";
        }
    }
}
