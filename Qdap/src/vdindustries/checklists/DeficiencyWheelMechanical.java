package vdindustries.checklists;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Wheel project code source: https://code.google.com/p/android-wheel/
 */
public class DeficiencyWheelMechanical extends ActionBarActivity {

	Deficiency currentDef;
	String id, object_str, item_str, verb_str, direction_str, location_str, trade_def, room_def, floor_def;
	int x, y;

	public Context context;

	private boolean priority_def;
	private WheelView item, location, direction, verb, specificItem;

	SpecificItemAdapter specItemAdapter;
	ItemAdapter itemAdapter;
	LocationAdapter locationAdapter;
	DirectionAdapter dirAdapter;
	VerbAdapter verbAdapter;

	private boolean newDef;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_def_wheel);

		context = this;

		specificItem = (WheelView) findViewById(R.id.specificItem);
		specificItem.setVisibleItems(6);
		specItemAdapter = new SpecificItemAdapter(this);
		specificItem.setViewAdapter(specItemAdapter);

		item = (WheelView) findViewById(R.id.item);
		item.setVisibleItems(6);
		itemAdapter = new ItemAdapter(this);
		item.setViewAdapter(itemAdapter);

		verb = (WheelView) findViewById(R.id.verb);
		verb.setVisibleItems(6);
		verbAdapter = new VerbAdapter(this);
		verb.setViewAdapter(verbAdapter);

		direction = (WheelView) findViewById(R.id.direction);
		direction.setVisibleItems(6);
		dirAdapter = new DirectionAdapter(this);
		direction.setViewAdapter(dirAdapter);

		location = (WheelView) findViewById(R.id.location);
		location.setVisibleItems(6);
		locationAdapter = new LocationAdapter(this);
		location.setViewAdapter(locationAdapter);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		TextView title = (TextView) findViewById(R.id.def_wheel_title_textview);
		TextView repID = (TextView) findViewById(R.id.reportid_def_wheel_textview);

		newDef = extras.getBoolean("new");

		if (!newDef) {

			currentDef = new Deficiency();
			currentDef.reportID = extras.getString("id");

			currentDef.X = x = extras.getInt("x");
			currentDef.Y = y = extras.getInt("y");
			currentDef.object = object_str = extras.getString("object");
			currentDef.item = item_str = extras.getString("item");
			currentDef.verb = verb_str = extras.getString("verb");
			currentDef.direction = direction_str = extras.getString("direction");
			currentDef.location = location_str = extras.getString("location");

			currentDef.trade = trade_def = extras.getString("trade");
			currentDef.roomNo = room_def = extras.getString("room");
			currentDef.floor = floor_def = extras.getString("floor");

			currentDef.priority = priority_def = extras.getBoolean("priority");

			specificItem.setCurrentItem(specItemAdapter.getItemIndex(object_str));
			item.setCurrentItem(itemAdapter.getItemIndex(item_str));
			verb.setCurrentItem(verbAdapter.getItemIndex(verb_str));
			direction.setCurrentItem(dirAdapter.getItemIndex(direction_str));
			location.setCurrentItem(locationAdapter.getItemIndex(location_str));

			// get the checkbox....
			CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxPriority);
			checkBox.setChecked(priority_def);

			title.setText("Edit deficiency");
			repID.setText(currentDef.reportID);
		} else {

			currentDef = new Deficiency();
			currentDef.reportID = DeficiencyParser.generateReportId();

			currentDef.X = x = extras.getInt("x");
			currentDef.Y = y = extras.getInt("y");

			currentDef.trade = trade_def = extras.getString("trade");
			currentDef.roomNo = room_def = extras.getString("room");
			currentDef.floor = floor_def = extras.getString("floor");

			title.setText("Create new deficiency");
			repID.setText(currentDef.reportID);
		}

		// gets the current item of the wheel for specific item
		object_str = specItemAdapter.getCurrentItem(specificItem.getCurrentItem());
		item_str = itemAdapter.getCurrentItem(item.getCurrentItem());
		verb_str = verbAdapter.getCurrentItem(verb.getCurrentItem());
		direction_str = dirAdapter.getCurrentItem(direction.getCurrentItem());
		location_str = locationAdapter.getCurrentItem(location.getCurrentItem());
	}

	/** Action performed on save-button click. */
	public void save(View view) {

		currentDef.direction = direction_str = dirAdapter.getCurrentItem(direction.getCurrentItem());
		currentDef.item = item_str = itemAdapter.getCurrentItem(item.getCurrentItem());
		currentDef.location = location_str = locationAdapter.getCurrentItem(location.getCurrentItem());
		currentDef.verb = verb_str = verbAdapter.getCurrentItem(verb.getCurrentItem());
		currentDef.object = object_str = specItemAdapter.getCurrentItem(specificItem.getCurrentItem());

		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxPriority);
		currentDef.priority = priority_def = (checkBox.isChecked());

		if (!newDef)
			DeficiencyParser.editDeficiency(currentDef);
		else
			DeficiencyParser.addNewDeficiency(currentDef);
		finish();
	}

	/**
	 * Adapter for SpecificItem
	 */
	class SpecificItemAdapter extends AbstractWheelTextAdapter {

		private String specificItem[] = new String[] { "", "RWL Pipe", "Drain Pipe", "Main Stack", "Boiler Stack",
				"Manifold", "Water Shut-Off", "Tub/Shower", "Toilet", "Hot/Cold Lines", "Fridge Water Line",
				"Dishwasher Line", "Trim" };

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

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {

			View view = super.getItem(index, cachedView, parent);

			return view;
		}

		@Override
		public int getItemsCount() {

			return specificItem.length;
		}

		@Override
		protected CharSequence getItemText(int index) {

			return specificItem[index];
		}
	}

	/**
	 * Adapter for Item
	 */
	class ItemAdapter extends AbstractWheelTextAdapter {

		private String itemList[] = new String[] { "", "Install", "Location", "Height", "Sink", "Spout", "Diverter",
				"Showerhead", "Plug", "Overflow", "Wall Escutcheon", "Base", "Tank", "P-Trap", "Flow-Control",
				"Coupling", "Insulation", "Dishwasher", "Washer" };

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

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {

			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {

			return itemList.length;
		}

		@Override
		protected CharSequence getItemText(int index) {

			return itemList[index];
		}
	}

	/**
	 * Adapter for Verb
	 */
	class VerbAdapter extends AbstractWheelTextAdapter {

		private String verb[] = new String[] { "", "Clogged/Blocked", "Cracked/Broken", "Leaking", "Kinked", "Loose",
				"Stiff", "Not Anchored", "Scratched", "Buried", "Missing", "Incorrect", "Not Square", "Not-Plumb",
				"Not-Level", "Not-Installed", "Not-Aligned", "Not-Secured", "Not Centered", "Undersized", "Oversized",
				"Replace", "Proud", "Extend", "Relocate", "Add", "Remove", "To Be" };

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

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {

			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {

			return verb.length;
		}

		@Override
		protected CharSequence getItemText(int index) {

			return verb[index];
		}
	}

	/**
	 * Adapter for direction
	 */
	class DirectionAdapter extends AbstractWheelTextAdapter {

		private String direction[] = new String[] { "", "@", "Below", "Above", "Right of", "Left of ", "Behind",
				"Infront", "To", "By", "Location", "With" };

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

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {

			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {

			return direction.length;
		}

		@Override
		protected CharSequence getItemText(int index) {

			return direction[index];
		}
	}

	/**
	 * Adapter for Location
	 */
	class LocationAdapter extends AbstractWheelTextAdapter {

		private String location[] = new String[] { "", "Corner", "Floor", "Ceiling", "Kitchen", "Living", "Dining",
				"Bathroom", "Entry", "Laundry", "Closet", "Storage", "Den", "Opposite Side", "TV Center", "Window",
				"Suite Entry", "Grout Line", "Countertop" };

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

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {

			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {

			return location.length;
		}

		@Override
		protected CharSequence getItemText(int index) {

			return location[index];
		}
	}

}
