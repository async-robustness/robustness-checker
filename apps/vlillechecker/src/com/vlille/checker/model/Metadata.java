package com.vlille.checker.model;

public class Metadata /*extends Entity*/ {

    public static final String LAST_UPDATE = "lastUpdate";
    public static final String LATITUDE_E6 = "latitudeE6";
    public static final String LONGITUDE_E6 = "longitudeE6";

	/**
	 * Last update when was checked the vlille stations.
	 */
	public long lastUpdate;
	
	/**
	 * Default latitude.
	 */
    public int latitude1e6;
	
	/**
	 * Default longitude.
	 */
    public int longitude1e6;

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getLatitude1e6() {
		return latitude1e6;
	}

	public void setLatitude1e6(int latitude) {
		this.latitude1e6 = latitude;
	}

	public int getLongitude1e6() {
		return longitude1e6;
	}

	public void setLongitude1e6(int longitude) {
		this.longitude1e6 = longitude;
	}

}
