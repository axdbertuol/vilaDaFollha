package com.viladafolha.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AwsMailConfig {
    private final String secretKey;
    private final String accessKey;

    public AwsMailConfig(
            @Value("${aws.accesskey.secret}") String secretKey,
            @Value("${aws.accesskey}") String accessKey
    ) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        accessKey,
                                        secretKey)))
                .withRegion(Regions.SA_EAST_1)
                .build();
    }

    @Bean
    public JavaMailSender javaMailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
    }
}
