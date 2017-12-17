package eek.tripplanning;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Trip implements Serializable {
	
	private int PID;
	private String placeName;
	private double lattitude;
	private double longitude;
	private String placeDescription;
	private byte[] imageByte;
	private int TID;
	private String AttractionTypeName;

	public Trip() {
	}

	public Trip(int pID, String PlaceName, double lat, double longi,
			String PlaceDescription, byte[] imgByte, int tID) {
		super();
		
		PID = pID;
		placeName = PlaceName;
		lattitude = lat;
		longitude = longi;
		placeDescription = PlaceDescription;
		imageByte = imgByte;
		TID = tID;
	}

	

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPlaceDescription() {
		return placeDescription;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public int getTID() {
		return TID;
	}

	public void setTID(int tID) {
		TID = tID;
	}

	public String getAttractionTypeName() {
		return AttractionTypeName;
	}

	public void setAttractionTypeName(String attractionTypeName) {
		AttractionTypeName = attractionTypeName;
	}

}
