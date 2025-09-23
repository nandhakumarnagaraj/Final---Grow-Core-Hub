package com.growcorehub.version1.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGeneratorRFC7518 {
	public static void main(String[] args) {
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // generates secure 512-bit key
		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("Generated RFC 7518 compliant HS512 key (base64):");
		System.out.println(base64Key);
	}
}
