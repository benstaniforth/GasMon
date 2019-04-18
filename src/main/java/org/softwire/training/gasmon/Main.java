package org.softwire.training.gasmon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softwire.training.gasmon.aws.AwsClientFactory;
import org.softwire.training.gasmon.config.Config;
import org.softwire.training.gasmon.model.Event;
import org.softwire.training.gasmon.model.LocationData;
import org.softwire.training.gasmon.receiver.QueueSubscription;
import org.softwire.training.gasmon.receiver.Receiver;
import org.softwire.training.gasmon.repository.S3Repository;
import org.softwire.training.gasmon.services.EventsServices;
import org.softwire.training.gasmon.services.LocationsService;
import org.softwire.training.gasmon.services.Utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    private static void run() throws IOException {
        LOG.info("Starting to run...");

        Config config = new Config();

        AwsClientFactory awsClientFactory = new AwsClientFactory();
        AmazonSQS sqs = awsClientFactory.sqs();
        AmazonSNS sns = awsClientFactory.sns();
        AmazonS3 s3 = awsClientFactory.s3();

        S3Repository repository = new S3Repository(s3, config.locations.s3Bucket);

        LocationsService locationsService = new LocationsService(repository, config.locations.s3Key);

        List<LocationData> locationData = locationsService.getValidLocations();

        // Create a file to output the averages to:
        File file = new File("AverageLog.txt");
        file.createNewFile();

        FileWriter writer = new FileWriter(file);
        writer.write("");
        writer.close();

        try (QueueSubscription queueSubscription = new QueueSubscription(sqs, sns, config.receiver.snsTopicArn)) {
            Receiver receiver = new Receiver(sqs, queueSubscription.getQueueUrl());
            LOG.info("{}", locationData);

            List<Event> validEvents = new ArrayList<>();
            long timeSinceAverageLastCalculated = System.currentTimeMillis();
            double averageValue = 0.0;
            LocalTime curTime = LocalTime.now();

            while (curTime.plusMinutes(45).isAfter(LocalTime.now())) {

                long currentTime = System.currentTimeMillis();
                List<Event> events = receiver.getEvents();
                for (Event event : events) {
                    if (locationsService.isValidLocation(event.getLocationId())) {
                        if (!EventsServices.isDuplicateEvent(event.getEventId(), validEvents)) {
                            validEvents.add(event);
                            LOG.info("Added : {}", event);
                        } else {
                            LOG.info("Skipped duplicate event : {}", event);
                        }
                    } else {
                        LOG.info("Skipped event. Unknown location");
                    }
                }

                if ((timeSinceAverageLastCalculated + 60_000) < System.currentTimeMillis()) {
                    averageValue = EventsServices.averageEventValues(currentTime, validEvents);
                    LOG.info("Current Time: " + LocalTime.now() + ", Average for 5-6 minutes before is: " + averageValue);
                    timeSinceAverageLastCalculated = System.currentTimeMillis();
                    Utilities.writeToFile(String.valueOf(averageValue), file);
                }
            }

        }
    }
}
