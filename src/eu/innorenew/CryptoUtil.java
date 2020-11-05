package eu.innorenew;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoUtil {
    public static PublicKey pub;
    public static PrivateKey pvt;

    public static void generatePubPvtKeyPair() {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyPairGenerator g = null;
        try {
            g = KeyPairGenerator.getInstance("EC");
            g.initialize(ecSpec, new SecureRandom());
            KeyPair keypair = g.generateKeyPair();
            pub = keypair.getPublic();
            pvt = keypair.getPrivate();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }


    public static boolean verfyMessage(Message message) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            KeyFactory kf = KeyFactory.getInstance("EC");

            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(message.getPub_key()));

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(message.getBody().getBytes("UTF-8"));
            return ecdsaVerify.verify(Base64.getDecoder().decode(message.getSignature()));

        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (SignatureException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeySpecException e) {
        }
        return false;

    }

    public static Message signMessage(Message message) {
        Signature ecdsaSign = null;
        try {
            ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initSign(pvt);
            ecdsaSign.update(message.getBody().getBytes("UTF-8"));
            byte[] signature = ecdsaSign.sign();
            String pub_key = Base64.getEncoder().encodeToString(pub.getEncoded());
            String sig = Base64.getEncoder().encodeToString(signature);
            message.setPub_key(pub_key);
            message.setSignature(sig);
            return message;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}









