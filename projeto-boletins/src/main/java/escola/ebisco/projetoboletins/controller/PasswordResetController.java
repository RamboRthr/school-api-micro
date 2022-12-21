package escola.ebisco.projetoboletins.controller;

import escola.ebisco.projetoboletins.Domain.email.EmailData;
import escola.ebisco.projetoboletins.Repo.UserRepository;
import escola.ebisco.projetoboletins.cryptography.DigitalSignature;
import escola.ebisco.projetoboletins.payload.request.ForgotPasswordRequest;
import escola.ebisco.projetoboletins.payload.request.ResetPasswordRequest;
import escola.ebisco.projetoboletins.payload.response.EmailSendResponse;
import escola.ebisco.projetoboletins.payload.response.MessageResponse;
import escola.ebisco.projetoboletins.security.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity sendEmailForPasswordReseting(@Valid @RequestBody ForgotPasswordRequest request) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {

        if (userRepository.existsByEmail(request.getEmail())) {

            String emailBase64 = encodeEmail(request.getEmail());

            DigitalSignature digitalSignature = new DigitalSignature();


            var privateKeyHash = (String) redisTemplate.opsForValue().get("privateKey");


            String resetKey = digitalSignature.createDigitalSignature(emailBase64, privateKeyHash);

            request.setResetKey(resetKey);

            try {
                emailService.sendEmail("school@eb-isco.com", EmailData.from(request));

            } catch (Exception exc) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(EmailSendResponse.builder()
                        .success(false)
                        .message(String.format("Failed to send email due to %s", exc.getMessage()))
                        .build());
            }

            return ResponseEntity.ok(new MessageResponse("We have sent you an e-mail to confirm this action."));
        }
        else {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }

    }
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResponseEntity resetPassword(@RequestParam("key") String signedKey, @RequestBody ResetPasswordRequest request) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        if (userRepository.existsByEmail(request.getEmail())){
            String emailBase64 = encodeEmail(request.getEmail());
            DigitalSignature digitalSignature = new DigitalSignature();
            String publicKeyHash = redisTemplate.opsForValue().get("publicKey").toString();

            if (digitalSignature.verifySignature(emailBase64, publicKeyHash,signedKey)){
                var updatedUser = userRepository.findByEmail(request.getEmail()).get();
                updatedUser.setPassword(encoder.encode(request.getNewPassword()));
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

    private String encodeEmail(String email){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        var digestedEmail = messageDigest.digest(email.getBytes());

        return Base64.getEncoder().encodeToString(digestedEmail);
    }
}
