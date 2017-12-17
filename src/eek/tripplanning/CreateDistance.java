package eek.tripplanning;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateDistance extends Activity {

	Spinner spnPlaceFrom;
	Spinner spnPlaceTo;
	EditText txtDistance;
	Button btnInsertDistance;
	
	ArrayList<Integer> PID_List=new ArrayList<Integer>();
	ArrayList<String> PlaceName_Liist=new ArrayList<String>();
	
	SQLiteDatabase db;
	String path=Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath=path+"/"+"MobileTripDB";
	String TableName2="DistanceTable";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_distance);
		
		Intent getResponseData=getIntent();
		Bundle bdResponse=getResponseData.getBundleExtra("BD");
		
		PID_List.addAll(bdResponse.getIntegerArrayList("PID"));
		PlaceName_Liist.addAll(bdResponse.getStringArrayList("PlaceName"));
		
		spnPlaceFrom=(Spinner) findViewById(R.id.spnPlaceFrom);
		spnPlaceTo=(Spinner) findViewById(R.id.spnPlaceTo);
		txtDistance=(EditText) findViewById(R.id.txtDistance);
		btnInsertDistance=(Button) findViewById(R.id.btnInsertDistance);
		
		ArrayAdapter<String> placeFrom=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, PlaceName_Liist);
		spnPlaceFrom.setAdapter(placeFrom);
		
		ArrayAdapter<String> placeTo=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, PlaceName_Liist);
		spnPlaceTo.setAdapter(placeTo);
		
		btnInsertDistance.setOnClickListener(new onClick());
	}
	
	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(v.getId()==btnInsertDistance.getId()){
					
					int placeFrom,placeTo;
					Double distance;
					
					distance=Double.parseDouble(txtDistance.getText().toString().trim());
					
					db=SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
					placeFrom=PID_List.get(spnPlaceFrom.getSelectedItemPosition());
					placeTo=PID_List.get(spnPlaceTo.getSelectedItemPosition());
					
					long retValue;
					
					db.beginTransaction();
					
					db.execSQL("CREATE TABLE IF NOT EXISTS "+TableName2+" (DID Integer PRIMARY KEY autoincrement,PlaceFrom Integer,PlaceTo Integer,Distance Double);");
					
					ContentValues value=new ContentValues();
					value.put("PlaceFrom", placeFrom);
					value.put("PlaceTo", placeTo);
					value.put("Distance", distance);

					retValue=db.insert(TableName2, null, value);
					
					db.setTransactionSuccessful();
					
					db.endTransaction();
					
					if(retValue>0){
						Toast.makeText(CreateDistance.this, "Insert Place Distance Successful",
								   Toast.LENGTH_LONG).show();
						
						txtDistance.setText("");
						
					}
				}
		}

	}


}
