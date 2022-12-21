package escola.ebisco.projetoboletins.cryptography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class DigitalSignature {




    Signature signature = Signature.getInstance("SHA256withRSA");

    KeyFactory kf = KeyFactory.getInstance("RSA");

    public DigitalSignature() throws NoSuchAlgorithmException {
    }

    public String createDigitalSignature(String emailHash, String hashKey) throws  InvalidKeyException, SignatureException, InvalidKeySpecException {

        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(hashKey.getBytes()));

        signature.initSign(privateKey);
        signature.update(emailHash.getBytes());

        byte[] assinatura = signature.sign();

        System.out.println(Base64.getEncoder().encodeToString(assinatura));

        return Base64.getEncoder().encodeToString(assinatura);
    }

    public boolean verifySignature(String emailHash, String hashKey, String signedKey) throws IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        var publicKeyBytes = hashKey.getBytes();

        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        signature.initVerify(publicKey);
        signature.update(emailHash.getBytes());

        if (signature.verify(Base64.getDecoder().decode(signedKey))){
            System.out.println("Ok");
            return true;
        }else {
            System.out.println("Not Ok");
            return false;
        }
    }
}
