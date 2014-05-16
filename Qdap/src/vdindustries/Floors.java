package vdindustries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.content.DeficiencyParser;
import vdindustries.masterflow.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Floors extends ActionBarActivity {
	
	ExpandableListAdapter			listAdapter;
	ExpandableListView				expListView;
	List<String>					listDataHeader;
	HashMap<String, List<String>>	listDataChild;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_floors);
		
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.list);
//		expListView = new ExpandableListView(this);
		
		// preparing list data
		prepareListData();
		
		
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		
		// setting list adapter
		expListView.setAdapter(listAdapter);
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override public boolean onGroupClick(ExpandableListView parent, View v,
													int groupPosition, long id) {
			
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
			
				if (listDataHeader.get(groupPosition).equals("Floor 2")) {
					ImageView img = (ImageView) findViewById(R.id.imageView1);
					DeficiencyParser.loadFloorPlan(img, "Floor 2");
				}
				if (listDataHeader.get(groupPosition).equals("Floor 3")) {
					ImageView img = (ImageView) findViewById(R.id.imageView1);
					DeficiencyParser.loadFloorPlan(img, "Floor 3");
				}
				if (listDataHeader.get(groupPosition).equals("PH")) {
					ImageView img = (ImageView) findViewById(R.id.imageView1);
					DeficiencyParser.loadFloorPlan(img, "PH");
				}
				
				
				return false;
			}
		});
		
		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override public void onGroupExpand(int groupPosition) {
			
				Toast.makeText(getApplicationContext(),
					listDataHeader.get(groupPosition) + " Expanded",
					Toast.LENGTH_SHORT).show();
			}
		});
		
		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override public void onGroupCollapse(int groupPosition) {
			
				Toast.makeText(getApplicationContext(),
					listDataHeader.get(groupPosition) + " Collapsed",
					Toast.LENGTH_SHORT).show();
				
			}
		});
		
		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override public boolean onChildClick(ExpandableListView parent, View v,
													int groupPosition, int childPosition, long id) {
			
				Toast.makeText(
					getApplicationContext(),
					listDataHeader.get(groupPosition)
							+ " : "
							+ listDataChild.get(
								listDataHeader.get(groupPosition)).get(
								childPosition), Toast.LENGTH_SHORT)
					.show();
				
				if (listDataChild.get(
					listDataHeader.get(groupPosition)).get(
					childPosition).equals("The Godfather")) {
					Intent intent = new Intent(Floors.this, Checklist.class);
					Floors.this.startActivity(intent);
				}
				return false;
			}
		});
	}
	
	/* Preparing the list data */
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
}