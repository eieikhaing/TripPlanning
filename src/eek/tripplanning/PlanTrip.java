package eek.tripplanning;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("unused")
public class PlanTrip extends Activity {

	Spinner spnStart;
	Spinner spnEnd;
	Spinner spnVehicles;
	//Spinner spnPaths;
	EditText kvalue;
	// Spinner spnAttraction_Start,spnAttraction_End;
	Button btnSearch;

	static ArrayList<Trip> Trip_List = new ArrayList<Trip>();
	static ArrayList<Integer> PID_List = new ArrayList<Integer>();
	static ArrayList<Double> Lat_List = new ArrayList<Double>();
	static ArrayList<Double> Lon_List = new ArrayList<Double>();
	static ArrayList<String> PlaceName_List = new ArrayList<String>();

	ArrayList<Integer> PlaceFrom = new ArrayList<Integer>();
	ArrayList<Integer> PlaceTo = new ArrayList<Integer>();
	ArrayList<Double> Distance = new ArrayList<Double>();
	ArrayList<String> AttractionType_List = new ArrayList<String>();
	ArrayList<String> vehicle_List = new ArrayList<String>();
//	String Kpath_List[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
//			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
//			"22", "23", "24", "25","26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
//			"36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
//			"47", "48", "49", "50", "51", "52","43", "54", "55", "56",
//			"57", "58", "59", "60", "61", "62", "63", "100", "110", "120", "130","300", "350", "400","450","500", "550", "600","610","620","630","640","650","700","63", "1115", "2353", "2356" };

	@SuppressWarnings("rawtypes")
	public static Hashtable hash;
	public static int count;
	public static String END;
	public static String START;
	public static String nodePlace = "";

	static ArrayList<Double> weight = new ArrayList<Double>();
	static ArrayList<String> resultPath = new ArrayList<String>();
	SQLiteDatabase db;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath = path + "/" + "MobileTripDB";
	String TableName = "AttractionTypeTable";
	String TableName1 = "TouristAttractionTable";
	String TableName2 = "DistanceTable";

	@SuppressWarnings("rawtypes")
	public PlanTrip() {
		hash = new Hashtable();
		count = 0;
	}

