package com.titxu.cloud.common.core.util.jose;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
public final class KeyGeneratorUtils {

    private KeyGeneratorUtils() {
    }

    static SecretKey generateSecretKey() {
        SecretKey hmacKey;
        try {
            hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return hmacKey;
    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    public static KeyPair loadRsaKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(KeyGeneratorUtils.class.getResourceAsStream("/titxu.jks"), "lxue0422".toCharArray());
        Key key = keyStore.getKey("titxu", "lxue0422".toCharArray());
        if (key instanceof PrivateKey) {
            // Get certificate of public key
            Certificate certificate = keyStore.getCertificate("titxu");
            // Get public key
            PublicKey publicKey = certificate.getPublicKey();
            // Return a key pair
            return new KeyPair(publicKey, (PrivateKey) key);
        }
        return new KeyPair(null, null);
    }

    static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    static KeyPair generateEcKey() {
        EllipticCurve ellipticCurve = new EllipticCurve(
                new ECFieldFp(
                        new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
                new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
                new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"));
        ECPoint ecPoint = new ECPoint(
                new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
                new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
        ECParameterSpec ecParameterSpec = new ECParameterSpec(
                ellipticCurve,
                ecPoint,
                new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
                1);

        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(ecParameterSpec);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}