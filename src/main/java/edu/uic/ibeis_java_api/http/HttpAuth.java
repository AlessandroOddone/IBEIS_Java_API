package edu.uic.ibeis_java_api.http;

import android.org.apache.commons.codec.binary.Base64;
import edu.uic.ibeis_java_api.exceptions.AuthorizationHeaderException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HttpAuth {

    private static final String TOKEN_NAME = "IBEIS";
    private static final String TOKEN_SECRET = "CB73808F-A6F6-094B-5FCD-385EBAFF8FC0";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Generates the secret key signature
     * @param key
     * @param messageToSendBytes
     * @return Secret key signature
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static String getSignature(String key, byte[] messageToSendBytes) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keyHmac = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(keyHmac);
        byte[] keyBytes = mac.doFinal(messageToSendBytes);
        return new String(Base64.encodeBase64(keyBytes));
    }

    /**
     * Generates the authorization header for http requests
     * @param url
     * @return Authorization header
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String getAuthorizationHeader(URL url) throws AuthorizationHeaderException {
        try {
            byte[] messageToSendBytes = url.toString().getBytes();
            String secretKeySignature = getSignature(TOKEN_SECRET, messageToSendBytes);
            return TOKEN_NAME + ":" + secretKeySignature;

        } catch (Exception e) {
            if (e instanceof NoSuchAlgorithmException) {
                throw new AuthorizationHeaderException("no such algorithm");
            }
            else if (e instanceof InvalidKeyException) {
                throw new AuthorizationHeaderException("invalid key");
            }
            throw new AuthorizationHeaderException();
        }
    }
}
