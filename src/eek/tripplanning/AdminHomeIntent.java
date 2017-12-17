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

public class AdminHomeIntent extends Activity {

	Button btnCreateTouristAttraction;
	Button btnCreateDistance;
	Button btnCreateAttraction;
	
	ArrayList<Integer> TID_List=new ArrayList<Integer>();
	ArrayList<String> AttractionTypeName_Liist=new ArrayList<String>();
	
	ArrayList<Integer> PID_List=new ArrayList<Integer>();
	ArrayList<String> PlaceName_Liist=new ArrayList<String>();
	
	SQLiteDatabase db;
	String path=Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath=path+"/"+"MobileTripDB";
	String TableName="AttractionTypeTable";
	String TableName1="TouristAttractionTable";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_home);
		
		btnCreateTouristAttraction=(Button) findViewById(R.id.btnCreateTouristAttraction);
		btnCreateDistance=(Button) findViewById(R.id.btnCreateDistance);
		btnCreateAttraction=(Button) findViewById(R.id.btnCreateAttraction);
		
		btnCreateTouristAttraction.setOnClickListener(new onClick());
		btnCreateDistance.setOnClickListener(new onClick());
		btnCreateAttraction.setOnClickListener(new onClick());
	}

	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(v.getId()==btnCreateTouristAttraction.getId()){

					db=SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
					
					db.beginTransaction();
					
					String mySQL="select * from "+TableName;
					
					Cursor c1=db.rawQuery(mySQL, null);
					
					while(c1.moveToNext()){

						TID_List.add(c1.getInt(c1.getColumnIndex("TID")));
						AttractionTypeName_Liist.add(c1.getString(c1.getColumnIndex("AttractionTypeName")));
						
					}
					
					db.setTransactionSuccessful();
					
					db.endTransaction();
		
					Intent tourstAttraction=new Intent(AdminHomeIntent.this, CreateTouristAttraction.class);
					Bundle AttractionBundle=new Bundle();
					AttractionBundle.putIntegerArrayList("TID", TID_List);
					AttractionBundle.putStringArrayList("AttractionTypeName", AttractionTypeName_Liist);
					tourstAttraction.putExtra("BD", AttractionBundle);
					startActivity(tourstAttraction);
					
					TID_List.clear();
					AttractionTypeName_Liist.clear();
				
				}
				if(v.getId()==btnCreateDistance.getId()){
					
					db=SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
					
					db.beginTransaction();
					
					String mySQL="select * from "+TableName1;
					
					Cursor c1=db.rawQuery(mySQL, null);
					
					while(c1.moveToNext()){

						PID_List.add(c1.getInt(c1.getColumnIndex("PID")));
						PlaceName_Liist.add(c1.getString(c1.getColumnIndex("PlaceName")));
						
					}
					
					db.setTransactionSuccessful();
					
					db.endTransaction();
		
					Intent distance=new Intent(AdminHomeIntent.this, CreateDistance.class);
					Bundle AttractionBundle=new Bundle();
					AttractionBundle.putIntegerArrayList("PID", PID_List);
					AttractionBundle.putStringArrayList("PlaceName", PlaceName_Liist);
					distance.putExtra("BD", AttractionBundle);
					startActivity(distance);
					
					PID_List.clear();
					PlaceName_Liist.clear();

				}
				if(v.getId()==btnCreateAttraction.getId()){
					Intent attraction=new Intent(AdminHomeIntent.this,CreateAttractionType.class);
					startActivity(attraction);
				}
		}

	}
	
}
