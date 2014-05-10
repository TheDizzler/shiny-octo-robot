package vdindustries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.content.DeficiencyParser;
import vdindustries.masterflow.R;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Floors extends ActionBarActivity {
	
	ExpandableListAdapter			listAdapter;
	ExpandableListView				expListView;
	List<String>					listDataHeader;
	HashMap<String, List<String>>	listDataChild;
	
	LinearLayout					layout;
	LinearLayout.LayoutParams		params;
	
	ImageView						currentImage;
	Room							currentRoom;
	
	Context							context;
	
	ImageButton						btn;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_floors);
		context = this;
		
		layout = (LinearLayout) findViewById(R.id.room_layout);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
		
		
		
		
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.list);
		
		// preparing list data
		prepareListData();
		
		currentImage = (ImageView) findViewById(R.id.imageView1);
		
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		
		// setting list adapter
		expListView.setAdapter(listAdapter);
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override public boolean onGroupClick(ExpandableListView parent, View v,
													int groupPosition, long id) {
			
				Toast.makeText(getApplicationContext(),
					"Group Clicked " + listDataHeader.get(groupPosition),
					Toast.LENGTH_SHORT).show();
				
				currentRoom = null;
				currentImage = (ImageView) findViewById(R.id.imageView1);
				DeficiencyParser.loadFloorPlan(currentImage,
					listDataHeader.get(groupPosition));
				
				
				
				return false;
			}
		});
		
		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override public void onGroupExpand(int groupPosition) {
			
//				Toast.makeText(getApplicationContext(),
//					listDataHeader.get(groupPosition) + " Expanded",
//					Toast.LENGTH_SHORT).show();
			}
		});
		
		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override public void onGroupCollapse(int groupPosition) {
			
//				Toast.makeText(getApplicationContext(),
//					listDataHeader.get(groupPosition) + " Collapsed",
//					Toast.LENGTH_SHORT).show();
//				
			}
		});
		
		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition,
										long id) {
			
				
				currentRoom = new Room(context,
					listDataChild.get(listDataHeader.get(groupPosition)).
						get(childPosition), currentImage);
				
//				currentRoom.placeDeficiencies();
//				LayoutParams lp = v.getLayoutParams();
				btn = new ImageButton(context);
				btn.setImageResource(R.drawable.flag_red);
				btn.setLayoutParams(params);
				btn.setX(200);
				btn.setY(200);
				layout.addView(btn);
				
				
				return false;
			}
		});
	}
	
//	@Override public void onBackPressed() {
//	
//		
//		if (currentRoom != null) {
//			currentRoom = null;
//			currentImage = null;
//		} else
//			super.onBackPressed();
//			
//	}
	
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
