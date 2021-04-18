package com.doogod.video.meetingapi.aws.chime;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.chime.AmazonChimeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChimeClient {

    @Value("${spring.aws.access_key}")
    private String accessKey;

    @Value("${spring.aws.secret_key}")
    private String secretKey;

    @Bean
    public AmazonChimeClient amazonChimeClient() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        return (AmazonChimeClient) AmazonChimeClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
