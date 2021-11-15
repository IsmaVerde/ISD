package es.udc.ws.runfic.service.utils;

import java.time.LocalDateTime;

public class RunServiceDateTimeUtils {
    public static LocalDateTime roundToMinute(LocalDateTime input) {
        return LocalDateTime.of(input.getYear(), input.getMonth(), input.getDayOfMonth(), input.getHour(), input.getMinute());
    }
}
