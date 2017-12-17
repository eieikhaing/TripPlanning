package eek.tripplanning;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;

	private List<Trip> TripList = new ArrayList<Trip>();
	private ArrayList<Trip> arraylist;

	String status;
	SQLiteDatabase db;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	String dbPath = path + "/" + "MobileTripDB";
	String TableName = "AttractionTypeTable";
	String TableName1 = "TouristAttractionTable";
	String TableName2 = "DistanceTable";
	private AlertDialog.Builder builder;

	public ListViewAdapter(Context context, List<Trip> placeList, String status) {
		mContext = context;
		this.TripList = placeList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Trip>();
		this.arraylist.addAll(placeList);
		this.status = status;
	}

	public class ViewHolder {
		TextView placeName;
		// TextView PID;
		ImageView placeImage;
	}

	@Override
	public int getCount() {
		return TripList.size();
	}

	@Override
	public Trip getItem(int position) {
		return TripList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);

			holder.placeName = (TextView) view.findViewById(R.id.txtPName);
			// holder.PID = (TextView) view.findViewById(R.id.txtPID);
			holder.placeImage = (ImageView) view.findViewById(R.id.imgPlace);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// holder.PID.setText(TripList.get(position).getPID()+"");
		holder.placeName.setText(TripList.get(position).getPlaceName() + "");

		holder.placeImage.setImageBitmap(Utils.getImage(TripList.get(position)
				.getImageByte()));

		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (status == "R") {
					builder = new AlertDialog.Builder(mContext);
					builder.setTitle("Delete "
							+ TripList.get(position).getPlaceName());
					builder.setMessage("Do you want to delete?");
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									db.delete(TableName1, "PID" + "="
											+ TripList.get(position).getPID(),
											null);

									db.delete(TableName2, "PlaceFrom" + "="
											+ TripList.get(position).getPID()
											+" or "+ "PlaceTo" + "="
											+ TripList.get(position).getPID(),
											null);
				

									dialog.cancel();

									Intent intent = new Intent(mContext,
											RetrieveTrip.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
											| Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_CLEAR_TASK);
									mContext.startActivity(intent);

								}

							});
					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();

				}
				return true;
			}

		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (status == "R") {

					Intent intent = new Intent(mContext, SingleItemView.class);

					intent.putExtra("PID", TripList.get(position).getPID());
					intent.putExtra("PlaceName", TripList.get(position)
							.getPlaceName());
					intent.putExtra("Latitude", TripList.get(position)
							.getLattitude());
					intent.putExtra("Longitude", TripList.get(position)
							.getLongitude());
					intent.putExtra("PlaceDescription", TripList.get(position)
							.getPlaceDescription());
					intent.putExtra("image", TripList.get(position)
							.getImageByte());
					intent.putExtra("TID", TripList.get(position).getTID());

					mContext.startActivity(intent);
				}

			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		TripList.clear();
		if (charText.length() == 0) {
			TripList.addAll(arraylist);
		} else {
			for (Trip tp : arraylist) {
				if (tp.getPlaceName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					TripList.add(tp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
