package qdap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.masterflow.R;
import vdindustries.masterflow.content.DeficiencyParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Floors extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	  ArrayList<String> tradesList;
	protected float x;
	protected float y;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_floors);
//		listView1
//		http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
		   // Get the reference of ListViewAnimals
        ListView trades=(ListView)findViewById(R.id.listView1);
        View roughins = getLayoutInflater().inflate(R.layout.roughins, null);
        trades.addHeaderView(roughins);
         tradesList = new ArrayList<String>();
         tradesList.add("Framing");
         tradesList.add("Mechanical");
         tradesList.add("Electrical");
         tradesList.add("Security");
         tradesList.add("HVAC");
         tradesList.add("Insulation");
         
//         View finishes = getLayoutInflater().inflate(R.layout.finishes, null);
         tradesList.add("FINISHES");    
         tradesList.add("Drywall");   
         tradesList.add("Paint");
         tradesList.add("Mechanical");
         tradesList.add("Electrical");
         tradesList.add("Flooring");
         tradesList.add("Cabinets");
         tradesList.add("Countertops");
         tradesList.add("Finish Carp.");
  
         
         ArrayAdapter<String> arrayAdapter =      
                 new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tradesList);
                 // Set The Adapter
                 trades.setAdapter(arrayAdapter); 
                 
                 // register onClickListener to handle click events on each item
                 trades.setOnItemClickListener(new OnItemClickListener()
                    {
                             // argument position gives the index of item which is clicked
                            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
                            {
                                
                                   
                                 }
                    });
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.list);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
			
					ImageView img= (ImageView) findViewById(R.id.imageView1);
				    DeficiencyParser.loadFloorPlan(img, listDataHeader.get(groupPosition));	
			
	
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
		
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
	
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				
				
				if (listDataChild.get(
						listDataHeader.get(groupPosition)).get(
						childPosition).equals("The Godfather")) {
					Intent intent = new Intent(Floors.this, Checklist.class);
					Floors.this.startActivity(intent);
				}
				return false;
			}
		});
		ImageView imageView= (ImageView) findViewById(R.id.imageView1);
		
		
		imageView.setOnTouchListener(new OnTouchListener()
	      {

			@Override
			public boolean onTouch(View arg, MotionEvent event) {
				// TODO Auto-generated method stub
			     x = event.getX(); 
			     y = event.getY();
				return false;
			}
			
	
	      });
	    imageView.setOnLongClickListener(new OnLongClickListener() {

	        @Override
	        public boolean onLongClick(View v) {
	   
	            return false;
	        }
	    });
	    
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.addAll(DeficiencyParser.getFloors());

		// Adding child data
		List<String> floor2 = new ArrayList<String>();
		floor2.addAll(DeficiencyParser.getRooms("Floor 2"));

		List<String> floor3 = new ArrayList<String>();
		floor3.addAll(DeficiencyParser.getRooms("Floor 3"));

		List<String> ph = new ArrayList<String>();
		ph.addAll(DeficiencyParser.getRooms("PH"));

		listDataChild.put(listDataHeader.get(0), floor2); // Header, Child data
		listDataChild.put(listDataHeader.get(1), floor3);
		listDataChild.put(listDataHeader.get(2), ph);
	}
	
	public void deficiency(View view) {
	    Intent intent = new Intent(this, Deficiency.class);
	    startActivity(intent);
	}
}
