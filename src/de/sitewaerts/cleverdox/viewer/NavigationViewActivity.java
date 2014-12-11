package de.sitewaerts.cleverdox.viewer;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.artifex.mupdfdemo.OutlineActivity;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.OutlineItem;

public class NavigationViewActivity extends OutlineActivity {
	
	private String closeLabel;
	private boolean bookmarksEnabled;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Get the intent that started this activity
        Intent intent = getIntent();
        // get options passed from cordova
        closeLabel = intent.getStringExtra("closeLabel");
        bookmarksEnabled = intent.getBooleanExtra("bookmarksEnabled", false); //XXX is false the desired default value?
        
    	//create action bar
    	ActionBar ab = getActionBar();
    	//enable up navigation
    	ab.setDisplayHomeAsUpEnabled(true);
    	//invisible icon
    	ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    	//set title to close lable string from cordova options
    	if (closeLabel != null) {
    		ab.setTitle(closeLabel);
    	}
	}
	
    /**
     * handle action bar menu events
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	//up navigation
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
