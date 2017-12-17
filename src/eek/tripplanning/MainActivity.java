package eek.tripplanning;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button btnPlanTrip;
	Button btnFilterAttraction;
	Button btnLogin;
	
	ArrayList<Integer> TID_List=new ArrayList<Integer>();
	ArrayList<String> AttractionType_List=new ArrayList<String>();
	
	ArrayList<Integer> PID_List=new ArrayList<Integer>();
	ArrayList<String> PlaceName_Liist=new ArrayList<String>();
	
	ArrayList<Trip> trip_List=new ArrayList<Trip>();

	SQLiteDatabase db;
	String path=Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath=path+"/"+"MobileTripDB";
	String TableName="AttractionTypeTable";
	String TableName1="TouristAttractionTable";
	String TableName2="DistanceTable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnPlanTrip=(Button) findViewById(R.id.btnPlanTrip);
        btnFilterAttraction=(Button) findViewById(R.id.btnFilterAttraction);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        
        btnPlanTrip.setOnClickListener(new onClick());
        btnFilterAttraction.setOnClickListener(new onClick());
        btnLogin.setOnClickListener(new onClick());
       
    }
    public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(v.getId()==btnPlanTrip.getId()){
					
					Intent PlanTrip=new Intent(MainActivity.this, PlanTrip.class);
					PlanTrip.putExtra("TripList", trip_List);
					startActivity(PlanTrip);
					trip_List.clear();
				}
				if(v.getId()==btnFilterAttraction.getId()){
					
					Intent PlanTripIntent=new Intent(MainActivity.this, RetrieveTrip.class);
					PlanTripIntent.putExtra("TripList", trip_List);
					startActivity(PlanTripIntent);
					trip_List.clear();
					
				}
				if(v.getId()==btnLogin.getId()){
					Intent adminIntent=new Intent(MainActivity.this, LoginIntent.class);
					startActivity(adminIntent);
				}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		TID_List.clear();
		AttractionType_List.clear();
		PID_List.clear();
		PlaceName_Liist.clear();
		trip_List.clear();
		this.finish();
	}

}
