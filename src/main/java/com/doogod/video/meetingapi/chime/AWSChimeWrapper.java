package com.doogod.video.meetingapi.chime;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.AmazonChimeClient;
import com.amazonaws.services.chime.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AWSChimeWrapper {

    @Value("${spring.aws.access_key}")
    private String accessKey;
    @Value("${spring.aws.secret_key}")
    private String secretKey;

    @Bean
    public SimpleJoin simpleJoin() {
        return new SimpleJoin(accessKey, secretKey);
    }
}
