package vdindustries.reportsview;

import vdindustries.Qdap.R;
import vdindustries.checklists.Room;
import vdindustries.content.TradeContent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

/** A fragment representing a single Trade detail screen.
 * This fragment is either contained in a {@link TradeListActivity} in two-pane
 * mode (on tablets) or a {@link TradeDetailActivity} on handsets. */
public class TradeDetailFragment extends Fragment {
	
	/** The fragment argument representing the item ID that this fragment
	 * represents. */
	public static final String		ARG_ITEM_ID	= "item_id";
	
	private TradeContent.TradeItem	tradeItem;
	
	ReportItem						reportListItem[];
	private static ListView			reportListView;
	
	/** Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes). */
	public TradeDetailFragment() {
	
	}
	
	
	@Override public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			tradeItem = TradeContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}
	
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
										Bundle savedInstanceState) {
	
		View rootView = inflater.inflate(R.layout.fragment_trade_detail, container, false);
		
		// Show the content in a ListView using a custom adapter
		if (tradeItem != null) {
			reportListItem = new ReportItem[tradeItem.deficiencies.size()];
			
			for (int i = 0; i < tradeItem.deficiencies.size(); ++i) {
				reportListItem[i] = new ReportItem(
					tradeItem.deficiencies.get(i));
				reportListItem[i].position = i;
				
			}
			
			ReportItemAdapter adapter = new ReportItemAdapter(
				getActivity(), R.layout.report_item_layout, reportListItem);
			
			reportListView = (ListView) rootView
				.findViewById(R.id.trade_detail);
			reportListView.setAdapter(adapter);
			
			ImageView plan = (ImageView) getActivity().findViewById(R.id.reportFloorplan);
			plan = new Room(getActivity(), "201", plan);
		}
		return rootView;
	}
}
