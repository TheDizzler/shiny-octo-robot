package qdap;

import vdindustries.masterflow.R;
import vdindustries.masterflow.R.id;
import vdindustries.masterflow.R.layout;
import vdindustries.masterflow.R.menu;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import vdindustries.masterflow.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Deficiency extends Activity {
    // Scrolling flag
    private boolean scrolling = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_deficiency);
                
        final WheelView specificItem = (WheelView) findViewById(R.id.specificItem);
        specificItem.setVisibleItems(3);
        specificItem.setViewAdapter(new SpecificItemAdapter(this));
        
        final WheelView item = (WheelView) findViewById(R.id.item);
        item.setVisibleItems(4);
        item.setViewAdapter(new ItemAdapter(this));
        
        final WheelView verb = (WheelView) findViewById(R.id.verb);
        verb.setVisibleItems(4);
        verb.setViewAdapter(new VerbAdapter(this));
        
        final WheelView direction = (WheelView) findViewById(R.id.direction);
        direction.setVisibleItems(4);
        direction.setViewAdapter(new DirectionAdapter(this));
        
        final WheelView location = (WheelView) findViewById(R.id.location);
        location.setVisibleItems(4);
        location.setViewAdapter(new LocationAdapter(this));
        

        specificItem.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//			    if (!scrolling) {
//			        updateCities(city, cities, newValue);
//			    }
			}
		});
        
        specificItem.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
            }
        });

        specificItem.setCurrentItem(1);
    }
    
    /**
     * Adapter for SpecificItem
     */
    private class SpecificItemAdapter extends AbstractWheelTextAdapter {

        private String specificItem[] =
            new String[] {"Drywall", "Tub/Showerbase", "Towel bar", "Glass Door", "Slider Door", "Pocket Door", "Shower Niche","Bench","Door", "Closet","Exterior Cladding", "Canopy", "Railing", "Shear-Wall", "Electrical Panel", "Communication Box","Vanity","Cabinet", "Desk Space" };

 
        /**
         * Constructor
         */
        protected SpecificItemAdapter(Context context) {
            super(context, R.layout.specific_item_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.specific_item_name);
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
    private class ItemAdapter extends AbstractWheelTextAdapter {

        private String item[] =
            new String[] {"Backing", "Joist", "Plywood", "Wall", "Wing Wall", "Stud", "Plate", "Dimensions", "Rough-Opening","Poly-Liner", "Header", "Hold-Down", "Anchor", "Jamb", "Cripple" };
 
        /**
         * Constructor
         */
        protected ItemAdapter(Context context) {
            super(context, R.layout.item_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.item_name);
        }
        
  
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return item.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return item[index];
        }
    }
    
    /**
     * Adapter for Verb
     */
    private class VerbAdapter extends AbstractWheelTextAdapter {

        private String verb[] =
            new String[] {"Missing", "Crowned", "Bowed", "Split", "Incorrect","Not-Plumb","Not-Square","Not-Level","Not-Installed","Not-Aligned","Not-Secured","Off","Undersized","Oversized","Reduce","Enlarge","Box-Out","Replace","Proud","Extend","Relocate","Subtract","Add","Center","To Be"};
 
        /**
         * Constructor
         */
        protected VerbAdapter(Context context) {
            super(context, R.layout.verb_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.verb_name);
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
    private class DirectionAdapter extends AbstractWheelTextAdapter {

        private String verb[] =
            new String[] {"@", "Below", "Above", "Right of", "Left of","Behind","Infront","To","By"};
 
        /**
         * Constructor
         */
        protected DirectionAdapter(Context context) {
            super(context, R.layout.verb_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.verb_name);
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
     * Adapter for Location
     */
    private class LocationAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String verb[] =
            new String[] {"Corner", "Floor", "Ceiling", "Kitchen", "Living","Dining","Bathroom","Entry","Laundry","Closet","Storage","Den","Opposite Side","TV Center","Window","Suite Entry"};
 
        /**
         * Constructor
         */
        protected LocationAdapter(Context context) {
            super(context, R.layout.verb_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.verb_name);
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
}
