package vdindustries.planview;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vdindustries.masterflow.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class NewPlans extends ActionBarActivity {
	
	private int						FLAGHEIGHT;
	
	private NewPlans				context;
	private ImageView				plan;
	
	
	private RelativeLayout			layout;
	LayoutParams					params;
	LayoutParams					buttonLayout;
	private LayoutParams			planLayout;
	
	private List<Deficiency>		defs;
	private ArrayList<ImageButton>	defBtns;
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newplanview);
		
		FLAGHEIGHT = getResources().getDrawable(R.drawable.flag_blue).getIntrinsicHeight();
		context = this;
		
		layout = (RelativeLayout) findViewById(R.id.rl);
		buttonLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		planLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		createRoomButtons("Floor 3");	
	}
	
	
	private void showDeficiencies(String roomNo) {
	
		defBtns = new ArrayList<ImageButton>();
		
		defs = DeficiencyParser.getDefsByRoom(roomNo);
		
		for (int i = 0; i < defs.size(); ++i) {
			
			Deficiency def = defs.get(i);
			ImageButton btn = new ImageButton(this);
			
			if (def.completed)
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_grey));
			else if (def.priority)
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_red));
			else
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_blue));
			
			
			btn.setLayoutParams(buttonLayout);
			btn.setId(i);
			
			btn.setX(def.X);
			btn.setY(def.Y - FLAGHEIGHT);
			layout.addView(btn);
			defBtns.add(btn);
		}
	}
	
	
	private void showRoom(String roomNo) {
	
		plan = new TouchImageView(this);
		plan.setLayoutParams(planLayout);
		DeficiencyParser.loadRoomPlan(plan, roomNo);
		layout.addView(plan);
		Toast.makeText(getApplicationContext(),
			plan.getWidth() + ", " + plan.getHeight(), Toast.LENGTH_SHORT).show();
		showDeficiencies(roomNo);
	}
	
	
	
	/** Creates a button for each room on a floor. */
	private void createRoomButtons(String floorID) {
	
		
//		roomButtons = new ArrayList<Button>();
		NodeList roomNodes = DeficiencyParser.getRoomsByFloor(floorID);
		
		for (int i = 0; i < roomNodes.getLength(); ++i) {
			
			String roomNo = ((Element) roomNodes.item(i)).getAttribute(Deficiency.ROOMNO);
			Button btn = new Button(this);
			btn.setText(roomNo);
			setRoomListener(btn, roomNo);
			
			
			layout.addView(btn);
//			roomButtons.add(btn);
		}
	}
	
	/** ButtonListener for room buttons */
	private void setRoomListener(Button btn, final String roomNo) {
	
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				showRoom(roomNo);
			}
		});
	}
}