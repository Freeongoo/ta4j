package ta4jexamples.utils;

import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class ZoneOffsetUtils {

    public static final String TIME_ZONE = "UTC";

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE);

    public static final ZoneOffset DEFAULT_ZONE_OFFSET;

    static {
        int rawOffset = DEFAULT_TIME_ZONE.getRawOffset();
        long offsetInHours = TimeUnit.HOURS.convert(rawOffset, TimeUnit.MILLISECONDS);

        DEFAULT_ZONE_OFFSET = ZoneOffset.ofHours((int) offsetInHours);
    }

    private ZoneOffsetUtils() {
    }
}
