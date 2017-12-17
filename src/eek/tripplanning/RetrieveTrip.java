package eek.tripplanning;

import java.util.ArrayList;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import eek.tripplanning.Trip;

public class RetrieveTrip extends Activity implements OnItemSelectedListener {

	EditText txtSearchPlace;
	ListView listPlaces;
	ListViewAdapter adapter;
	Spinner spnFilterAttraction;
	// Button btnFilterAttraction;
	RadioGroup radGroup;
	RadioButton radAll, radType;
	ArrayList<Trip> Place_List = new ArrayList<Trip>();

	ArrayList<Trip> Trip_List = new ArrayList<Trip>();
	ArrayList<Trip> Type_List = new ArrayList<Trip>();
	SQLiteDatabase db;
	int pos, pos1;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath = path + "/" + "MobileTripDB";
	String TableName = "AttractionTypeTable";
	String TableName1 = "TouristAttractionTable";
	String TableName2 = "DistanceTable";

	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retrieve_layout);

		txtSearchPlace = (EditText) findViewById(R.id.txtSearchPlace);
		listPlaces = (ListView) findViewById(R.id.listPlaces);
		// btnFilterAttraction = (Button)
		// findViewById(R.id.btnFilterAttraction);
		spnFilterAttraction = (Spinner) findViewById(R.id.spnFilterAttraction);
		radGroup = (RadioGroup) findViewById(R.id.ragGroup);
		final String value = ((RadioButton) findViewById(radGroup
				.getCheckedRadioButtonId())).getText().toString();
		radAll = (RadioButton) findViewById(R.id.radTypes);
		radType = (RadioButton) findViewById(R.id.radChooseTypes);
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		db.beginTransaction();
		String mySQL = "select * from " + TableName;
		Cursor c = db.rawQuery(mySQL, null);
		while (c.moveToNext()) {
			Trip t = new Trip();
			t.setTID(c.getInt(c.getColumnIndex("TID")));
			t.setAttractionTypeName(c.getString(c
					.getColumnIndex("AttractionTypeName")));

			Type_List.add(t);
		}
		String mySQL2 = "select * from " + TableName1;

		Cursor c2 = db.rawQuery(mySQL2, null);

		while (c2.moveToNext()) {

			Trip t = new Trip();
			t.setPID(c2.getInt(c2.getColumnIndex("PID")));
			t.setPlaceName(c2.getString(c2.getColumnIndex("PlaceName")));
			t.setLattitude(c2.getDouble(c2.getColumnIndex("Latitude")));
			t.setLongitude(c2.getDouble(c2.getColumnIndex("Longitude")));
			t.setPlaceDescription(c2.getString(c2
					.getColumnIndex("PlaceDescription")));
			t.setImageByte(c2.getBlob(c2.getColumnIndex("image")));
			t.setTID(c2.getInt(c2.getColumnIndex("TID")));
			Trip_List.add(t);

		}
		db.setTransactionSuccessful();
		db.endTransaction();

		ArrayList<String> Attraction_Type_List = new ArrayList<String>();

		for (int i = 0; i < Type_List.size(); i++) {
			Attraction_Type_List.add(Type_List.get(i).getAttractionTypeName());
		}

		ArrayAdapter<String> Attraction_Type = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Attraction_Type_List);
		spnFilterAttraction.setAdapter(Attraction_Type);

		spnFilterAttraction.setOnItemSelectedListener(this);

		for (int i = 0; i < Trip_List.size(); i++) {
			Trip trip = new Trip(Trip_List.get(i).getPID(), Trip_List.get(i)
					.getPlaceName(), Trip_List.get(i).getLattitude(), Trip_List
					.get(i).getLongitude(), Trip_List.get(i)
					.getPlaceDescription(), Trip_List.get(i).getImageByte(),
					Trip_List.get(i).getTID());
			Place_List.add(trip);
		}

		adapter = new ListViewAdapter(this, Place_List, "R");

		listPlaces.setAdapter(adapter);

		txtSearchPlace.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = txtSearchPlace.getText().toString()
						.toLowerCase(Locale.getDefault());
				Log.e("Search", text);
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});

		int radioId = radGroup.getCheckedRadioButtonId();
		radGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				// Method 1 For Getting Index of RadioButton
				pos = radGroup.indexOfChild(findViewById(checkedId));

				if (pos == 0) {
					adapter = new ListViewAdapter(RetrieveTrip.this,
							Place_List, "R");
					listPlaces.setAdapter(adapter);

				}
				pos1 = radGroup.indexOfChild(findViewById(radGroup
						.getCheckedRadioButtonId()));
				if (pos1 == 1) {
					ArrayList<Trip> Place_List = new ArrayList<Trip>();

					ArrayList<Trip> Trip_List = new ArrayList<Trip>();

					int TID = spnFilterAttraction.getSelectedItemPosition() + 1;
					System.out.println("Type ID " + TID);

					db = SQLiteDatabase.openDatabase(dbPath, null,
							SQLiteDatabase.OPEN_READWRITE);
					db.beginTransaction();
					String mySQL3 = "select * from " + TableName1
							+ " where TID=" + TID;
					Cursor c3 = db.rawQuery(mySQL3, null);
					while (c3.moveToNext()) {
						Trip t = new Trip();
						t.setPID(c3.getInt(c3.getColumnIndex("PID")));
						t.setPlaceName(c3.getString(c3
								.getColumnIndex("PlaceName")));
						t.setLattitude(c3.getDouble(c3
								.getColumnIndex("Latitude")));
						t.setLongitude(c3.getDouble(c3
								.getColumnIndex("Longitude")));
						t.setPlaceDescription(c3.getString(c3
								.getColumnIndex("PlaceDescription")));
						t.setImageByte(c3.getBlob(c3.getColumnIndex("image")));
						t.setTID(c3.getInt(c3.getColumnIndex("TID")));
						Trip_List.add(t);
					}
					db.setTransactionSuccessful();
					db.endTransaction();

					for (int i = 0; i < Trip_List.size(); i++) {
						Trip trip = new Trip(Trip_List.get(i).getPID(),
								Trip_List.get(i).getPlaceName(), Trip_List.get(
										i).getLattitude(), Trip_List.get(i)
										.getLongitude(), Trip_List.get(i)
										.getPlaceDescription(), Trip_List
										.get(i).getImageByte(), Trip_List
										.get(i).getTID());
						Place_List.add(trip);
					}

					adapter = new ListViewAdapter(RetrieveTrip.this,
							Place_List, "R");
					listPlaces.setAdapter(adapter);

				}

			}

		});

	}

	// TODO Auto-generated method stub

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Place_List.clear();
		Type_List.clear();
		this.finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (radType.isChecked()) {
			ArrayList<Trip> Place_List = new ArrayList<Trip>();

			ArrayList<Trip> Trip_List = new ArrayList<Trip>();

			int TID = spnFilterAttraction.getSelectedItemPosition() + 1;
			System.out.println("Type ID " + TID);

			db = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			db.beginTransaction();
			String mySQL3 = "select * from " + TableName1 + " where TID=" + TID;
			Cursor c3 = db.rawQuery(mySQL3, null);
			while (c3.moveToNext()) {
				Trip t = new Trip();
				t.setPID(c3.getInt(c3.getColumnIndex("PID")));
				t.setPlaceName(c3.getString(c3.getColumnIndex("PlaceName")));
				t.setLattitude(c3.getDouble(c3.getColumnIndex("Latitude")));
				t.setLongitude(c3.getDouble(c3.getColumnIndex("Longitude")));
				t.setPlaceDescription(c3.getString(c3
						.getColumnIndex("PlaceDescription")));
				t.setImageByte(c3.getBlob(c3.getColumnIndex("image")));
				t.setTID(c3.getInt(c3.getColumnIndex("TID")));
				Trip_List.add(t);
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			for (int i = 0; i < Trip_List.size(); i++) {
				Trip trip = new Trip(Trip_List.get(i).getPID(), Trip_List
						.get(i).getPlaceName(),
						Trip_List.get(i).getLattitude(), Trip_List.get(i)
								.getLongitude(), Trip_List.get(i)
								.getPlaceDescription(), Trip_List.get(i)
								.getImageByte(), Trip_List.get(i).getTID());
				Place_List.add(trip);
			}

			adapter = new ListViewAdapter(RetrieveTrip.this, Place_List, "R");
			listPlaces.setAdapter(adapter);

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
