package vdindustries.checklists;

import vdindustries.Qdap.R;
import vdindustries.content.TouchImageView;
import vdindustries.reportsview.TradeListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CheckListsActivity extends ActionBarActivity implements ExpandableListFragment.OnItemSelectedListener {
	
	
	Room	currentRoom;
	
	Context	context;
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklists);
		context = this;
		
//		((ExpandableListFragment) getSupportFragmentManager().
//				findFragmentById(R.id.expand_list_fragment)).setActivateOnItemClick(true);
		
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
	
	
	
}
