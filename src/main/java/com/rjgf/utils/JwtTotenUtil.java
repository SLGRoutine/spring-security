package com.rjgf.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class JwtTotenUtil {
    //密钥  用于签名和验证JWT
    private  String key = "abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc";

    // 过期时间，单位为毫秒，这里设置为24小时
    public static final  long EXPIRE = 1000*60*60*24;  // 1000毫秒=1秒  60秒=1分钟

    /**
     * 生成token
     * @param payloadMap
     * @return
     */
    public  String encodeJWT(Map payloadMap){
        //1.定义header部分
        Map headerMap = new HashMap();
        headerMap.put("alg",SignatureAlgorithm.HS256.getValue());
        headerMap.put("typ","JWT");


        Date eDate = new Date(System.currentTimeMillis()+EXPIRE);
        payloadMap.put("exp",eDate);  //将过期时间加入负载

        //3.生成token
        String token = Jwts.builder()
               .setHeaderParams(headerMap)
                .setSubject("user") //设置主题
                .setIssuedAt(new Date())   //token签发时间
                .setExpiration(eDate)   //token过期时间
               .setClaims(payloadMap)   //设置负载
               .signWith(SignatureAlgorithm.HS256, key) //设置签名算法和密钥
               .compact();      //拼接header和payload   生成JWT字符串
        return token;
    }

    /**
     * 解析token
     * @param jwtToken JWT字符串
     * @return 解析后的Claims对象
     */
    public Claims decodeJWTWithKey(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)  // 设置签名密钥
                    .parseClaimsJws(jwtToken)  // 解析JWT
                    .getBody();  // 获取负载部分
        } catch (ExpiredJwtException e) {
            log.error("Token已经过期", e);
            return e.getClaims();  // 返回过期的Claims
        } catch (Exception e) {
            log.error("Token解析失败", e);
            return null;  // 返回null表示解析失败
        }
    }


    /**
     * 验证token
     * @param token
     * @param username
     * @return
     */
    public boolean validateToken(String token,String username){
        return username.equals(decodeJWTWithKey(token).getSubject())&&!isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        if (token==null||token.isEmpty()){
            log.warn("jwttoken is null or empty");
            return true;
        }
        try{
            //                                   claims  载荷中的字段token的过期时间
            Date time =decodeJWTWithKey(token).getExpiration();
            return time.before(new Date());
        }catch (Exception e){
            log.warn(e.getMessage());
            return  true;
        }
    }

    /**
     * ======================================
     *  以下方法为测试使用，生产环境中请使用上面的方法
     *  ======================================
     */

//    其中key为盐，jsonwebtoken的jar包规定，key必须字节数要大于等于你所用的加密算法的最小字节数
//    这里使用HS256，所以key的字节数要大于等于32字节，这里使用256位的key
    public static  String encodeJWT(String key) {
        //1.定义header部分
        Map headerMap = new HashMap();
        headerMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headerMap.put("typ", "JWT");

        //2.定义payload部分   这里存放的是payload部分，也就是token的主体部，包含了token的主体信息
        Map payloadMap = new HashMap();
        payloadMap.put("sub", "测试jwt生成的token");
        payloadMap.put("iat", System.currentTimeMillis());
        payloadMap.put("exp", System.currentTimeMillis() + 1000 * 60*60);  // 过期时间
        payloadMap.put("username", "cys");
        payloadMap.put("role", "admin");

        //3.生成token
        String token = Jwts.builder()
               .setHeaderParams(headerMap)
               .setClaims(payloadMap)
               .signWith(SignatureAlgorithm.HS256, key)
               .compact();
        return token;
    }

    /**
     * 解析token  获取token主体中的信息
     */
    public static void   decodeJWT(String jwtToken, String key) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            Object sub = claims.get("sub");
            Object iat = claims.get("iat");
            Object exp = claims.get("exp");
            Object name = claims.get("username");
            Object role = claims.get("role");


            System.out.println("sub:"+sub+"\nname:"+name.toString()+"\nrole:"+role+"\niat:"+iat+"\nexp:"+exp+"\n是否失效:"
            +(System.currentTimeMillis() > Long.parseLong(exp.toString())));

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("error:token已经过期");
        }

    }


}
