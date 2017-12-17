package eek.tripplanning;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTouristAttraction extends Activity {

	EditText txtPlaceName;
	EditText txtLat;
	EditText txtLong;
	Spinner spnAttractionType;
	EditText txtPlaceDescription;
	TextView txtImage;
	Button btnBrowse;
	Button btnInsertPlace;

	String placeName, placeDescription;
	double Latitude, Longitude;
	int attractionType;
	public static Uri selectedImageUri;

	ArrayList<Integer> TID_List = new ArrayList<Integer>();
	ArrayList<String> AttractionType_List = new ArrayList<String>();

	SQLiteDatabase db;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath = path + "/" + "MobileTripDB";
	String TableName = "AttractionTypeTable";
	String TableName1 = "TouristAttractionTable";
	String TableName2 = "DistanceTable";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_toursit_attraction);

		Intent getResponseData = getIntent();
		Bundle bdResponse = getResponseData.getBundleExtra("BD");

		TID_List.addAll(bdResponse.getIntegerArrayList("TID"));
		AttractionType_List.addAll(bdResponse
				.getStringArrayList("AttractionTypeName"));

		txtPlaceName = (EditText) findViewById(R.id.txtPlaceName);
		txtLat = (EditText) findViewById(R.id.txtLat);
		txtLong = (EditText) findViewById(R.id.txtLong);
		spnAttractionType = (Spinner) findViewById(R.id.spnAttractionType);
		txtPlaceDescription = (EditText) findViewById(R.id.txtPlaceDescription);
		txtImage = (TextView) findViewById(R.id.txtImage);
		btnBrowse = (Button) findViewById(R.id.btnBrowse);
		btnInsertPlace = (Button) findViewById(R.id.btnInsert);

		ArrayAdapter<String> AttractionType = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				AttractionType_List);
		spnAttractionType.setAdapter(AttractionType);

		btnBrowse.setOnClickListener(new onClick());
		btnInsertPlace.setOnClickListener(new onClick());
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TableName1
				+ " (PID Integer PRIMARY KEY autoincrement,PlaceName VARCHAR,Latitude Double,Longitude Double,TID Integer,PlaceDescription VARCHAR,image BLOB);");

	}

	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == btnBrowse.getId()) {
				openImageChooser();
				if (selectedImageUri != null) {
					String path = selectedImageUri.getPath().toString();
					txtImage.setText(path);
				}
			}
			if (v.getId() == btnInsertPlace.getId()) {
				// place1=PID_List.get(spnPlace1.getSelectedItemPosition());
				Cursor c = db.rawQuery(
						"SELECT * FROM TouristAttractionTable where PlaceName=='"
								+ txtPlaceName.getText() + "';", null);
				if (c.getCount() > 0) {
					showMessage("Error", "Existing PlaceName");
				} else {
					if (txtPlaceName.getText().toString().trim().length() == 0
							|| txtLat.getText().toString().trim().length() == 0
							|| txtLong.getText().toString().trim().length() == 0
							|| txtPlaceDescription.getText().toString().trim()
									.length() == 0) {
						showMessage("Error", "Enter data");
						return;
					} else {
						placeName = txtPlaceName.getText().toString();
						Latitude = Double.parseDouble(txtLat.getText()
								.toString().trim());
						Longitude = Double.parseDouble(txtLong.getText()
								.toString().trim());
						attractionType = TID_List.get(spnAttractionType
								.getSelectedItemPosition());
						placeDescription = txtPlaceDescription.getText()
								.toString();

						InputStream iStream = null;
						byte[] inputData = null;
						try {
							iStream = getContentResolver().openInputStream(
									selectedImageUri);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						try {
							inputData = Utils.getBytes(iStream);
						} catch (IOException e) {
							e.printStackTrace();
						}

						ContentValues value = new ContentValues();
						value.put("PlaceName", placeName);
						value.put("Latitude", Latitude);
						value.put("Longitude", Longitude);
						value.put("TID", attractionType);
						value.put("PlaceDescription", placeDescription);
						value.put("image", inputData);

						db.insert(TableName1, null, value);

						Toast.makeText(CreateTouristAttraction.this,
								"Insert Attraction Name Successful",
								Toast.LENGTH_LONG).show();

						txtPlaceName.setText("");
						txtLat.setText("");
						txtLong.setText("");
						txtPlaceDescription.setText("");
						txtImage.setText("");

					}
				}
			}
		}

		private void showMessage(String title, String message) {
			// TODO Auto-generated method stub
			Builder builder = new Builder(CreateTouristAttraction.this);
			builder.setCancelable(true);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.show();
		}
	}

	void openImageChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				100);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {
				selectedImageUri = data.getData();
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		TID_List.clear();
		AttractionType_List.clear();
	}

}
