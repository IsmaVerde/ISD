package es.udc.ws.runfic.model.utils;

import java.time.LocalDateTime;

public class RunFicDateTimeUtils {
    public static LocalDateTime roundToMinute(LocalDateTime input) {
        return LocalDateTime.of(input.getYear(), input.getMonth(), input.getDayOfMonth(), input.getHour(), input.getMinute());
    }
}