package org.softwire.training.gasmon.services;

import com.google.gson.Gson;
import org.softwire.training.gasmon.model.LocationData;
import org.softwire.training.gasmon.repository.S3Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocationsService {

    private List<LocationData> validLocations;

    private final S3Repository s3Repository;
    private final String locationsFileName;
    private final Gson gson = new Gson();

    public LocationsService(S3Repository s3Repository, String locationsFileName) {
        this.s3Repository = s3Repository;
        this.locationsFileName = locationsFileName;
    }

    public List<LocationData> getValidLocations () throws IOException {
        if (validLocations == null) {
            String locationsJson = s3Repository.getObjectAtKey(locationsFileName);
            validLocations = Arrays.asList(gson.fromJson(locationsJson, LocationData[].class));
        }
        return validLocations;
    }

    public boolean isValidLocation (String locationId) throws IOException {
        getValidLocations();
        for (LocationData location : validLocations) {
            if (location.getId().equals(locationId)) {
                return true;
            }
        }
        return false;
    }

}
