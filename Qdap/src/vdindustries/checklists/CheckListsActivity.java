package vdindustries.checklists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
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
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;

public class CheckListsActivity extends ActionBarActivity implements ExpandableListFragment.OnItemSelectedListener,
															TradesChoiceFragment.OnTradeSelectedListener,
															PlanFragment.OnCreateDeficiency,
															Room.OnEditDeficiency {
	
	Context					context;
	private static String	tradeSelected;
	
	
	
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
		
		if (tradeSelected != null)
			fragment.loadDeficiencies(tradeSelected);
	}
	
	
	@Override public void onTradeSelected(String tradeSelected) {
	
		PlanFragment fragment = (PlanFragment) getFragmentManager().findFragmentById(
			R.id.plan_fragment);
		
		this.tradeSelected = tradeSelected;
		fragment.loadDeficiencies(tradeSelected);
		
	}
	
	/** Show the deficiency wheel and c reate a new deficiency. */
	@Override public void onLongClick(int x, int y) {
	
		if (tradeSelected == null || tradeSelected.isEmpty())
			Toast.makeText(context, "Please select a trade before creating defiencies.",
				Toast.LENGTH_LONG).show();
		else {
			Intent intent = new Intent(context, DeficiencyWheel.class);
			intent.putExtra("new", true);
			intent.putExtra("x", x);
			intent.putExtra("y", y);
			startActivity(intent);
		}
	}
	
	
	@Override public void onFlagClick(Deficiency def) {
	
		Intent intent = new Intent(context, DeficiencyWheel.class);
		
		intent.putExtra("new", false);
		
		intent.putExtra("id", def.reportID);
		intent.putExtra("x", def.X);
		intent.putExtra("y", def.Y);
		intent.putExtra("object", def.object);
		intent.putExtra("item", def.item);
		intent.putExtra("verb", def.verb);
		intent.putExtra("direction", def.direction);
		intent.putExtra("location", def.location);
		startActivity(intent);
		
	}
}