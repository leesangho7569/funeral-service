package com.pcp.funeralsvc.web;

import com.pcp.funeralsvc.config.JasyptConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest(classes = JasyptConfig.class)
class JasyptTest {

    final BeanFactory beanFactory = new AnnotationConfigApplicationContext(JasyptConfig.class);
    final StringEncryptor stringEncryptor = beanFactory.getBean("jasyptStringEncryptor", StringEncryptor.class);

    @Test
    void testEncrypt() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword("petcare.123");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations(1000);
        config.setPoolSize(1);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");

        ((PooledPBEStringEncryptor) stringEncryptor).setConfig(config);
//		stringEncryptor.setConfig(config);

        String encDBUrl = stringEncryptor.encrypt("jdbc:postgresql://192.168.30.40:5433/funeralsvc");
        String encDBUrl2 = stringEncryptor.encrypt("jdbc:postgresql://192.168.30.40:5433/petcare_funeral");

        String encDBUsername = stringEncryptor.encrypt("petcare");
        String encDBPassword = stringEncryptor.encrypt("petcare.123");

        String encMQUrl = stringEncryptor.encrypt("192.168.30.40:9672");
        String encMQUsername = stringEncryptor.encrypt("funeral_service");
        String encMQPassword = stringEncryptor.encrypt("funeral_service.123");
        String encKey = stringEncryptor.encrypt("petcare.123");

        String encFuAgentUrl     = stringEncryptor.encrypt("http://localhost:9099");
        String encFuAgentUsername = stringEncryptor.encrypt("fu_agent");
        String encFuAgentPassword = stringEncryptor.encrypt("fu_agent.123");
        String encUserServiceUrl     = stringEncryptor.encrypt("http://192.168.30.40:9003");

        String decDBUrl = stringEncryptor.decrypt(encDBUrl);
        String decDBUrl2 = stringEncryptor.decrypt(encDBUrl2);
        String decDBUsername = stringEncryptor.decrypt(encDBUsername);
        String decDBPassword = stringEncryptor.decrypt(encDBPassword);

        String decMQUrl = stringEncryptor.decrypt(encMQUrl);
        String decMQUsername = stringEncryptor.decrypt(encMQUsername);
        String decMQPassword = stringEncryptor.decrypt(encMQPassword);
        String decKey = stringEncryptor.decrypt(encKey);

        String decFuAgentUrl = stringEncryptor.decrypt(encFuAgentUrl);
        String decFuAgentUsername = stringEncryptor.decrypt(encFuAgentUsername);
        String decFuAgentPassword = stringEncryptor.decrypt(encFuAgentPassword);

        String decUserServiceUrl = stringEncryptor.decrypt(encUserServiceUrl);

        System.out.printf("Encrypted DB URL: %s%n" +
                        "Encrypted DB2 URL: %s%n" +
                        "Encrypted DB Username: %s%n" +
                        "Encrypted DB Password: %s%n" +
                        "Encrypted MQ URL: %s%n" +
                        "Encrypted MQ Username: %s%n" +
                        "Encrypted MQ Password: %s%n" +
                        "Encrypted Key: %s%n" +
                        "Encrypted FU-AGENT URL: %s%n" +
                        "Encrypted FU-AGENT Username: %s%n" +
                        "Encrypted FU-AGENT Password: %s%n" +
                        "Encrypted User-Service URL: %s%n" +
                        "Decrypted DB URL: %s%n" +
                        "Decrypted DB2 URL: %s%n" +
                        "Decrypted DB Username: %s%n" +
                        "Decrypted DB Password: %s%n" +
                        "Decrypted MQ URL: %s%n" +
                        "Decrypted MQ Username: %s%n" +
                        "Decrypted MQ Password: %s%n" +
                        "Decrypted Key: %s%n" +
                        "Decrypted FU-AGENT URL: %s%n" +
                        "Decrypted FU-AGENT Username: %s%n" +
                        "Decrypted FU-AGENT Password: %s%n" +
                        "Decrypted User-Service URL: %s%n",

                encDBUrl, encDBUrl2, encDBUsername, encDBPassword,
                encMQUrl, encMQUsername, encMQPassword, encKey,
                encFuAgentUrl, encFuAgentUsername, encFuAgentPassword,
                encUserServiceUrl,
                decDBUrl, decDBUrl2, decDBUsername, decDBPassword,
                decMQUrl, decMQUsername, decMQPassword, decKey,
                decFuAgentUrl, decFuAgentUsername, decFuAgentPassword,
                decUserServiceUrl);
    }

}
