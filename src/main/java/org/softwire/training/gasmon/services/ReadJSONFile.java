package org.softwire.training.gasmon.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.softwire.training.gasmon.model.LocationData;
import org.softwire.training.gasmon.config.Config;
import org.softwire.training.gasmon.repository.S3Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReadJSONFile {

    public static List<LocationData> getLocationDataFromJSON(S3Repository repository, Config config) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        LocationData[] locationData = gson.fromJson(repository.getObjectAtKey(config.locations.s3Key), LocationData[].class);
        return Arrays.asList(locationData);

    }
}
