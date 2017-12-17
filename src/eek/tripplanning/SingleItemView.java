package eek.tripplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleItemView extends Activity {

	// Declare Variables
	/* TextView txtPID; */
	TextView txtPlaceName;
	TextView txtLatitude;
	TextView txtLongitude;
	TextView txtPlaceDescription;
	TextView txtPlaceType;
	ImageView imgPlace;

	int PID, TID;
	String PlaceName, PlaceDescription;
	double Latitude, Longitude;
	byte[] image;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlelist_item);

		txtPlaceName = (TextView) findViewById(R.id.txtPlaceName);

		txtPlaceDescription = (TextView) findViewById(R.id.txtPlaceDescription);
		imgPlace = (ImageView) findViewById(R.id.imgPlace);

		Intent i = getIntent();

		PID = i.getIntExtra("PID", PID);
		PlaceName = i.getStringExtra("PlaceName");
		Latitude = i.getDoubleExtra("Latitude", Latitude);
		Longitude = i.getDoubleExtra("Longitude", Longitude);
		TID = i.getIntExtra("TID", TID);
		PlaceDescription = i.getStringExtra("PlaceDescription");
		image = i.getByteArrayExtra("image");

		txtPlaceName.setText(PlaceName);

		txtPlaceDescription.setText(PlaceDescription);
		imgPlace.setImageBitmap(Utils.getImage(image));

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();

	}

}
