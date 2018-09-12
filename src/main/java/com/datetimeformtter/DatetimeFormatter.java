package com.datetimeformtter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DatetimeFormatter {
	
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
 
	private final String datetime;
	
	public DatetimeFormatter(String datetime){
		this.datetime = datetime;
	}
	
	public Long datetimeToEpochtime() {
		LocalDateTime localDateTime = LocalDateTime.parse(datetime, dateTimeFormatter);
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()/1000;
	}

	public String localiseDatetime(String timeZoneId) {
		ZonedDateTime utc = ZonedDateTime.parse(datetime, inputFormatter).withZoneSameInstant(ZoneOffset.UTC);
		TimeZone tz = TimeZone.getTimeZone(timeZoneId);
		return utc.withZoneSameInstant(tz.toZoneId()).format(outputFormatter);
	}

}
