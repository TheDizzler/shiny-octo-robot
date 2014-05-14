package vdindustries.checklists;


import org.w3c.dom.Element;

import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import vdindustries.planview.TouchImageView;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	
	Room					currentRoom;
	RelativeLayout			layout;
	ImageView				currentImage;
	private LayoutParams	planLayoutParams;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
	
		
		return inflater.inflate(R.layout.fragment_checklists_plan, container, false);
	}
	
	
	@Override public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		layout = (RelativeLayout) getActivity().findViewById(R.id.plan_fragment);
//		currentImage = new ImageView(getActivity());
		
		/* Load the first floor of building as default. This is neccessary
		 * or else floorplans will not load before a roomplan is loaded. */
		loadFloorPlan(((Element) DeficiencyParser.listFloorNodes.item(0)).getAttribute(Deficiency.FLOORID));
	}
	
	
	public void loadFloorPlan(String floorID) {
	
		if (currentRoom != null)
			currentRoom.removeViews();
		currentRoom = null;
		ImageView currentImage = (ImageView) getActivity().findViewById(R.id.plan_image);

		
//		planLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
//		currentImage.setLayoutParams(planLayoutParams);
//		currentImage.setScaleType(ScaleType.FIT_START);
		DeficiencyParser.loadFloorPlan(currentImage, floorID);
		currentImage.setLongClickable(false);
//		((TouchImageView) currentImage).resetZoom();
	}
	
	
	public void loadRoomPlan(String roomNo) {
	
		ImageView currentImage = (ImageView) getActivity().findViewById(R.id.plan_image);
		
		
		if (currentRoom != null)
			currentRoom.removeViews();
		
//		planLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		currentRoom = new Room(getActivity(), roomNo, currentImage, layout);
		
//		Toast.makeText(getActivity(),
//			"bitmap.getWidth() = " + DeficiencyParser.getImageWidth(roomNo)
//					+ ", bitmap.getHeight() = " + DeficiencyParser.getImageHeight(roomNo) +
//					"\nimageview.getWidth()" + currentImage.getWidth() +
//					"\nimageview.getHeight()" + currentImage.getHeight(), Toast.LENGTH_LONG).show();
//		((TouchImageView) currentImage).resetZoom();
	}
}
