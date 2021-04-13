package com.doogod.video.meetingapi.aws.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.doogod.video.meetingapi.db.models.Resident;
import org.checkerframework.checker.units.qual.A;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SMSSendingService {

    @Value("${spring.aws.access_key}")
    private String accessKey;

    @Value("${spring.aws.secret_key}")
    private String secretKey;

    @Autowired
    Jdbi jdbi;

    Logger logger = LoggerFactory.getLogger(SMSSendingService.class);

    public void sendTextMessage(String phoneNumber, String message) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        AmazonSNS sns = AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();


        Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("DoGood").withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));

        PublishRequest request = new PublishRequest();
        request.withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes);

        PublishResult result = sns.publish(request);

        logger.info("Sent sms message [" + message + "] to [" + phoneNumber + "]");
    }
}
