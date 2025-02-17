package com.rjgf.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtTotenUtilTest {



    @Test
    public void encodeJWT() {
        String token = JwtTotenUtil.encodeJWT("abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc");
        System.out.println(token);
    }

    @Test
    public void decodeJWT() {
        JwtTotenUtil.decodeJWT(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5Vqd3TnlJ_miJDnmoR0b2tlbiIsInJvbGUiOiJhZG1pbiIsImV4cCI6MTczOTQ5NzQ0MTc3NSwiaWF0IjoxNzM5NDkzODQxNzc1LCJ1c2VybmFtZSI6ImN5cyJ9.DKjGZBSHOhOXaGwcFUhs5QBbRVNoFNLtmj-yKZDPtag","abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc");
    }
}