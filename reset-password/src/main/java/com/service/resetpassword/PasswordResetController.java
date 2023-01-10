package com.service.resetpassword;

import Classes.ForgotPasswordRequest;
import com.service.resetpassword.Cryptography.DigitalSignature;
import com.service.resetpassword.Data.UserRepository;
import com.service.resetpassword.Requests.ResetPasswordRequest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/forgotMyPassword")
public class PasswordResetController {
    @Autowired
    UserRepository userRepository;

    //@Autowired
    //EmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;


    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity sendEmailForPasswordReseting(@Valid @RequestBody ForgotPasswordRequest request) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {

        if (userRepository.existsByEmail(request.getEmail())) {

            var emailBytes = getBytesFromEmail(request.getEmail());

            DigitalSignature digitalSignature = new DigitalSignature();

            var privateKeyBase64 = Base64.getDecoder().decode(redisTemplate.opsForValue().get("privateKey").toString());

            String resetKey = digitalSignature.createDigitalSignature(emailBytes, privateKeyBase64);

            request.setResetKey(resetKey);

            send(request);

            return ResponseEntity.ok("We have sent you an authentication e-mail.");
        }
        else {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest request) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        if (userRepository.existsByEmail(request.getEmail())){
            var emailBytes = getBytesFromEmail(request.getEmail());

            DigitalSignature digitalSignature = new DigitalSignature();

            var publicKeyBytes = Base64.getDecoder().decode(redisTemplate.opsForValue().get("publicKey").toString());

            if (digitalSignature.verifySignature(emailBytes, publicKeyBytes,request.getSignedKey())){
                var updatedUser = userRepository.findByEmail(request.getEmail()).get();
                updatedUser.setPassword(encoder.encode(request.getNewPassword()));
                updatedUser.setLoginAttempts(0);
                userRepository.save(updatedUser);
                return new ResponseEntity("Password updated", OK);

            }
            else{
                return new ResponseEntity("Key does not match.", FORBIDDEN);
            }
        }
        else {
            return new ResponseEntity("User not found", FORBIDDEN);
        }
    }

    private byte[] getBytesFromEmail(String email){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest(email.getBytes());
    }

    public void send(ForgotPasswordRequest request) {
        rabbitTemplate.convertAndSend(this.queue.getName(), request);
    }

}
