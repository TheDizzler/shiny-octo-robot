package vdindustries.checklists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.Qdap.R;
import vdindustries.content.DeficiencyParser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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

public class CheckListsActivity extends ActionBarActivity implements ExpandableListFragment.OnItemSelectedListener,
															TradesChoiceFragment.OnTradeSelectedListener {
	
	
	Room	currentRoom;
	
	Context	context;
	
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklists);
		context = this;
		
		
	}
	
	
	@Override public void onFloorSelected(String floorID) {
	
		PlanFragment fragment = (PlanFragment) getFragmentManager().findFragmentById(
			R.id.plan_fragment);
		
		fragment.loadFloorPlan(floorID);
	}
	
	
	@Override public void onRoomSelected(String roomNo) {
	
		
		PlanFragment fragment = (PlanFragment) getFragmentManager().findFragmentById(
			R.id.plan_fragment);
		
		fragment.loadRoomPlan(roomNo);
		
	}
	
	
	@Override public void onTradeSelected(String tradeSelected) {
	
		PlanFragment fragment = (PlanFragment) getFragmentManager().findFragmentById(
			R.id.plan_fragment);
		
		fragment.loadDeficiencies(tradeSelected);
		
	}
	
	
	
	public void deficiencyWheel(View view) {
	
		Intent intent = new Intent(context, DeficiencyWheel.class);
		startActivity(intent);
	}
}
