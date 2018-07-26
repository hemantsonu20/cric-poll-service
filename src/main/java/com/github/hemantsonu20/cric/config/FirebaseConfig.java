package com.github.hemantsonu20.cric.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		
		ClassPathResource resource = new ClassPathResource("cric-poll-firebase-adminsdk-s8v6x-03a2e9b4b5.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
		    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
		    .setDatabaseUrl("https://cric-poll.firebaseio.com")
		    .build();

		FirebaseApp.initializeApp(options);
	}
}
