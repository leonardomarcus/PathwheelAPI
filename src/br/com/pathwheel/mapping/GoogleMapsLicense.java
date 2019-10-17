package br.com.pathwheel.mapping;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GoogleMapsLicense {
	
	public static String getUrlWithDigitalSignature(String url, String clientId, String privateCryptoKey) throws NoSuchAlgorithmException, InvalidKeyException, MalformedURLException {
		//adicionando o client-id na URL
		url+="&client="+clientId;
		
		URL urlAuxiliar = new URL(url);
		String resource = urlAuxiliar.getPath()+"?"+urlAuxiliar.getQuery();
		
		// Convert the key from 'web safe' base 64 to binary
		privateCryptoKey = privateCryptoKey.replace('-', '+');
		privateCryptoKey = privateCryptoKey.replace('_', '/');

		// Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
		byte[] key = Base64.getDecoder().decode(privateCryptoKey);
		
		// Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		
		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		
		// compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());
		
		// base 64 encode the binary signature
		// Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
		String signature = Base64.getEncoder().encodeToString(sigBytes);
		
		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		
		return url + "&signature=" + signature;
	}

}
