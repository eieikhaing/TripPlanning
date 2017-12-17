package eek.tripplanning;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

public class ShowResultList extends Activity {

	EditText txtSearchResult;
	ListView listResult;
	ListViewAdapterResult adapterResult;

	ArrayList<Path> Result_List = new ArrayList<Path>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result_rayout);

		txtSearchResult = (EditText) findViewById(R.id.txtSearchResult);
		listResult = (ListView) findViewById(R.id.listResult);

		Result_List = (ArrayList<Path>) getIntent()
				.getSerializableExtra("Result_Path_List");

		adapterResult = new ListViewAdapterResult(this, Result_List);

		listResult.setAdapter(adapterResult);
		txtSearchResult.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = txtSearchResult.getText().toString().toLowerCase(Locale.getDefault());
				Log.e("Search", text);
				adapterResult.filter(text);
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

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Result_List.clear();
		this.finish();
	}
}
