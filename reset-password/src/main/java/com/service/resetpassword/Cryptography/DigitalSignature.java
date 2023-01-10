package com.service.resetpassword.Cryptography;

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

    public String createDigitalSignature(byte[] emailBytes, byte[] privateKeyByte) throws InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));

        signature.initSign(privateKey);
        signature.update(emailBytes);

        byte[] assinatura = signature.sign();

        return Base64.getEncoder().encodeToString(assinatura);
    }

    public boolean verifySignature(byte[] emailHash, byte[] publicKeyBytes, String signedKey) throws InvalidKeySpecException, InvalidKeyException, SignatureException {

        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        signature.initVerify(publicKey);
        signature.update(emailHash);

        if (signature.verify(Base64.getDecoder().decode(signedKey.getBytes()))){
            System.out.println("Ok");
            return true;
        }else {
            System.out.println("Not Ok");
            return false;
        }
    }
}
