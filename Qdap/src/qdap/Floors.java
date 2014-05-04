package qdap;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;

public class Floors extends ExpandableListActivity{
	
	  // Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

        super.onCreate(savedInstanceState);

        
        // Create Expandable List and set it's properties
        ExpandableListView expandableList = getExpandableListView(); 
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // Set the Items of Parent
        setGroupParents();
        // Set The Child Data
        setChildData();

        // Create the Adapter
        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);
    }

    // method to add parent Items
    public void setGroupParents() 
    {
        parentItems.add("2nd Floor");
        parentItems.add("3rd Floor");
        parentItems.add("4th Floor");
        
    }

    // method to set child data of each parent
    public void setChildData() 
    {


        ArrayList<String> child = new ArrayList<String>();
        child.add("200");
        child.add("201");
        child.add("202");
        child.add("203");
  
        
        childItems.add(child);


        child = new ArrayList<String>();
        child.add("300");
        child.add("301");
        child.add("302");
        child.add("303");
        
        childItems.add(child);

 
        child = new ArrayList<String>();
        child.add("400");
        child.add("401");
        child.add("402");
        child.add("403");
        
        childItems.add(child);

        
    }
}