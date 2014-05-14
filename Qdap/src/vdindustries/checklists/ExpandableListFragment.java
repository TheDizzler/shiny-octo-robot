package vdindustries.checklists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.Qdap.R;
import vdindustries.content.DeficiencyParser;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.Toast;


public class ExpandableListFragment extends Fragment {
	
	ExpandableListAdapter			listAdapter;
	ExpandableListView				expListView;
	List<String>					listDataHeader;
	HashMap<String, List<String>>	listDataChild;
	
	/** Used to communicate with activity. */
	private OnItemSelectedListener	listener;
	private Context					context;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
	
		final View view = inflater.inflate(R.layout.fragment_checklists_list,
			container, false);
		
		context = view.getContext();
		// get the listview
		expListView = (ExpandableListView) view.findViewById(R.id.expand_list_view);
		
		// preparing list data
		prepareListData();
		
		listAdapter = new ExpandableListAdapter(view.getContext(), listDataHeader, listDataChild);
		
		// setting list adapter
		expListView.setAdapter(listAdapter);
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			public boolean onGroupClick(ExpandableListView parent, View v,
										int groupPosition, long id) {
			
//						Toast.makeText(view.getContext(),
//							"Group Clicked " + listDataHeader.get(groupPosition),
//							Toast.LENGTH_SHORT).show();
				
//						ImageView currentImage = (ImageView) findViewById(R.id.plan_image);
//						
				String floor = listDataHeader.get(groupPosition);
//						DeficiencyParser.loadFloorPlan(currentImage, floor);
//						((TouchImageView) currentImage).resetZoom();
				
				((CheckListsActivity) getActivity()).onFloorSelected(floor);
				
				return false;
			}
		});
		
		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override public void onGroupExpand(int groupPosition) {
			
//						Toast.makeText(getApplicationContext(),
//							listDataHeader.get(groupPosition) + " Expanded",
//							Toast.LENGTH_SHORT).show();
				
				
			}
		});
		
		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override public void onGroupCollapse(int groupPosition) {
			
//						Toast.makeText(getApplicationContext(),
//							listDataHeader.get(groupPosition) + " Collapsed",
//							Toast.LENGTH_SHORT).show();
				
			}
		});
		
		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition,
										long id) {
			
				
				String roomNo = listDataChild.get(listDataHeader.get(groupPosition)).get(
					childPosition);
				((CheckListsActivity) getActivity()).onRoomSelected(roomNo);
				
				
				return true;
			}
		});
		
		
		return view;
	}
	
	
	@Override public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
	}
	
	
	public void onAttach(Activity activity) {
	
		super.onAttach(activity);
		if (activity instanceof OnItemSelectedListener) {
			listener = (OnItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
											+ " must implemenet ExpandableListFragment"
											+ ".OnItemSelectedListener");
		}
	}
	
	@Override public void onDetach() {
	
		super.onDetach();
		listener = null;
	}
	
	
	
	/** ExpandableList setup. */
	private void prepareListData() {
	
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		
		// Adding child data
		listDataHeader.addAll(DeficiencyParser.getFloorIDs());
		
		// Adding child data
		List<String> floor2 = new ArrayList<String>();
		floor2.addAll(DeficiencyParser.getRoomIDs("Floor 2"));
		
		List<String> floor3 = new ArrayList<String>();
		floor3.addAll(DeficiencyParser.getRoomIDs("Floor 3"));
		
		List<String> ph = new ArrayList<String>();
		ph.addAll(DeficiencyParser.getRoomIDs("PH"));
		
		listDataChild.put(listDataHeader.get(0), floor2); // Header, Child data
		listDataChild.put(listDataHeader.get(1), floor3);
		listDataChild.put(listDataHeader.get(2), ph);
	}
	
	
	/** Used to communicate with activity. */
	public interface OnItemSelectedListener {
		
		public void onFloorSelected(String floorID);
		
		public void onRoomSelected(String roomNo);
	}
}
