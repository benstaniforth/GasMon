package org.softwire.training.gasmon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softwire.training.gasmon.aws.AwsClientFactory;
import org.softwire.training.gasmon.config.Config;
import org.softwire.training.gasmon.receiver.QueueSubscription;
import org.softwire.training.gasmon.receiver.Receiver;
import org.softwire.training.gasmon.repository.S3Repository;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            run();
        } catch (Throwable e) {
            LOG.error("Fatal error, terminating program", e);
            System.exit(1);
        }
    }

    private static void run() {
        LOG.info("Starting to run...");

        Config config = new Config();

        AwsClientFactory awsClientFactory = new AwsClientFactory();
        AmazonSQS sqs = awsClientFactory.sqs();
        AmazonSNS sns = awsClientFactory.sns();
        AmazonS3 s3 = awsClientFactory.s3();

        S3Repository repository = new S3Repository(s3, config.locations.s3Bucket);

        try (QueueSubscription queueSubscription = new QueueSubscription(sqs, sns, config.receiver.snsTopicArn)) {
            Receiver receiver = new Receiver(sqs, queueSubscription.getQueueUrl());

            // ...
            // Your code here!
            // ...
        }
    }
}
