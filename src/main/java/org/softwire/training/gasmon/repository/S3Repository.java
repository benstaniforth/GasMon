package org.softwire.training.gasmon.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public class S3Repository {

    private static final Logger LOG = LoggerFactory.getLogger(S3Repository.class);

    private final AmazonS3 s3;
    private final String s3Bucket;

    public S3Repository(AmazonS3 s3, String s3Bucket) {
        this.s3 = s3;
        this.s3Bucket = s3Bucket;
    }

    public String getObjectAtKey(String key) throws IOException {
        return CharStreams.toString(new InputStreamReader(s3.getObject(s3Bucket, key).getObjectContent()));
    }
}
