package vdindustries.checklists;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Room extends ImageView {
	
	
	private int						FLAGHEIGHT;
	
	private ImageView				plan;
	private Bitmap					planBMP;
	
	private RelativeLayout			layout;
	LayoutParams					params;
	LayoutParams					buttonLayoutParams;
//	private LayoutParams			planLayoutParams;
	
	private List<Deficiency>		defs;
	private ArrayList<ImageButton>	defBtns;
	
	Bitmap							protoflag	= BitmapFactory.decodeResource(getResources(),
													R.drawable.flag_grey);
	
	private Context					context;
	public String					roomNo;
	final Vibrator					vibrator;
	
	int								lastClickX, lastClickY;
	
	private Canvas					canvas;
	
	
	/** Constructor for Checklists view implementation. */
	public Room(Context context, String roomNo, ImageView img, RelativeLayout rl) {
	
		super(context);
		
		this.context = context;
		this.plan = img;
		this.roomNo = roomNo;
		
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		buttonLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout = rl;
		FLAGHEIGHT = getResources().getDrawable(R.drawable.flag_blue).getIntrinsicHeight();
		
		defs = DeficiencyParser.getDefsByRoom(roomNo);
		showRoom(roomNo);
	}
	
	
	/** Constructor for Reports view implementation. */
	public Room(Context context, String roomNo, ImageView img) {
	
		super(context);
		
		this.context = context;
		this.plan = img;
		this.roomNo = roomNo;
		
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		FLAGHEIGHT = getResources().getDrawable(R.drawable.flag_blue).getIntrinsicHeight();
		
		showRoomReports(roomNo);
	}
	
	
	private void showRoom(final String roomNo) {
	
		DeficiencyParser.loadRoomPlan(plan, roomNo);
		plan.setX(getResources().getDimension(R.dimen.plan_horizontal_margin));
		plan.setY(+getResources().getDimension(R.dimen.plan_vertical_margin));
		
//		plan.setLongClickable(true);
//		plan.setOnTouchListener(new LocationGetter(this));
//		plan.setOnLongClickListener(new LongListener(this));
//		showDeficiencies(roomNo, "Framing");
	}
	
	
	private void showRoomReports(String roomNo) {
	
		DeficiencyParser.loadRoomPlanBMP(planBMP, roomNo);
//		planBMP.setX(getResources().getDimension(R.dimen.plan_horizontal_margin));
//		planBMP.setY(getResources().getDimension(R.dimen.plan_vertical_margin));
		
		Bitmap immutable = null;
		planBMP = convertToMutable(context, immutable);
		canvas = new Canvas(planBMP);
//		showDeficiency();
		plan.setImageBitmap(planBMP);
	}
	
	
	public void showDeficiency(Deficiency def) {
	
		Bitmap flag;
		if (def.completed)
			flag = BitmapFactory.decodeResource(getResources(),
				R.drawable.flag_grey);
		else if (def.priority)
			flag = BitmapFactory.decodeResource(getResources(),
				R.drawable.flag_red);
		else
			flag = BitmapFactory.decodeResource(getResources(),
				R.drawable.flag_blue);
		
		int x = def.X; // other transform equations may be needed
		int y = def.Y - flag.getHeight();
		canvas.drawBitmap(flag, x, y, null);
	}
	
	
	/** Display room deficiencise by trade. */
	public void showDeficiencies(String roomNo, String tradeToShow) {
	
		defBtns = new ArrayList<ImageButton>();
		
		defs = DeficiencyParser.getDefsByRoom(roomNo);
		
		for (int i = 0; i < defs.size(); ++i) {
			
			Deficiency def = defs.get(i);
			if (!def.trade.equalsIgnoreCase(tradeToShow))
				//break;
				continue;
			ImageButton btn = new ImageButton(context);
			
			if (def.completed)
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_grey));
			else if (def.priority)
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_red));
			else
				btn.setImageDrawable(getResources().getDrawable(R.drawable.flag_blue));
			
			
//			btn.setBackground(getResources().getDrawable(R.drawable.flag_bg));
			btn.setOnClickListener(new FlagListener(def, vibrator)); // setLongClickable?
			btn.setLayoutParams(buttonLayoutParams);
			btn.setId(i);
			btn.setX(def.X - 18 + getResources().getDimension(R.dimen.plan_horizontal_margin));
			btn.setY(def.Y - FLAGHEIGHT - 9 + getResources().getDimension(
				R.dimen.plan_vertical_margin));
			layout.addView(btn);
			defBtns.add(btn);
		}
	}
	
	/** Removes flags from imageview. */
	public void removeViews() {
	
		if (defBtns != null)
			for (ImageButton btn : defBtns)
				((ViewManager) btn.getParent()).removeView(btn);
	}
	
	
	/** Used to communicate with activity. */
	public interface OnEditDeficiency {
		
		public void onFlagClick(Deficiency def);
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


/** Click listener for deficiency flags. */
class FlagListener implements OnClickListener {
	
	private Deficiency	def;
	private Vibrator	vibrator;
	
	public FlagListener(Deficiency def, Vibrator vib) {
	
		this.def = def;
		this.vibrator = vib;
	}
	
	
	@Override public void onClick(View v) {
	
		vibrator.vibrate(500);
//		Toast.makeText(v.getContext(), def.toString(), Toast.LENGTH_SHORT).show();
		((CheckListsActivity) v.getContext()).onFlagClick(def);
	}
}
