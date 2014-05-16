package vdindustries.reportsview;

import vdindustries.reportsview.TradeListFragment;
import vdindustries.Qdap.R;
import vdindustries.content.DeficiencyParser;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ReportItemAdapter extends ArrayAdapter<ReportItem> {
	
	Context		context;
	int			layoutResourceId;
	ReportItem	data[]	= null;
	
	public ReportItemAdapter(Context context, int layoutResourceId,
								ReportItem[] data) {
	
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override public View getView(final int position, View convertView,
									final ViewGroup parent) {
	
		View row = convertView;
		ReportItemHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ReportItemHolder();
			
			
			
			holder.reportCheck = (CheckBox) row.findViewById(R.id.reportCheck);
			holder.reportText = (TextView) row.findViewById(R.id.reportText);
			
			
			holder.reportCheck.setOnCheckedChangeListener(null);
			
			holder.reportCheck
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override public void onCheckedChanged(CompoundButton buttonView,
															boolean isChecked) {
					
						int getPosition = (Integer) buttonView.getTag();
						data[getPosition].checked = isChecked;
						ReportItem rItem = data[getPosition];
						DeficiencyParser.setCompletionStatus(
							rItem.def.roomNo, rItem.def.trade,
							rItem.reportID, isChecked);
						TradeListFragment.setDefic();
					}
				});
			row.setTag(holder);
			row.setTag(R.id.reportText, holder.reportText);
			row.setTag(R.id.reportCheck, holder.reportCheck);
		} else {
			holder = (ReportItemHolder) row.getTag();
		}
		ReportItem reportItem = data[position];
		holder.reportCheck.setTag(position);
		holder.reportText.setText(reportItem.text);
		holder.reportCheck.setChecked(reportItem.checked);
		return row;
	}
	
	protected class ReportItemHolder {
		
		CheckBox	reportCheck;
		TextView	reportText;
	}
	
	public void OnCheckedChangeListener() {
	
	}
	
}