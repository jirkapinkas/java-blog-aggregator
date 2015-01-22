package cz.jiripinkas.jba.service;

import java.security.Security;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

@Service
public class JceWorkaround {

	@PostConstruct
	public void init() {
		// solution for bug: error: javax.net.ssl.SSLException: java.lang.RuntimeException: Could not generate DH keypair
		Security.addProvider(new BouncyCastleProvider());
	}
	
}
