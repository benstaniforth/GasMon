package org.softwire.training.gasmon.config;

import com.typesafe.config.ConfigFactory;

public class Config {

    public final LocationsConfig locations;
    public final ReceiverConfig receiver;

    public Config() {
        com.typesafe.config.Config config = ConfigFactory.load();
        locations = LocationsConfig.fromRawConfig(config.getConfig("locations"));
        receiver = ReceiverConfig.fromRawConfig(config.getConfig("receiver"));
    }

    public static class LocationsConfig {

        public final String s3Bucket;
        public final String s3Key;

        private LocationsConfig(String s3Bucket, String s3Key) {
            this.s3Bucket = s3Bucket;
            this.s3Key = s3Key;
        }

        private static LocationsConfig fromRawConfig(com.typesafe.config.Config config) {
            return new LocationsConfig(config.getString("s3Bucket"), config.getString("s3Key"));
        }
    }

    public static class ReceiverConfig {

        public final String snsTopicArn;

        private ReceiverConfig(String snsTopicArn) {
            this.snsTopicArn = snsTopicArn;
        }

        private static ReceiverConfig fromRawConfig(com.typesafe.config.Config config) {
            return new ReceiverConfig(config.getString("snsTopicArn"));
        }
    }
}
