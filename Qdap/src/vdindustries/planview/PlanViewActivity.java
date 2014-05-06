package vdindustries.planview;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vdindustries.content.DeficiencyParser;
import vdindustries.masterflow.R;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class PlanViewActivity extends ActionBarActivity {
	
	List<Button> floorbtns;
	ImageView	image;
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planview);
		
		image = (ImageView) findViewById(R.id.image);
		createFloorButtons();
		
	}
	
	/** Creates a button for each floor in XML file.
	 * Must add android:id="@+id/linear_layout" to activity XML.*/
	private void createFloorButtons() {
	
		LinearLayout l_layout = (LinearLayout) findViewById(R.id.linear_layout); 
		l_layout.setOrientation(LinearLayout.VERTICAL);
		
		floorbtns= new ArrayList<Button>();
		NodeList floorNodes = DeficiencyParser.listFloorNodes;
		
		for(int i = 0; i < floorNodes.getLength(); ++i) {
			
			String btntext = ((Element)floorNodes.item(i)).getAttribute("floorID");
			Button btn = new Button(this);
			btn.setText(btntext);
			setListener(btn, btntext);
			
			
			l_layout.addView(btn);
			floorbtns.add(btn);
		}
	}


	private void setListener(Button btn, final String btntext) {
	
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				loadFloorPlan(btntext);
			}
	});
		
	}

	protected void loadFloorPlan(String floorID) {
	
		String file = DeficiencyParser.getFloorImageFile(floorID);
		try {
			image.setImageDrawable(loadImageFromAsset(file));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

	/** TODO */
	private Bitmap loadImageFromDirectory() {
	
		
//				try {
//				Bitmap bmap = BitmapFactory.decodeFile();
//				} catch (IOException ex) {
//					throw ex;
//				}
		return null;
		
	}
	
	/** Loads an image from the assets directory. */
	protected Drawable loadImageFromAsset(String file) throws IOException {
	
		Drawable d;
		
		InputStream is = getAssets().open(file);
		d = Drawable.createFromStream(is, null);
		
		return d;
	}
}