	@SuppressWarnings({})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan_trip);

		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		db.beginTransaction();

		String mySQL3 = "select * from " + TableName1;

		Cursor c3 = db.rawQuery(mySQL3, null);

		while (c3.moveToNext()) {

			PID_List.add(c3.getInt(c3.getColumnIndex("PID")));
			PlaceName_List.add(c3.getString(c3.getColumnIndex("PlaceName")));

		}

		db.setTransactionSuccessful();

		db.endTransaction();

		spnStart = (Spinner) findViewById(R.id.spnStart);
		spnEnd = (Spinner) findViewById(R.id.spnEnd);
		spnVehicles = (Spinner) findViewById(R.id.spnVehicles);
		//spnPaths = (Spinner) findViewById(R.id.spnPaths);
		 kvalue=(EditText)findViewById(R.id.textView6);
		btnSearch = (Button) findViewById(R.id.btnSearch);

		ArrayAdapter<String> start = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				PlaceName_List);
		spnStart.setAdapter(start);

		ArrayAdapter<String> end = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				PlaceName_List);
		spnEnd.setAdapter(end);

		vehicle_List.add("Car");
		vehicle_List.add("Motorcycle");
		vehicle_List.add("Bicycle");
		vehicle_List.add("Walking");

		ArrayAdapter<String> vehicles = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				vehicle_List);
		spnVehicles.setAdapter(vehicles);

		//ArrayAdapter<String> kpath = new ArrayAdapter<String>(this,
		//		android.R.layout.simple_spinner_item, android.R.id.text1,
			//	Kpath_List);
		///spnPaths.setAdapter(kpath);
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		db.beginTransaction();

		String mySQL4 = "select * from " + TableName2;

		Cursor c4 = db.rawQuery(mySQL4, null);

		while (c4.moveToNext()) {
			PlaceFrom.add(c4.getInt(c4.getColumnIndex("PlaceFrom")));
			PlaceTo.add(c4.getInt(c4.getColumnIndex("PlaceTo")));
			Distance.add(c4.getDouble(c4.getColumnIndex("Distance")));
		}

		db.setTransactionSuccessful();

		db.endTransaction();

		btnSearch.setOnClickListener(new onClick());
	}

	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == btnSearch.getId()) {

				int startID, endID, startPosition, endPosition;
				String vehicles;

				startPosition = spnStart.getSelectedItemPosition();
				endPosition = spnEnd.getSelectedItemPosition();

				startID = PID_List.get(startPosition);
				endID = PID_List.get(endPosition);

				vehicles = spnVehicles.getSelectedItem().toString();

				Graph graph = new Graph();

				for (int i = 0; i < PlaceFrom.size(); i++) {
					graph.addEdge(PlaceFrom.get(i) + "", PlaceTo.get(i) + "",
							Distance.get(i));
				}

				START = startID + "";
				END = endID + "";

				String sourceNode, targetNode;
				//int K = Integer.parseInt(spnPaths.getSelectedItem().toString());
				int K = Integer.parseInt(kvalue.getText().toString());
				usageExample1(graph, START, END, K);
				ArrayList<String> resultNode = new ArrayList<String>();
				ArrayList<String> Result_PID = new ArrayList<String>();
				ArrayList<Path> Result_Path_List = new ArrayList<Path>();
				//System.out.println(resultPath.size());
				for (int i=0;i<resultPath.size();i++){
					
					System.out.println("Path "+i+"=  \t"+resultPath.get(i)+"     "+weight.get(i)+"\n");
				}
				
				
				// for (int j = 0; j < resultPath.size(); j++) {
				for (int j = 0; j < K; j++) {
					Result_PID.add(resultPath.get(j));

					String placeNamePath = resultPath.get(j);
					String placeName[] = placeNamePath.split("_");
					int placeNameID[] = new int[placeName.length];

					for (int m = 0; m < placeNameID.length; m++) {
						placeNameID[m] = Integer.parseInt(placeName[m]);
					}

					for (int i = 0; i < placeNameID.length; i++) {
						int placeID = placeNameID[i];
						for (int l = 0; l < PID_List.size(); l++) {
							if (placeID == PID_List.get(l)) {
								nodePlace += PlaceName_List.get(l) + "→";
							}
						}
					}
					String result = nodePlace.substring(0,
							nodePlace.length() - 1);
					resultNode.add(result);
					nodePlace = "";
				}

				String vehicle = spnVehicles.getSelectedItem().toString();
				double time = 0;

				for (int c = 0; c < resultNode.size(); c++) {
					Path result = new Path();
					result.setRID(c+1);
					result.setPath(resultNode.get(c));
					result.setTotalCost(weight.get(c));
					if (vehicle.equals("Car")) {
						time = weight.get(c) / 30;
					} else if (vehicle.equals("MotorCycle")) {
						time = weight.get(c) / 30;
					} else if (vehicle.equals("Bicycle")) {
						time = weight.get(c) / 12;
					} else if (vehicle.equals("Walking")) {
						time = weight.get(c) / 4;
					}
					// System.out.println(time);
					result.setTime(time);
					result.setPID(Result_PID.get(c));
					Result_Path_List.add(result);
				}

				Intent intent = new Intent(PlanTrip.this, ShowResultList.class);
				intent.putExtra("Result_Path_List", Result_Path_List);
				startActivity(intent);

				Result_Path_List.clear();
				resultPath.clear();
				weight.clear();
				nodePlace = "";
			}

		}

		public void usageExample1(Graph graph, String START, String END, int k) {
			// TODO Auto-generated method stub

			/* Compute the K shortest paths and record the completion time */
			System.out.print("Computing the " + k + " shortest paths from ["
					+ START + "] to [" + END + "] ");
			System.out.print("using Yen's algorithm... ");
			List<Path> ksp;
			long timeStart = System.currentTimeMillis();
			Yen yenAlgorithm = new Yen();
			ksp = yenAlgorithm.ksp(graph, START, END, k);
			long timeFinish = System.currentTimeMillis();
			System.out.println("complete.");

			System.out.println("Operation took " + (timeFinish - timeStart)
					/ 1000.0 + " seconds.");

			/* Output the K shortest paths */
			// System.out.println("k) cost: [path]");
			int n = 0;

			for (Path p : ksp) {
				// System.out.println(++n + ") " + p);
				resultPath.add(p.toString());
				weight.add(p.getTotalCost());
			}

		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Trip_List.clear();
		PID_List.clear();
		Lat_List.clear();
		Lon_List.clear();
		PlaceName_List.clear();
		PlaceFrom.clear();
		PlaceTo.clear();
		AttractionType_List.clear();
		vehicle_List.clear();
		weight.clear();

		this.finish();
	}

}