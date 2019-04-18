package org.softwire.training.gasmon.services;

import org.softwire.training.gasmon.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsServices {

    public static boolean isDuplicateEvent(String eventId, List<Event> validEvents) {

        for (Event event : validEvents) {
            if (event.getEventId().equals(eventId)) {
                return true;
            }
        }

        return false;
    }


    public static boolean isValidTimeStamp(long currentTime, long timeStamp) {
        return (timeStamp >= (currentTime - 360_000)) && (timeStamp <= (currentTime - 300_000));
    }

    public static double averageEventValues(long currentTime, List<Event> validEvents) {
        double total = 0;
        int counter = 0;


        for (Event validEvent : validEvents) {
            if (isValidTimeStamp(currentTime, validEvent.getTimestamp())) {
                total += validEvent.getValue();
                counter++;
            }
        }
        return total / counter;
    }

    public static HashMap<String, List<Event>> eventsByLocation(List<Event> validEvents) {

        HashMap<String, List<Event>> eventsHash = new HashMap<>();

        for (Event event : validEvents) {
            eventsHash.computeIfAbsent(event.getLocationId(), k -> new ArrayList<>()).add(event);
        }
        return eventsHash;
    }

}
