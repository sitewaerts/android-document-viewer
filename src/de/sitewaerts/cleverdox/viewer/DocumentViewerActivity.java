package de.sitewaerts.cleverdox.viewer;

import com.artifex.mupdfdemo.MuPDFActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

/**
 * currently this is just a dummy that is invoked from cordova/phonegap
 * @author Philipp Bohnenstengel (raumobil GmbH)
 *
 */
public class DocumentViewerActivity
        extends MuPDFActivity
{
	/*
	 * options from cordova
	 */
	private String documentViewCloseLabel;
	private String navigationViewCloseLabel;
	private boolean emailEnabled;
	private boolean printEnabled;
	private boolean openWithEnabled;
	private boolean bookmarksEnabled;
	private boolean searchEnabled;
	private String title;
//    /**
//     * Called when the activity is first created.
//     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Get the intent that started this activity
        Intent intent = getIntent();
        // get all the options passed from cordova
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	Bundle viewerOptions = extras.getBundle("de.sitewaerts.cordova.documentviewer.DocumentViewerPlugin"); //XXX
        	documentViewCloseLabel = viewerOptions.getString("documentView.closeLabel");
        	navigationViewCloseLabel = viewerOptions.getString("navigationView.closeLabel");
        	emailEnabled = viewerOptions.getBoolean("email.enabled");
        	printEnabled = viewerOptions.getBoolean("print.enabled");
        	openWithEnabled = viewerOptions.getBoolean("openWith.enabled");
        	bookmarksEnabled = viewerOptions.getBoolean("bookmarks.enabled");
        	searchEnabled = viewerOptions.getBoolean("search.enabled");
        	title = viewerOptions.getString("title");
        }
        createUI(savedInstanceState);
    }
    
    @Override
    public void createUI(Bundle savedInstanceState) {
    	super.createUI(savedInstanceState);
    	ActionBar ab = getActionBar();
    	ab.setDisplayHomeAsUpEnabled(true);
    	if (title != null) {
    		ab.setTitle(title);
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case android.R.id.home:
    			finish();
    			return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
}
