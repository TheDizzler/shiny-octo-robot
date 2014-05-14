package vdindustries.reportsview;


import vdindustries.Categories;
import vdindustries.content.DeficiencyParser;
import vdindustries.reportsview.TradeDetailFragment;
import vdindustries.masterflow.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;



public class ReportItemAdapter extends ArrayAdapter<ReportItem> {
	public static ReportItemHolder holder;
	Context context;
	int layoutResourceId;
	ReportItem data[] = null;
	ReportItem reportItem;

	public ReportItemAdapter(Context context, int layoutResourceId,
			ReportItem[] data) {

		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
		/*ReportItemHolder*/ holder=null;

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

		  reportItem = data[position];
		holder.reportText.setText(reportItem.text);
		holder.reportCheck.setClickable(true);
		holder.reportCheck.setChecked(reportItem.checked);

		
        holder.reportCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean buttonChecked) {
            	//String test = reportItem.def.direction;
            	//CheckBox checkB = (CheckBox) buttonView.findViewById(R.id.reportCheck);
                //DeficiencyParser.setCompletionStatus(reportItem.def.roomNo, reportItem.def.trade, reportItem.reportID, buttonChecked);
            	  DeficiencyParser.setCompletionStatus(reportItem.def.roomNo, reportItem.def.trade, reportItem.reportID, buttonChecked);

            	 //data[position].checked=buttonChecked;
                // buttonView.setChecked(buttonChecked);
               // holder.reportCheck.setChecked(buttonChecked);
             // reportItem.checked = true;
            	// reportItem.def.completed=buttonChecked;
          //    TradeDetailFragment.setChecked();
           //	reportItem.checked=buttonChecked;
//            	 TradeDetailFragment.setChecked(null, parent, null);
                //this.notify();
             //  buttonView.setChecked(buttonChecked);
              
            }

        });
       

		
		return row;
	}

	static class ReportItemHolder {
		CheckBox reportCheck;
		TextView reportText;
	}
	
}
