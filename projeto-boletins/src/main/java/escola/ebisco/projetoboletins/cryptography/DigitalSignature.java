package escola.ebisco.projetoboletins.cryptography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;


public class DigitalSignature {




    Signature signature = Signature.getInstance("SHA256withRSA");

    KeyFactory kf = KeyFactory.getInstance("RSA");

    public DigitalSignature() throws NoSuchAlgorithmException {
    }

    public String createDigitalSignature(String emailHash, byte[] privateKeyBytes) throws InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

        System.out.println(Arrays.toString(Files.readAllBytes(Paths.get(".public"))));
        System.out.println(Arrays.toString(privateKeyBytes));

        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        signature.initSign(privateKey);
        signature.update(emailHash.getBytes());

        byte[] assinatura = signature.sign();

        System.out.println(Base64.getEncoder().encodeToString(assinatura));

        return Base64.getEncoder().encodeToString(assinatura);
    }

    public boolean verifySignature(String emailHash, byte[] publicKeyBytes, String signedKey) throws InvalidKeySpecException, InvalidKeyException, SignatureException {

        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        signature.initVerify(publicKey);
        signature.update(emailHash.getBytes());

        System.out.println(signedKey.getBytes(StandardCharsets.ISO_8859_1).length);
        if (signature.verify(signedKey.getBytes(StandardCharsets.ISO_8859_1))){
            System.out.println("Ok");
            return true;
        }else {
            System.out.println("Not Ok");
            return false;
        }
    }
}
