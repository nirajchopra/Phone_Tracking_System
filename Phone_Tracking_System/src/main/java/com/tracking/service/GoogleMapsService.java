package com.tracking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleMapsService {

	@Value("${google.maps.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();

	public GoogleMapsResponse getLocationDetails(Double latitude, Double longitude) {
		try {
			String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
					latitude, longitude, apiKey);

			GoogleMapsApiResponse response = restTemplate.getForObject(url, GoogleMapsApiResponse.class);

			if (response != null && !response.getResults().isEmpty()) {
				GoogleMapsApiResponse.Result result = response.getResults().get(0);
				return new GoogleMapsResponse(result.getFormattedAddress(), result.getPlaceId());
			}
		} catch (Exception e) {
			System.err.println("Error calling Google Maps API: " + e.getMessage());
		}

		return null;
	}

	public static class GoogleMapsResponse {
		private String formattedAddress;
		private String placeId;

		public GoogleMapsResponse(String formattedAddress, String placeId) {
			this.formattedAddress = formattedAddress;
			this.placeId = placeId;
		}

		public String getFormattedAddress() {
			return formattedAddress;
		}

		public String getPlaceId() {
			return placeId;
		}
	}

	// Response classes for Google Maps API
	public static class GoogleMapsApiResponse {
		private java.util.List<Result> results;

		public java.util.List<Result> getResults() {
			return results;
		}

		public void setResults(java.util.List<Result> results) {
			this.results = results;
		}

		public static class Result {
			private String formatted_address;
			private String place_id;

			public String getFormattedAddress() {
				return formatted_address;
			}

			public void setFormattedAddress(String formatted_address) {
				this.formatted_address = formatted_address;
			}

			public String getPlaceId() {
				return place_id;
			}

			public void setPlaceId(String place_id) {
				this.place_id = place_id;
			}
		}
	}
}
