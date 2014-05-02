package vdindustries.qdapreportview;

import vdindustries.trade.TradeContent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Trade detail screen.
 * This fragment is either contained in a {@link TradeListActivity} in two-pane
 * mode (on tablets) or a {@link TradeDetailActivity} on handsets.
 */
public class TradeDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String	ARG_ITEM_ID	= "item_id";
	
	/**
	 * The list this fragment is presenting.
	 */
	private TradeContent.Trade	mItem;
	
	
	ImageView image;
	
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TradeDetailFragment() {
	
	}
	
	
	@Override public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = TradeContent.TRADE_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
		
		image = (ImageView) getView().findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_launcher);
	}
	
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
										Bundle savedInstanceState) {
	
		View rootView = inflater.inflate(R.layout.fragment_trade_detail, container, false);
		
		// Show the content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.trade_detail)).setText(mItem.content);
		}
		
		
		
		return rootView;
	}
}
