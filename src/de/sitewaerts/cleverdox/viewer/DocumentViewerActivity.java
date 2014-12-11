package de.sitewaerts.cleverdox.viewer;

import java.lang.reflect.Field;

//import com.artifex.mupdfdemo.Hit;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.MuPDFView;
import com.artifex.mupdfdemo.OutlineActivity;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.OutlineItem;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
//import com.artifex.mupdfdemo.MuPDFActivity.TopBarMode;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
	
	
    /**
     * Called when the activity is first created.
     */
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
//    	super.createUI(savedInstanceState);
    	//TODO this is the method of the super class; reimplement and customize
		if (getCore() == null) { //XXX
			return;			
		}

		// Now create the UI.
		// First create the document view
		MuPDFReaderView mDocView = new MuPDFReaderView(this) { //XXX
			@Override
			protected void onMoveToChild(int i) {
//				if (core == null)
//					return;
//				mPageNumberView.setText(String.format("%d / %d", i + 1,
//						core.countPages()));
//				mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
//				mPageSlider.setProgress(i * mPageSliderRes);
				super.onMoveToChild(i);
			}

			@Override
			protected void onTapMainDocArea() {
//				if (!mButtonsVisible) {
//					showButtons();
//				} else {
//					if (mTopBarMode == TopBarMode.Main)
//						hideButtons();
//				}
				ActionBar ab = getActionBar(); //XXX
				if (!ab.isShowing()) {
					ab.show();
				} else {
					ab.hide();
				}
			}

			@Override
			protected void onDocMotion() {
//				hideButtons();
				ActionBar ab = getActionBar(); //XXX
				ab.hide();
			}

//			@Override
//			protected void onHit(Hit item) {
//				switch (mTopBarMode) {
//				case Annot:
//					if (item == Hit.Annotation) {
//						showButtons();
//						mTopBarMode = TopBarMode.Delete;
//						mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
//					}
//					break;
//				case Delete:
//					mTopBarMode = TopBarMode.Annot;
//					mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
//				// fall through
//				default:
//					// Not in annotation editing mode, but the pageview will
//					// still select and highlight hit annotations, so
//					// deselect just in case.
//					MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
//					if (pageView != null)
//						pageView.deselectAnnotation();
//					break;
//				}
//			}
		};
		mDocView.setAdapter(new MuPDFPageAdapter(this, this, getCore())); //XXX

		SearchTask mSearchTask = new SearchTask(this, getCore()) { //XXX
			@Override
			protected void onTextFound(SearchTaskResult result) {
				SearchTaskResult.set(result);
				MuPDFReaderView mDocView = getMDocView(); //XXX
				// Ask the ReaderView to move to the resulting page
				mDocView.setDisplayedViewIndex(result.pageNumber);
				// Make the ReaderView act on the change to SearchTaskResult
				// via overridden onChildSetup method.
				mDocView.resetupChildren();
			}
		};
		setMSearchTask(mSearchTask); //XXX

//		// Make the buttons overlay, and store all its
//		// controls in variables
//		makeButtonsView();

		//TODO rebuild page slider without the MuPDF layout
//		// Set up the page slider
//		int smax = Math.max(core.countPages()-1,1);
//		mPageSliderRes = ((10 + smax - 1)/smax) * 2;

//		// Set the file-name text
//		mFilenameView.setText(mFileName);

//		// Activate the seekbar
//		mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				mDocView.setDisplayedViewIndex((seekBar.getProgress()+mPageSliderRes/2)/mPageSliderRes);
//			}
//
//			public void onStartTrackingTouch(SeekBar seekBar) {}
//
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				updatePageNumView((progress+mPageSliderRes/2)/mPageSliderRes);
//			}
//		});

		//TODO reimplement search interaction
//		// Activate the search-preparing button
//		mSearchButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				searchModeOn();
//			}
//		});

//		// Activate the reflow button
//		mReflowButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				toggleReflow();
//			}
//		});

//		if (core.fileFormat().startsWith("PDF"))
//		{
//			mAnnotButton.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					mTopBarMode = TopBarMode.Annot;
//					mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
//				}
//			});
//		}
//		else
//		{
//			mAnnotButton.setVisibility(View.GONE);
//		}

//		// Search invoking buttons are disabled while there is no text specified
//		mSearchBack.setEnabled(false);
//		mSearchFwd.setEnabled(false);
//		mSearchBack.setColorFilter(Color.argb(255, 128, 128, 128));
//		mSearchFwd.setColorFilter(Color.argb(255, 128, 128, 128));

//		// React to interaction with the text widget
//		mSearchText.addTextChangedListener(new TextWatcher() {
//
//			public void afterTextChanged(Editable s) {
//				boolean haveText = s.toString().length() > 0;
//				setButtonEnabled(mSearchBack, haveText);
//				setButtonEnabled(mSearchFwd, haveText);
//
//				// Remove any previous search results
//				if (SearchTaskResult.get() != null && !mSearchText.getText().toString().equals(SearchTaskResult.get().txt)) {
//					SearchTaskResult.set(null);
//					mDocView.resetupChildren();
//				}
//			}
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {}
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {}
//		});

//		//React to Done button on keyboard
//		mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE)
//					search(1);
//				return false;
//			}
//		});

//		mSearchText.setOnKeyListener(new View.OnKeyListener() {
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
//					search(1);
//				return false;
//			}
//		});

