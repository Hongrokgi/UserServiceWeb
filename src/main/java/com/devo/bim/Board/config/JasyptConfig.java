package com.devo.bim.Board.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        String key = "the_more_boundless_your_vision_the_more_real_you_are";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);                    //  암호화할 때 사용하는 키
        config.setAlgorithm("PBEWithMD5AndDES");    //  암호화 알고리즘
        config.setKeyObtentionIterations("1000");   //  반복할 해싱 횟수
        config.setPoolSize("1");                    //  인스턴스 pool
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setStringOutputType("base64");       // 인코딩 방식
        encryptor.setConfig(config);
        return encryptor;
    }
}
