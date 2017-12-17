package eek.tripplanning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapterResult extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;

	private List<Path> ResultList = new ArrayList<Path>();
	private ArrayList<Path> arraylist;

	public ListViewAdapterResult(Context context,
			ArrayList<Path> result_Path_List) {
		mContext = context;
		this.ResultList = result_Path_List;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Path>();
		this.arraylist.addAll(result_Path_List);
	}

	public class ViewHolder {
		TextView txtRID;
		TextView txtResultPath;
		TextView txtPathWeight;
		TextView txtTime;
	}

	@Override
	public int getCount() {
		return ResultList.size();
	}

	@Override
	public Path getItem(int position) {
		return ResultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item_result, null);
			holder.txtRID = (TextView) view.findViewById(R.id.txtRID);
			holder.txtTime = (TextView) view.findViewById(R.id.txtTime);
			holder.txtResultPath = (TextView) view
					.findViewById(R.id.txtResultPath);
			holder.txtPathWeight = (TextView) view
					.findViewById(R.id.txtPathWeight);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.txtRID.setText(ResultList.get(position).getRID() + "");
		holder.txtResultPath.setText(ResultList.get(position).getPath() + "");
		holder.txtPathWeight.setText(String.format("%.2f", new BigDecimal(
				ResultList.get(position).getTotalCost()))
				+ " Km");
		
		if (ResultList.get(position).getTime() < 1) {
			holder.txtTime.setText(String.format("%.2f", new BigDecimal(
					ResultList.get(position).getTime() * 60))
					+ " min");
		} else {
			double time=ResultList.get(position).getTime()-0.5;
			holder.txtTime
					.setText(String.format("%.0f", new BigDecimal((time* 60) / 60))
							+ " hr "
							+ String.format("%.2f", new BigDecimal((ResultList
									.get(position).getTime() * 60) % 60))
							+ " min");
		}
		
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// *********get Latitude & Longitude using ResultPath to show
				// map***********

				Intent intent = new Intent(mContext, GoogleMapResult.class);

				intent.putExtra("PID", ResultList.get(position).getPID());

				mContext.startActivity(intent);
			}
		});

		return view;
	}
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		ResultList.clear();
		if (charText.length() == 0) {
			ResultList.addAll(arraylist);
		} 
		else 
		{
			for (Path rp : arraylist) 
			{
				if (rp.getPath().toLowerCase(Locale.getDefault()).contains(charText))
				{
					ResultList.add(rp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
