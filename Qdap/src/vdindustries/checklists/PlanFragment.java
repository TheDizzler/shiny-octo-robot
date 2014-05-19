package vdindustries.checklists;


import org.w3c.dom.Element;

import vdindustries.Qdap.R;
import vdindustries.checklists.ExpandableListFragment.OnItemSelectedListener;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import vdindustries.content.TouchImageView;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

/** Fragment lifecycle:
 * onAttach()
 * onCreate()
 * onCreateView()
 * onActivityCreated() - Activity and fragment instance
 * 		have been created as well as thier view hierarchy. At this
 * 		point, view can be accessed with the findViewById() method.
 * onResume()
 * onPause()
 * onStop()
 * @author T-Dawg */
public class PlanFragment extends Fragment {
	
	Room							currentRoom;
	RelativeLayout					layout;
	ImageView						currentImage;
//	private LayoutParams	planLayoutParams;
	
	/** Used to communicate with activity. */
	private OnItemSelectedListener	listener;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
	
		
		return inflater.inflate(R.layout.fragment_checklists_plan, container, false);
	}
	
	
	@Override public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		layout = (RelativeLayout) getActivity().findViewById(R.id.plan_fragment);
		
		if (DeficiencyParser.listFloorNodes.item(0) != null)
			/* Load the first floor of building as default. This is neccessary
			 * or else floorplans will not load before a roomplan is loaded. */
			loadFloorPlan(((Element) DeficiencyParser.listFloorNodes.item(0)).getAttribute(Deficiency.FLOORID));
	}
	
	
	public void loadFloorPlan(String floorID) {
	
		ImageView currentImage = (ImageView) getActivity().findViewById(R.id.plan_image);
		
		if (currentRoom != null)
			currentRoom.removeViews();
		currentRoom = null;
		
		
		DeficiencyParser.loadFloorPlan(currentImage, floorID);
		currentImage.setLongClickable(false);
	}
	
	
	public void loadRoomPlan(String roomNo) {
	
		ImageView currentImage = (ImageView) getActivity().findViewById(R.id.plan_image);
		
		
		if (currentRoom != null)
			currentRoom.removeViews();
		
		
		currentRoom = new Room(getActivity(), roomNo, currentImage, layout);
		
		currentImage.setLongClickable(true);
		currentImage.setOnTouchListener(new LocationGetter(currentRoom));
		currentImage.setOnLongClickListener(new LongListener(currentRoom));
	}
	
	
	public void loadDeficiencies(String tradeSelected) {
	
//		ImageView currentImage = (ImageView) getActivity().findViewById(R.id.plan_image);
		
		if (currentRoom != null) {
			currentRoom.removeViews();
			
			currentRoom.showDeficiencies(currentRoom.roomNo, tradeSelected);
		}
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
	}
	
	/** Used to communicate with activity. */
	public interface OnCreateDeficiency {
		
		public void onLongClick(int x, int y);
	}
}



class LocationGetter implements OnTouchListener {
	
	Room	room;
	
	
	public LocationGetter(Room rm) {
	
		this.room = rm;
	}
	
	
	@Override public boolean onTouch(View v, MotionEvent event) {
	
		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				room.lastClickX = (int) event.getX();
				room.lastClickY = (int) event.getY();
				break;
			}
		}
		return false;
	}
}


class LongListener implements OnLongClickListener {
	
	Room	room;
	
	public LongListener(Room rm) {
	
		this.room = rm;
	}
	
	@Override public boolean onLongClick(View v) {
	
		room.vibrator.vibrate(500);
		
//		Toast.makeText(v.getContext(),
//			"Oh HI " + room.roomNo + "\nYou touched me at " +
//					room.lastClickX + ", " + room.lastClickY, Toast.LENGTH_SHORT).show();
		
		((CheckListsActivity) v.getContext()).onLongClick(room.lastClickX, room.lastClickY);
		return false;
	}
}
