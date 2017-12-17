package eek.tripplanning;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GoogleMapResult extends FragmentActivity implements OnMapReadyCallback  {
	
    private GoogleMap mMap;
    ArrayList<Integer> Result_PID=new ArrayList<Integer>();
    ArrayList<Trip> Trip_List=new ArrayList<Trip>();
    
    SQLiteDatabase db;
	String path=Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath=path+"/"+"MobileTripDB";
	String TableName1="TouristAttractionTable";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_result);
 
        String PID=getIntent().getStringExtra("PID");
        String placeName[]=PID.split("_");
        
        for(int i=0;i<placeName.length;i++){
        	Result_PID.add(Integer.parseInt(placeName[i]));
        	System.out.println(Result_PID.get(i));
        }
       
        db=SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		
		db.beginTransaction();
		
		String mySQL3="select * from "+TableName1;
		
		Cursor c3=db.rawQuery(mySQL3, null);
		
		while(c3.moveToNext()){
		for(int j=0;j<Result_PID.size();j++){
			if(c3.getInt(c3.getColumnIndex("PID"))==Result_PID.get(j)){
					Trip t=new Trip();
					t.setPID(c3.getInt(c3.getColumnIndex("PID")));
					t.setPlaceName(c3.getString(c3.getColumnIndex("PlaceName")));
					t.setLattitude(c3.getDouble(c3.getColumnIndex("Latitude")));
					t.setLongitude(c3.getDouble(c3.getColumnIndex("Longitude")));
					t.setPlaceDescription(c3.getString(c3.getColumnIndex("PlaceDescription")));
					t.setImageByte(c3.getBlob(c3.getColumnIndex("image")));
					t.setTID(c3.getInt(c3.getColumnIndex("TID")));
					Trip_List.add(t);
				}
			}
		}
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
        
        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback)this);
 
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Result_PID.clear();
		Trip_List.clear();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		
		mMap=googleMap;
		ArrayList<LatLng> Lat_Lng=new ArrayList<LatLng>();
		for(int n=0;n<Trip_List.size();n++){
			
			LatLng lat_long=new LatLng(Trip_List.get(n).getLattitude(), Trip_List.get(n).getLongitude());
			mMap.addMarker(new MarkerOptions().position(lat_long).title(Trip_List.get(n).getPlaceName()));
			Lat_Lng.add(lat_long);
			
			mMap.moveCamera(CameraUpdateFactory.newLatLng(lat_long));
			PolylineOptions rectOptions = new PolylineOptions();
			rectOptions.addAll(Lat_Lng);
			rectOptions.width(5);
			rectOptions.color(Color.RED);
			
			mMap.addPolyline(rectOptions);
		}
			
			
			Result_PID.clear();
			Trip_List.clear();
			
	}
}
