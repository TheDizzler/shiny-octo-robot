package vdindustries.checklists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;


public class ExpandableListFragment extends Fragment {
	
	ExpandableListAdapter			listAdapter;
	ExpandableListView				expListView;
	List<String>					listDataHeader;
	HashMap<String, List<String>>	listDataChild;
	
	private Context					context;
	
	/** Used to communicate with activity. */
	private OnItemSelectedListener	listener;
	
	
	
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
				
				String floor = listDataHeader.get(groupPosition);
				
				((CheckListsActivity) getActivity()).onFloorSelected(floor);
				
				return false;
			}
		});
		
		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override public void onGroupExpand(int groupPosition) {
			
				
			}
		});
		
		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override public void onGroupCollapse(int groupPosition) {
			
				
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
		NodeList floors = DeficiencyParser.listFloorNodes;
		
		for (int i = 0; i < floors.getLength(); ++i) {
			
			String floorID = ((Element) floors.item(i))
				.getAttribute(Deficiency.FLOORID);
			
			List<String> roomList = new ArrayList<String>();
			roomList.addAll(DeficiencyParser.getRoomIDs(floorID));
			
			listDataChild.put(listDataHeader.get(i), roomList); // Header, Child data
		}
	}
	
	
	/** Used to communicate with activity. */
	public interface OnItemSelectedListener {
		
		public void onFloorSelected(String floorID);
		
		public void onRoomSelected(String roomNo);
	}
}