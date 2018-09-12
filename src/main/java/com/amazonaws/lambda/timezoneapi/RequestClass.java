package com.amazonaws.lambda.timezoneapi;

public class RequestClass {
	
   Double latitude;
   Double longtitude;
   Long timestamp;
   
   public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

   public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public RequestClass(Double latitude, Double longtitude, Long timestamp) {
       this.latitude = latitude;
       this.longtitude = longtitude;
       this.timestamp = timestamp;
   }

   public RequestClass() {
   }
}
