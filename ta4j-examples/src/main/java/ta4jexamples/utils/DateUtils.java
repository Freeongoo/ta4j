package ta4jexamples.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static ta4jexamples.utils.ZoneOffsetUtils.DEFAULT_ZONE_OFFSET;

/**
 *
 */
public class DateUtils {

    public static final String ISO = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DELIMITER = ".";

    // ===============
    // calculate count
    public static Long getCountSecsBetween(Date dateFrom, Date dateTo) {
        return getCountDateUnitsBetween(dateFrom, dateTo, DateUtils::asLocalDateTime, ChronoUnit.SECONDS);
    }

    public static Long getCountSecsBetweenWithoutSign(Date dateFrom, Date dateTo) {
        return Math.abs(getCountSecsBetween(dateFrom, dateTo));
    }

    public static Long getCountSecsBetweenWithoutSign(OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        return getCountSecsBetweenWithoutSign(asDate(dateFrom), asDate(dateTo));
    }

    public static Long getCountDateUnitsBetween(Date dateFrom, Date dateTo, Function<Date, Temporal> temporalConverter, ChronoUnit chronoUnit) {
        Objects.requireNonNull(temporalConverter, "temporalConverter cannot be null");
        Objects.requireNonNull(chronoUnit, "chronoUnit cannot be null");

        if (dateFrom == null || dateTo == null) {
            return null;
        }


        Temporal localFrom = temporalConverter.apply(dateFrom);
        Temporal localTo = temporalConverter.apply(dateTo);

        if (Objects.equals(localFrom, localTo)) {
            return 0L;
        }

        return chronoUnit.between(localFrom, localTo);
    }

    public static LocalTime asLocalTime(Date date) {
        return asLocalDateTime(date).toLocalTime();
    }

    public static LocalDate asLocalDate(Date date) {
        return asLocalDateTime(date).toLocalDate();
    }

    public static LocalDate asLocalDate(ZonedDateTime date) {
        return asLocalDateTime(date).toLocalDate();
    }

    // ===============
    // convert to Date
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_OFFSET).toInstant());
    }

    public static Date asDate(OffsetDateTime offsetDateTime) {
        long epochMilli = offsetDateTime.toInstant().toEpochMilli();
        return new Date(epochMilli);
    }

    public static Date asDate(ZonedDateTime zonedDateTime) {
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return asDate(localDateTime);
    }

    // ===============
    // convert to LocalDateTime
    public static LocalDateTime asLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(OffsetDateTime offset) {
        return offset.toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(DEFAULT_ZONE_OFFSET).toLocalDateTime();
    }

    // ===============
    // convert to ZonedDateTime
    public static ZonedDateTime asZonedDateTime(Date date) {
        return asOffsetDateTime(date).toZonedDateTime();
    }

    public static ZonedDateTime asZonedDateTime(OffsetDateTime offset) {
        return offset.toZonedDateTime();
    }

    public static ZonedDateTime asZonedDateTime(LocalDateTime localDateTime) {
        OffsetDateTime offset = asOffsetDateTime(localDateTime);
        return offset.toZonedDateTime();
    }

    // ===============
    // convert to OffsetDateTime
    public static OffsetDateTime asOffsetDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toOffsetDateTime();
    }

    public static OffsetDateTime asOffsetDateTime(LocalDateTime localDateTime) {
        return OffsetDateTime.of(LocalDateTime.from(localDateTime), DEFAULT_ZONE_OFFSET);
    }

    public static OffsetDateTime asOffsetDateTime(Date date) {
        return asOffsetDateTime(asLocalDateTime(date));
    }

    public static String toStringBySeconds(long seconds, String format) {
        long mSec = seconds * 1000L;
        return toString(new Date(mSec), format);
    }

    public static String toString(Date date, String format) {
        if (date == null) {
            return null;
        }

        requireNonNull(format, "Cannot be null date format");

        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String toStringISO(Date date) {
        return toString(date, ISO);
    }

    public static String toStringISO(OffsetDateTime date) {
        return toStringISO(asDate(date));
    }

    public static String getYearMonth(OffsetDateTime offsetDateTime) {
        int year = offsetDateTime.getYear();
        Month month = offsetDateTime.getMonth();
        return year + DELIMITER + month.toString();
    }

    public static String getYearMonthDay(OffsetDateTime offsetDateTime) {
        String yearMonth = getYearMonth(offsetDateTime);
        return yearMonth + DELIMITER + offsetDateTime.getDayOfMonth();
    }

    public static Date parse(String dateStr, String format) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(ZoneOffsetUtils.DEFAULT_TIME_ZONE);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseISO(String dateStr) {
        return parse(dateStr, ISO);
    }

    public static OffsetDateTime parseISOGetOffset(String dateStr) {
        return asOffsetDateTime(parseISO(dateStr));
    }

    public static LocalDateTime parseToLocalDateTime(String dateStr, String format) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        Date date = parse(dateStr, format);
        return asLocalDateTime(date);
    }

    public static LocalDateTime parseToLocalDateTimeISO(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        Date date = parse(dateStr, ISO);
        return asLocalDateTime(date);
    }

    public static LocalDate parseToLocalDateISO(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        Date date = parse(dateStr, ISO);
        return asLocalDate(date);
    }

    public static ZonedDateTime parseToZoneDateTimeISO(String dateStr) {
        return asZonedDateTime(parseToLocalDateTimeISO(dateStr));
    }

    public static boolean isEquals(Date date1, Date date2) {
        return asLocalDateTime(date1).equals(asLocalDateTime(date2));
    }

    public static String getCurrentDateInISO() {
        return toString(new Date(), ISO);
    }

    public static boolean isSunDay(LocalDate localDate) {
        DayOfWeek d = localDate.getDayOfWeek();
        return d == DayOfWeek.SUNDAY;
    }

    public static boolean isSaturday(LocalDate localDate) {
        DayOfWeek d = localDate.getDayOfWeek();
        return d == DayOfWeek.SATURDAY;
    }
}