//		// Activate search invoking buttons
//		mSearchBack.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				search(-1);
//			}
//		});
//		mSearchFwd.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				search(1);
//			}
//		});
//
//		mLinkButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				setLinkHighlight(!mLinkHighlight);
//			}
//		});

		//TODO move feature to actionbar menu
//		if (core.hasOutline()) {
//			mOutlineButton.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					OutlineItem outline[] = core.getOutline();
//					if (outline != null) {
//						OutlineActivityData.get().items = outline;
//						Intent intent = new Intent(MuPDFActivity.this, OutlineActivity.class);
//						startActivityForResult(intent, OUTLINE_REQUEST);
//					}
//				}
//			});
//		} else {
//			mOutlineButton.setVisibility(View.GONE);
//		}

		// Reenstate last state if it was recorded
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page"+getMFileName(), 0)); //XXX

//		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
//			showButtons();
//
//		if(savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
//			searchModeOn();
//
//		if(savedInstanceState != null && savedInstanceState.getBoolean("ReflowMode", false))
//			reflowModeSet(true);

		// Stick the document view and the buttons overlay into a parent view
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(mDocView);
//		layout.addView(mButtonsView);
		setContentView(layout);
    	
		//add document view
    	setMDocView(mDocView);
    	
    	//create action bar
    	ActionBar ab = getActionBar();
    	//enable up navigation
    	ab.setDisplayHomeAsUpEnabled(true);
    	//invisible icon
    	ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    	//set title from cordova options
    	if (title != null) {
    		ab.setTitle(title);
    	}
    	//TODO make up button wider, disable title clickable
    }
    
    /**
     * create action bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.document_view_menu, menu);
        //hide actions based on cordova options
        MenuItem tmp;
        //TODO remove once thumbnails are implemented
		if (!getCore().hasOutline()) { //XXX potential nullpointer
			tmp = menu.findItem(R.id.action_navigation_view);
			if (tmp != null) {
				tmp.setEnabled(false);
			}
		}
        if (!openWithEnabled) {
        	tmp = menu.findItem(R.id.action_open_with);
        	if (tmp != null) {
        		tmp.setVisible(false);
        	}
        }
        if (!printEnabled) {
        	tmp = menu.findItem(R.id.action_print);
        	if (tmp != null) {
        		tmp.setVisible(false);
        	}
        }
        if (!emailEnabled) {
        	tmp = menu.findItem(R.id.action_email);
        	if (tmp != null) {
        		tmp.setVisible(false);
        	}
        }
        if (!bookmarksEnabled) {
        	tmp = menu.findItem(R.id.action_bookmark);
        	if (tmp != null) {
        		tmp.setVisible(false);
        	}
        }
        if (!searchEnabled) {
        	tmp = menu.findItem(R.id.action_search);
        	if (tmp != null) {
        		tmp.setVisible(false);
        	}
        }
        invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
		//XXX no call to super, because super class uses fields which were not initialized here
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
	    	case R.id.action_navigation_view:
				OutlineItem outline[] = getCore().getOutline(); //XXX potential nullpointer
				if (outline != null) {
					OutlineActivityData.get().items = outline;
					Intent intent = new Intent(DocumentViewerActivity.this, NavigationViewActivity.class);
					//add relevant cordova options
					intent.putExtra("closeLabel", navigationViewCloseLabel);
					intent.putExtra("bookmarksEnabled", bookmarksEnabled);
					startActivityForResult(intent, getOUTLINEREQUEST());
				}
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    /**
     * XXX here be dragons
     * using reflection to be able to extend MuPDF code with its private fields everywhere...
     * @param fieldname
     * @return value of requested field (has to be cast to field class or null if field does not exist
     */
    private Object getPrivateFieldOfSuper(String fieldname) {
    	Class<?> mpa = this.getClass().getSuperclass();
    	try {
    		Field f = mpa.getDeclaredField(fieldname);
    		f.setAccessible(true);
    		return f.get(this);
    	} catch (NoSuchFieldException e) {
    		return null;
    	} catch (IllegalAccessException e) {
    		return null;
		} catch (IllegalArgumentException e) {
    		return null;
		}
    }
    
    /**
     * XXX here be dragons
     * using reflection to be able to extend MuPDF code with its private fields everywhere...
     * @param fieldname
     * @param value new value
     * @return true if successful else false
     */
    private boolean setPrivateFieldOfSuper(String fieldname, Object value) {
    	Class<?> mpa = this.getClass().getSuperclass();
    	try {
    		Field f = mpa.getDeclaredField(fieldname);
    		f.setAccessible(true);
    		f.set(this, value);
    		return true;
    	} catch (NoSuchFieldException e) {
    		return false;
    	} catch (IllegalAccessException e) {
    		return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
    }
    
    public MuPDFReaderView getMDocView() {
    	return (MuPDFReaderView) getPrivateFieldOfSuper("mDocView");
    }
    
    public boolean setMDocView(MuPDFReaderView mprv) {
    	return setPrivateFieldOfSuper("mDocView", mprv);
    }
    
    public MuPDFCore getCore() {
    	return (MuPDFCore) getPrivateFieldOfSuper("core");
    }
    
    public boolean setMSearchTask(SearchTask st) {
    	return setPrivateFieldOfSuper("mSearchTask", st);
    }
    
    public String getMFileName() {
    	return (String) getPrivateFieldOfSuper("mFileName");
    }

    public int getOUTLINEREQUEST() {
    	return (Integer) getPrivateFieldOfSuper("OUTLINE_REQUEST");
    }
}
