package vdindustries.checklists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.Qdap.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class TradesChoiceFragment extends Fragment {

	// private static int position;
	protected static long id;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	ArrayList<String> tradesList;

	private Context context;
	/** Used to communicate with activity. */
	@SuppressWarnings("unused")
	private OnTradeSelectedListener listener;

	private static String tradeSelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_checklists_trade, container, false);

		context = view.getContext();

		ListView trades = (ListView) view.findViewById(R.id.listview_trade_checklists);

		// View roughins = inflater.inflate(R.layout.roughins, null);
		// trades.addHeaderView(roughins);
		tradesList = new ArrayList<String>();
		tradesList.add("Framing");
		tradesList.add("Mechanical");
		tradesList.add("Electrical");
		tradesList.add("Security");
		tradesList.add("HVAC");
		tradesList.add("Insulation");

		// View finishes = getLayoutInflater().inflate(R.layout.finishes, null);
		// tradesList.add("FINISHES");
		tradesList.add("Drywall");
		tradesList.add("Paint");
		tradesList.add("Mechanical(f)");
		tradesList.add("Electrical(f)");
		tradesList.add("Flooring");
		tradesList.add("Cabinets");
		tradesList.add("Countertops");
		tradesList.add("Finish Carp.");

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
				tradesList);

		// Set The Adapter
		trades.setAdapter(arrayAdapter);

		// register onClickListener to handle click events on each item
		trades.setOnItemClickListener(new OnItemClickListener() {

			// argument position gives the index of item which is clicked
			public void onItemClick(AdapterView<?> adapterview, View v, int position, long id) {

				TradesChoiceFragment.id = id;
				// TradesChoiceFragment.position = position;

				// if (position > 0) {
				// tradeSelected = tradesList.get(position - 1);
				// }
				tradeSelected = tradesList.get(position);
				((CheckListsActivity) getActivity()).onTradeSelected(tradeSelected);

			}
		});

		return view;
	}

	public void onAttach(Activity activity) {

		super.onAttach(activity);
		if (activity instanceof OnTradeSelectedListener) {
			listener = (OnTradeSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implemenet TradesChoiceFragment"
					+ ".OnTradeSelectedListener");
		}

	}

	@Override
	public void onDetach() {

		super.onDetach();
		listener = null;
	}

	/** Used to communicate with activity. */
	public interface OnTradeSelectedListener {

		public void onTradeSelected(String tradeSelected);
	}
}
