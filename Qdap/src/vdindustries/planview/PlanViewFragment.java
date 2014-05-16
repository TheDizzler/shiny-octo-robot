package vdindustries.planview;

import android.R;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class PlanViewFragment extends Fragment {
	
	private Integer			itemData;
	private Bitmap			plan;
	public ProgressDialog	progressDialog;
	private ImageView		imageView;
	
	
	public static PlanViewFragment newInstance() {
	
		return new PlanViewFragment();
	}
	
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
	}
	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//								Bundle savedInstanceState) {
//	
//		View root = inflater.inflate(R.layout.imageview, container, false);
//		imageView = (ImageView) root.findViewById(R.id.imageView1);
//		setImageInViewPager();
//		return root;
//	}
	
	
	public void setImageList(Integer integer) {
	
		this.itemData = integer;
	}
	
	
	public void setImageInViewPager() {
	
		try {
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			plan = BitmapFactory.decodeResource(getResources(), itemData, options);
			if (options.outWidth > 3000 || options.outHeight > 2000) {
				options.inSampleSize = 4;
			} else if (options.outWidth > 2000 || options.outHeight > 1500) {
				options.inSampleSize = 3;
			} else if (options.outWidth > 1000 || options.outHeight > 1000) {
				options.inSampleSize = 2;
			}
			
			options.inJustDecodeBounds = false;
			plan = BitmapFactory.decodeResource(getResources(), itemData, options);
			
			if (plan != null) {
				try {
					if (imageView != null)
						imageView.setImageBitmap(plan);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			System.gc();
		}
	}
	
	
	public void onDestroyView() {
	
		super.onDestroyView();
		
		if (plan != null) {
			plan.recycle();
			plan = null;
		}
	}
}