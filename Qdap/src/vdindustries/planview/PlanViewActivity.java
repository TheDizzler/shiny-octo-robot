package vdindustries.planview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import vdindustries.masterflow.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class PlanViewActivity extends ActionBarActivity {
	
	List<Button>		buttons;
	/** Useful methods: resetZoom(), setZoom(float scale) */
	ImageView			image;
	Canvas				canvas;
	List<Deficiency>	defs;
	
	int					finalHeight, finalWidth;
	
	Bitmap				plan;
	private Context		context;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planview);
		
		context = this;
		image = (ImageView) findViewById(R.id.image);
		
//		createFloorButtons();
		createRoomButtons("Floor 2");
//		showRoom("201");
	}
	
	
	
	private void showRoom(String room) {
	
		defs = new ArrayList<Deficiency>();
		
//		DeficiencyParser.loadRoomPlan(image, room);
		Bitmap immutable = null;
		String file = DeficiencyParser.getRoomImageFile(room);
		try {
			immutable = DeficiencyParser.loadBitmapFromAsset(file);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		plan = convertToMutable(this, immutable);
		canvas = new Canvas(plan);
		defs = DeficiencyParser.getDefsByRoom(room);
		
		for (int i = 0; i < defs.size(); ++i) {
			
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			
			layout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			Bitmap flag = BitmapFactory.decodeResource(getResources(),
				R.drawable.flag_red);
			
			canvas.drawBitmap(flag, defs.get(i).X, defs.get(i).Y, null);
//			layout.addView(flag);
//			setContentView(layout);
		}
		
//		BitmapDrawable dr = new BitmapDrawable(bmOverlay);
//        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
		
		image.setImageBitmap(plan);
		
	}
	
	
	/** Creates a button for each floor in XML file.
	 * Must add android:id="@+id/linear_layout" to activity XML. */
	private void createFloorButtons() {
	
		LinearLayout l_layout = (LinearLayout) findViewById(R.id.linear_layout);
		l_layout.setOrientation(LinearLayout.VERTICAL);
		
		buttons = new ArrayList<Button>();
		NodeList floorNodes = DeficiencyParser.listFloorNodes;
		
		for (int i = 0; i < floorNodes.getLength(); ++i) {
			
			String btntext = ((Element) floorNodes.item(i)).getAttribute(Deficiency.FLOORID);
			Button btn = new Button(this);
			btn.setText(btntext);
			setFloorListener(btn, btntext);
			
			
			l_layout.addView(btn);
			buttons.add(btn);
		}
	}
	
	/** Creates a button for each room on a floor. */
	private void createRoomButtons(String floorID) {
	
		LinearLayout l_layout = (LinearLayout) findViewById(R.id.linear_layout);
		l_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		buttons = new ArrayList<Button>();
		NodeList roomNodes = DeficiencyParser.getRoomsByFloor(floorID);
		
		for (int i = 0; i < roomNodes.getLength(); ++i) {
			
			String roomNo = ((Element) roomNodes.item(i)).getAttribute(Deficiency.ROOMNO);
			Button btn = new Button(this);
			btn.setText(roomNo);
			setRoomListener(btn, roomNo);
			
			
			l_layout.addView(btn);
			buttons.add(btn);
		}
	}
	
	/** ButtonListener for floor buttons. */
	private void setFloorListener(Button btn, final String btntext) {
	
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				DeficiencyParser.loadFloorPlan(image, "Floor 2");
			}
		});
	}
	
	/** ButtonListener for room buttons */
	private void setRoomListener(Button btn, final String roomNo) {
	
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				showRoom(roomNo);
			}
		});
	}
	
	
	
	/** might use the internal storage in some cases, creating temporary file
	 * that will be deleted as soon as it isn't finished */
	public static Bitmap convertToMutable(final Context context, final Bitmap imgIn) {
	
		final int width = imgIn.getWidth(), height = imgIn.getHeight();
		final Config type = imgIn.getConfig();
		File outputFile = null;
		final File outputDir = context.getCacheDir();
		try {
			outputFile = File.createTempFile(Long.toString(System.currentTimeMillis()), null,
				outputDir);
			outputFile.deleteOnExit();
			final RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");
			final FileChannel channel = randomAccessFile.getChannel();
			final MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0,
				imgIn.getRowBytes() * height);
			imgIn.copyPixelsToBuffer(map);
			imgIn.recycle();
			final Bitmap result = Bitmap.createBitmap(width, height, type);
			map.position(0);
			result.copyPixelsFromBuffer(map);
			channel.close();
			randomAccessFile.close();
			outputFile.delete();
			return result;
		} catch (final Exception e) {} finally {
			if (outputFile != null)
				outputFile.delete();
		}
		return null;
	}
	
}