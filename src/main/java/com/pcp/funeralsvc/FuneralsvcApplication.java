package com.pcp.funeralsvc;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class FuneralsvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuneralsvcApplication.class, args);
	}

}
