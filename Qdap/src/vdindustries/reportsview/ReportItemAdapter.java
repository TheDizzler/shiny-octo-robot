package vdindustries.reportsview;


import vdindustries.masterflow.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ReportItemAdapter extends ArrayAdapter<ReportItem> {

	Context context;
	int layoutResourceId;
	ReportItem data[] = null;

	public ReportItemAdapter(Context context, int layoutResourceId,
			ReportItem[] data) {

		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ReportItemHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ReportItemHolder();

			holder.reportCheck = (CheckBox) row.findViewById(R.id.reportCheck);
			holder.reportText = (TextView) row.findViewById(R.id.reportText);

			
			row.setTag(holder);
		} else {
			holder = (ReportItemHolder) row.getTag();
		}

		ReportItem reportItem = data[position];
		holder.reportText.setText(reportItem.text);
		holder.reportCheck.setClickable(true);
		holder.reportCheck.setChecked(reportItem.checked);

		return row;
	}

	static class ReportItemHolder {
		CheckBox reportCheck;
		TextView reportText;
	}
}
