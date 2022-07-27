/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mycompany.taskmanager.util;

import hu.mycompany.taskmanager.service.TaskService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerdan Tibor
 */
public class TimeUtil {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String OFFSET_DATE_TIME_FORMAT = "yyyy/MM/dd hh:mm O";
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public static LocalDate parseLocalDate(String dateString) {
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            localDate = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException ex) {
            logger.warn(ex.getMessage());
        }
        return localDate;
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        String localDateTimeFormat = "";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            localDateTimeFormat = localDateTime.format(formatter);
        } catch (DateTimeParseException ex) {
            logger.warn(ex.getMessage());
        }
        return localDateTimeFormat;
    }

    public static String formatOffsetDateTime(OffsetDateTime offsetDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OFFSET_DATE_TIME_FORMAT);
        return offsetDateTime.format(dateTimeFormatter);
    }

    public static Duration getDuration(OffsetDateTime start, OffsetDateTime end) {
        return Duration.between(start, end);
    }

    public static String formatDuration(Duration duration) {
        long hour = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.toSeconds() % 60;
        return String.format("%2dh %2dm %2ds", hour, minutes, seconds);
    }

}
