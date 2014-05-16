package vdindustries.checklists;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import vdindustries.Qdap.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Code source:
 * https://code.google.com/p/android-wheel/
 */
public class DeficiencyWheel extends ActionBarActivity {
	
	// Scrolling flag
	private boolean	scrolling;
	
	String			id, object_str, item_str, verb_str, direction_str, location_str;
	int				x, y;
	
	public Context	context;
	
	
	@Override public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_def_wheel);
		
		context = this;
		
		
		final WheelView specificItem = (WheelView) findViewById(R.id.specificItem);
		specificItem.setVisibleItems(6);
		SpecificItemAdapter specItemAdapter = new SpecificItemAdapter(this);
		specificItem.setViewAdapter(specItemAdapter);
		
		
		
		final WheelView item = (WheelView) findViewById(R.id.item);
		item.setVisibleItems(6);
		ItemAdapter itemAdapter = new ItemAdapter(this);
		item.setViewAdapter(itemAdapter);
		
		
		final WheelView verb = (WheelView) findViewById(R.id.verb);
		verb.setVisibleItems(6);
		VerbAdapter verbAdapter = new VerbAdapter(this);
		verb.setViewAdapter(verbAdapter);
		
		
		
		final WheelView direction = (WheelView) findViewById(R.id.direction);
		direction.setVisibleItems(6);
		DirectionAdapter dirAdapter = new DirectionAdapter(this);
		direction.setViewAdapter(dirAdapter);
		
		
		final WheelView location = (WheelView) findViewById(R.id.location);
		location.setVisibleItems(6);
		LocationAdapter locationAdapter = new LocationAdapter(this);
		location.setViewAdapter(locationAdapter);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		TextView title = (TextView) findViewById(R.id.def_wheel_title_textview);
		TextView repID = (TextView) findViewById(R.id.reportid_def_wheel_textview);
		
		if (!extras.getBoolean("new")) {
			
			title.setText("Edit deficiency");
			
			repID.setText(extras.getString("id"));
			x = extras.getInt("x");
			y = extras.getInt("y");
			object_str = extras.getString("object");
			item_str = extras.getString("item");
			verb_str = extras.getString("verb");
			direction_str = extras.getString("direction");
			location_str = extras.getString("location");
			
			specificItem.setCurrentItem(specItemAdapter.getItemIndex(object_str));
			item.setCurrentItem(itemAdapter.getItemIndex(item_str));
			verb.setCurrentItem(verbAdapter.getItemIndex(verb_str));
			direction.setCurrentItem(dirAdapter.getItemIndex(direction_str));
			location.setCurrentItem(locationAdapter.getItemIndex(location_str));
		} else {
			
			title.setText("Create new deficiency");
			
			x = extras.getInt("x");
			y = extras.getInt("y");
			repID.setText("ProjectXXXXXXX\n" + x + ", " + y);
		}
		
		//gets the current item of the wheel for specific item
		specItemAdapter.getCurrentItem(specificItem.getCurrentItem());
		itemAdapter.getCurrentItem(item.getCurrentItem());
		verbAdapter.getCurrentItem(verb.getCurrentItem());
		dirAdapter.getCurrentItem(direction.getCurrentItem());
		locationAdapter.getCurrentItem(location.getCurrentItem());
		
		
		
		specificItem.addChangingListener(new OnWheelChangedListener() {
			
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			
// if (!scrolling) {
// updateCities(city, cities, newValue);
// }
			}
		});
		
		specificItem.addScrollingListener(new OnWheelScrollListener() {
			
			public void onScrollingStarted(WheelView wheel) {
			
				scrolling = true;
			}
			
			public void onScrollingFinished(WheelView wheel) {
			
				scrolling = false;
			}
		});
		
		specificItem.setCurrentItem(0);
	}
	
	
	public void save(View view) {
	
//		Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
		
	}
	
	/**
	* Adapter for SpecificItem
	*/
	private class SpecificItemAdapter extends AbstractWheelTextAdapter {
		
		private String	specificItem[]	= new String[]
										{ "", "Drywall", "Tub/Showerbase", "Towel bar",
												"Glass Door", "Slider Door", "Pocket Door",
												"Shower Niche", "Bench", "Door", "Closet",
												"Exterior Cladding", "Canopy", "Railing",
												"Shear-Wall", "Electrical Panel", "Communication Box",
												"Vanity", "Cabinet", "Desk Space" };
		
		
		/**
		* Constructor
		*/
		protected SpecificItemAdapter(Context context) {
		
			super(context, R.layout.specific_item_layout, NO_RESOURCE);
			setItemTextResource(R.id.specific_item_name);
			
		}
		
		public String getCurrentItem(int number) {
		
			String strselected_txt = specificItem[number];
			return strselected_txt;
		}
		
		public int getItemIndex(String item) {
		
			for (int i = 0; i < specificItem.length; ++i)
				if (specificItem[i].equalsIgnoreCase(item))
					return i;
			return 0;
		}
		
		@Override public View getItem(int index, View cachedView, ViewGroup parent) {
		
			View view = super.getItem(index, cachedView, parent);
			
			return view;
		}
		
		@Override public int getItemsCount() {
		
			return specificItem.length;
		}
		
		@Override protected CharSequence getItemText(int index) {
		
			return specificItem[index];
		}
	}
	
	/**
	* Adapter for Item
	*/
	private class ItemAdapter extends AbstractWheelTextAdapter {
		
		private String	itemList[]	= new String[]
									{ "", "Backing", "Joist", "Plywood", "Wall", "Wing Wall",
											"Stud", "Plate", "Dimensions", "Rough-Opening",
											"Poly-Liner", "Header", "Hold-Down", "Anchor", "Jamb", "Cripple" };
		
		/**
		* Constructor
		*/
		protected ItemAdapter(Context context) {
		
			super(context, R.layout.item_layout, NO_RESOURCE);
			
			setItemTextResource(R.id.item_name);
		}
		
		public String getCurrentItem(int number) {
		
			String strselected_txt = itemList[number];
			return strselected_txt;
		}
		
		public int getItemIndex(String item) {
		
			for (int i = 0; i < itemList.length; ++i)
				if (itemList[i].equalsIgnoreCase(item))
					return i;
			return 0;
		}
		
		@Override public View getItem(int index, View cachedView, ViewGroup parent) {
		
			View view = super.getItem(index, cachedView, parent);
			return view;
		}
		
		@Override public int getItemsCount() {
		
			return itemList.length;
		}
		
		@Override protected CharSequence getItemText(int index) {
		
			return itemList[index];
		}
	}
	
	/**
	* Adapter for Verb
	*/
	private class VerbAdapter extends AbstractWheelTextAdapter {
		
		private String	verb[]	= new String[] { "", "Missing", "Crowned", "Bowed", "Split",
										"Incorrect", "Not-Plumb", "Not-Square", "Not-Level",
										"Not-Installed", "Not-Aligned", "Not-Secured", "Off",
										"Undersized", "Oversized", "Reduce", "Enlarge", "Box-Out",
										"Replace", "Proud", "Extend", "Relocate", "Subtract",
										"Add", "Center", "To Be" };
		
		/**
		* Constructor
		*/
		protected VerbAdapter(Context context) {
		
			super(context, R.layout.verb_layout, NO_RESOURCE);
			
			setItemTextResource(R.id.verb_name);
		}
		
		public String getCurrentItem(int number) {
		
			String strselected_txt = verb[number];
			return strselected_txt;
		}
		
		public int getItemIndex(String item) {
		
			for (int i = 0; i < verb.length; ++i)
				if (verb[i].equalsIgnoreCase(item))
					return i;
			return 0;
		}
		
		@Override public View getItem(int index, View cachedView, ViewGroup parent) {
		
			View view = super.getItem(index, cachedView, parent);
			return view;
		}
		
		@Override public int getItemsCount() {
		
			return verb.length;
		}
		
		@Override protected CharSequence getItemText(int index) {
		
			return verb[index];
		}
	}
	
	/**
	* Adapter for direction
	*/
	private class DirectionAdapter extends AbstractWheelTextAdapter {
		
		private String	direction[]	= new String[] { "", "@", "Below", "Above", "Right of",
											"Left of", "Behind", "Infront", "To", "By" };
		
		/**
		* Constructor
		*/
		protected DirectionAdapter(Context context) {
		
			super(context, R.layout.direction_layout, NO_RESOURCE);
			
			setItemTextResource(R.id.direction_name);
		}
		
		public String getCurrentItem(int number) {
		
			String strselected_txt = direction[number];
			return strselected_txt;
			
		}
		
		public int getItemIndex(String item) {
		
			for (int i = 0; i < direction.length; ++i)
				if (direction[i].equalsIgnoreCase(item))
					return i;
			return 0;
		}
		
		@Override public View getItem(int index, View cachedView, ViewGroup parent) {
		
			View view = super.getItem(index, cachedView, parent);
			return view;
		}
		
		@Override public int getItemsCount() {
		
			return direction.length;
		}
		
		@Override protected CharSequence getItemText(int index) {
		
			return direction[index];
		}
	}
	
	/**
	* Adapter for Location
	*/
	private class LocationAdapter extends AbstractWheelTextAdapter {
		
		// Countries names
		private String	location[]	= new String[] { "", "Corner", "Floor", "Ceiling", "Kitchen", "Living",
											"Dining", "Bathroom", "Entry", "Laundry", "Closet",
											"Storage", "Den", "Opposite Side", "TV Center",
											"Window", "Suite Entry" };
		
		/**
		* Constructor
		*/
		protected LocationAdapter(Context context) {
		
			super(context, R.layout.location_layout, NO_RESOURCE);
			
			setItemTextResource(R.id.location_name);
		}
		
		public String getCurrentItem(int number) {
		
			String strselected_txt = location[number];
			return strselected_txt;
		}
		
		public int getItemIndex(String item) {
		
			for (int i = 0; i < location.length; ++i)
				if (location[i].equalsIgnoreCase(item))
					return i;
			return 0;
		}
		
		@Override public View getItem(int index, View cachedView, ViewGroup parent) {
		
			View view = super.getItem(index, cachedView, parent);
			return view;
		}
		
		@Override public int getItemsCount() {
		
			return location.length;
		}
		
		@Override protected CharSequence getItemText(int index) {
		
			return location[index];
		}
	}
	
}