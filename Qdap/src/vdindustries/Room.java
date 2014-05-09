package vdindustries;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import vdindustries.masterflow.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Room extends ImageView {

	private ArrayList<Button>	flagBtns;
	ImageView					image;
	Bitmap						plan;
	Canvas						canvas;
	List<Deficiency>			defs;
	
	private Context context;
	
	
	public Room(Context context, String roomNo, ImageView img) {
	
		super(context);
		this.context = context;
		this.image = img;
		showRoom(roomNo);
	}
	
	
	private void showRoom(String roomNo) {
		
		defs = new ArrayList<Deficiency>();
		flagBtns = new ArrayList<Button>();
		
		Bitmap immutable = null;
		String file = DeficiencyParser.getRoomImageFile(roomNo);
		try {
			immutable = DeficiencyParser.loadBitmapFromAsset(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		plan = convertToMutable(context, immutable);
		canvas = new Canvas(plan);
		defs = DeficiencyParser.getDefsByRoom(roomNo);
		
		for (int i = 0; i < defs.size(); ++i) {
			
			Deficiency def = defs.get(i);
			if (def.trade.equalsIgnoreCase("Framing")) {
				LinearLayout layout = new LinearLayout(context);
				layout.setOrientation(LinearLayout.VERTICAL);
				
				layout.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				
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
				
				
				int x = defs.get(i).X;
				int y = defs.get(i).Y;
				canvas.drawBitmap(flag, x, y - flag.getHeight(), null);
				createFlagButton(roomNo, x, y);
			}
		}
		
		
		image.setImageBitmap(plan);
		
	}
	
	/** Creates an invisible button over a flag. 
	 * @param roomNo */
	private void createFlagButton(final String roomNo, int x, int y) {
	
		Button btn = new Button(context);
		btn.setVisibility(View.VISIBLE);
		btn.setBackgroundColor(Color.TRANSPARENT);
		btn.setX(x);
		btn.setY(y);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override public void onClick(View v) {
			
				Toast.makeText(context, roomNo, Toast.LENGTH_SHORT)
					.show();
			}
		});
		
		flagBtns.add(btn);
		
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
