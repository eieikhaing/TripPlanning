package eek.tripplanning;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAttractionType extends Activity {

	EditText txtAttractionType;
	Button btnInsertAttractionType;

	SQLiteDatabase db;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath = path + "/" + "MobileTripDB";
	String TableName = "AttractionTypeTable";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_attraction_type);

		txtAttractionType = (EditText) findViewById(R.id.txtAttractionType);
		btnInsertAttractionType = (Button) findViewById(R.id.btnInsertAttractionType);

		btnInsertAttractionType.setOnClickListener(new onClick());
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TableName
				+ " (TID Integer PRIMARY KEY autoincrement,AttractionTypeName VARCHAR);");
	}

	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == btnInsertAttractionType.getId()) {
				String attractionType = txtAttractionType.getText().toString();
				Cursor c = db.rawQuery(
						"SELECT * FROM AttractionTypeTable where  AttractionTypeName=='"
								+ txtAttractionType.getText() + "';", null);
				if (c.getCount() > 0) {
					showMessage("Error", "Existing Type");
				} else {

					if (txtAttractionType.getText().toString().trim().length() == 0) {
						showMessage("Error", "Enter Type Name");
						return;
					} else {

						ContentValues value = new ContentValues();
						value.put("AttractionTypeName", attractionType);
						db.insert(TableName, null, value);
						showMessage("Success", "Record added");
						txtAttractionType.setText("");
					}
				}

			}
		}

		private void showMessage(String title, String message) {
			// TODO Auto-generated method stub
			Builder builder = new Builder(CreateAttractionType.this);
			builder.setCancelable(true);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.show();

		}

	}

}
