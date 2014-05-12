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
                
        final WheelView country = (WheelView) findViewById(R.id.country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this));
        
        final WheelView deficiency = (WheelView) findViewById(R.id.deficiency);
        deficiency.setVisibleItems(4);
        deficiency.setViewAdapter(new DeficiencyAdapter(this));
        
        final WheelView d = (WheelView) findViewById(R.id.D);
        d.setVisibleItems(4);
        d.setViewAdapter(new D(this));
        
        
//        final String cities[][] = new String[][] {
//        		new String[] {"New York", "Washington", "Chicago", "Atlanta", "Orlando"},
//        		new String[] {"Ottawa", "Vancouver", "Toronto", "Windsor", "Montreal"},
//        		new String[] {"Kiev", "Dnipro", "Lviv", "Kharkiv"},
//        		new String[] {"Paris", "Bordeaux"},
//        		};
//        
//        final WheelView city = (WheelView) findViewById(R.id.city);
//        city.setVisibleItems(5);

        country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//			    if (!scrolling) {
//			        updateCities(city, cities, newValue);
//			    }
			}
		});
        
        country.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
//                updateCities(city, cities, country.getCurrentItem());
            }
        });

        country.setCurrentItem(1);
    }
    
//    /**
//     * Updates the city wheel
//     */
//    private void updateCities(WheelView city, String cities[][], int index) {
//        ArrayWheelAdapter<String> adapter =
//            new ArrayWheelAdapter<String>(this, cities[index]);
//        adapter.setTextSize(18);
//        city.setViewAdapter(adapter);
//        city.setCurrentItem(cities[index].length / 2);        
//    }
    
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] =
            new String[] {"USA", "Canada", "Ukraine", "France"};
        // Countries flags
 
        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.country_name);
        }
        
  
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
       
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
    
    /**
     * Adapter for countries
     */
    private class DeficiencyAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String deficiency[] =
            new String[] {"asdf", "aa", "sdf", "ewr"};
 
        /**
         * Constructor
         */
        protected DeficiencyAdapter(Context context) {
            super(context, R.layout.deficiency_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.deficiency_name);
        }
        
  
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
//            ImageView img = (ImageView) view.findViewById(R.id.flag);
//            img.setImageResource(flags[index]);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return deficiency.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return deficiency[index];
        }
    }
    
    /**
     * Adapter for countries
     */
    private class D extends AbstractWheelTextAdapter {
        // Countries names
        private String d[] =
            new String[] {"aaaaaaa", "aaaaa", "saaaadf", "ewaaaar"};
 
        /**
         * Constructor
         */
        protected D(Context context) {
            super(context, R.layout.d_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.d_name);
        }
        
  
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
//            ImageView img = (ImageView) view.findViewById(R.id.flag);
//            img.setImageResource(flags[index]);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return d.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return d[index];
        }
    }
}
