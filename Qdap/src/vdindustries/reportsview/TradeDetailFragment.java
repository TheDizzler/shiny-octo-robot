package vdindustries.reportsview;

import vdindustries.Qdap.R;
import vdindustries.content.TradeContent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** A fragment representing a single Trade detail screen.
 * This fragment is either contained in a {@link TradeListActivity} in two-pane
 * mode (on tablets) or a {@link TradeDetailActivity} on handsets. */
public class TradeDetailFragment extends Fragment {
	
	/** The fragment argument representing the item ID that this fragment
	 * represents. */
	public static final String		ARG_ITEM_ID	= "item_id";
	
	private TradeContent.TradeItem	tradeItem;
	
	/** Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes). */
	public TradeDetailFragment() {
	
	}
	
	
	@Override public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			tradeItem = TradeContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}
	
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
										Bundle savedInstanceState) {
	
		View rootView = inflater.inflate(R.layout.fragment_trade_detail, container, false);
		
		// Show the content as text in a TextView.
		if (tradeItem != null) {
			
			StringBuilder stringDef = new StringBuilder();
			for (int i = 0; i < tradeItem.deficiencies.size(); ++i) {
				stringDef.append(tradeItem.deficiencies.get(i).toString() + "\n");
			}
			((TextView) rootView.findViewById(R.id.trade_detail))
				.setText(tradeItem.deficiencies.size() + "\n" + stringDef);
		}
		
		return rootView;
	}
	
}
