package eu.trustdemocracy.votes.core.interactors.util;

import eu.trustdemocracy.votes.infrastructure.JWTKeyFactory;
import java.util.UUID;
import lombok.val;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

public final class TokenUtils {

  public static void generateKeys() throws JoseException {
    val rsaKey = RsaJwkGenerator.generateJwk(2048);
    JWTKeyFactory.setPrivateKey(rsaKey.getPrivateKey());
    JWTKeyFactory.setPublicKey(rsaKey.getPublicKey());
  }

  public static String createToken(UUID id, String username) {
    val claims = new JwtClaims();
    claims.setExpirationTimeMinutesInTheFuture(10);
    claims.setGeneratedJwtId();
    claims.setIssuedAtToNow();
    claims.setNotBeforeMinutesInThePast(2);

    claims.setSubject(id.toString());
    claims.setClaim("username", username);

    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(claims.toJson());
    jws.setKey(JWTKeyFactory.getPrivateKey());
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

    try {
      return jws.getCompactSerialization();
    } catch (JoseException e) {
      throw new RuntimeException(e);
    }
  }

}
