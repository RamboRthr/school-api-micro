package com.service.resetpassword.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class KeysConfig {

    public KeysConfig() throws IOException {
    }

    @EventListener(ApplicationReadyEvent.class)
        private void generateKeys() throws NoSuchAlgorithmException, IOException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            String priv;
            try (FileWriter writer = new FileWriter("private.pem")) {
                priv = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                writer.write(priv);
            }
            String pub;
            try (FileWriter writer = new FileWriter("public.pem")) {
                pub = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                writer.write(pub);
            }
        }
    @Value("${app.privateKey}")
    private String privateKey;
    @Value("${app.publicKey}")
    private String publicKey;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    private void setKeyFromEnv() throws IOException {

        //privateKey = Files.readString(Paths.get("private.pem"));
        //publicKey = Files.readString(Paths.get("public.pem"));

        redisTemplate.opsForValue().set("privateKey", privateKey);
        redisTemplate.opsForValue().set("publicKey", publicKey);
    }
}
