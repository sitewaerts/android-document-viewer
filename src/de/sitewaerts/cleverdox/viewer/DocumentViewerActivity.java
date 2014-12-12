package de.sitewaerts.cleverdox.viewer;

import java.io.File;
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
//import com.artifex.mupdfdemo.R;
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
import android.os.Environment;
import android.widget.SearchView.OnQueryTextListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * currently this is just a dummy that is invoked from cordova/phonegap
 * @author Philipp Bohnenstengel (raumobil GmbH)
 *
 */
public class DocumentViewerActivity
        extends MuPDFActivity
{
	private final int SEARCH_FORWARD = 1;
	private final int SEARCH_BACKWARD = -1;
	
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
	
	private int visiblePages = 1;
	/**
	 * remember last search term
	 */
	private String cachedSearchTerm = "";
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
/*
 * XXX this is the method of the super class MuPDFActivity; reimplemented and customized
 */
    	if (getCore() == null) { //XXX
			return;			
		}

		// Now create the UI.
		// First create the document view
		MuPDFReaderView mDocView = new MuPDFReaderView(this) { //XXX
			@Override
			protected void onMoveToChild(int i) {
// TODO update pagenumberview and slider ui				
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
				ActionBar ab = getActionBar();
				// TODO show/hide pagenumberview and slider
				if (!ab.isShowing()) {
					ab.show();
				} else {
					ab.hide();
				}
			}

			@Override
			protected void onDocMotion() {
//				hideButtons();
				ActionBar ab = getActionBar();
				ab.hide();
			}

//XXX MuPDF document editing, we don't need that
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

// XXX replaced by action bar
//		// Make the buttons overlay, and store all its
//		// controls in variables
//		makeButtonsView();

// TODO rebuild page slider without the MuPDF layout
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

// TODO reimplement search interaction
//		// Activate the search-preparing button
//		mSearchButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				searchModeOn();
//			}
//		});

// XXX MuPDF reflow mode, we don't need that
//		// Activate the reflow button
//		mReflowButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				toggleReflow();
//			}
//		});

// XXX MuPDF document editing, we don't need that
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

// TODO reimplement search
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

// XXX outline feature is now in action bar menu
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

// XXX currently show/hideButton methods are not implemented separately but in onTapMainDocArea
//		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
//			showButtons();
//
// TODO reimplement search
//		if(savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
//			searchModeOn();
//
// XXX reflow mode is deactivated
//		if(savedInstanceState != null && savedInstanceState.getBoolean("ReflowMode", false))
//			reflowModeSet(true);

		// Stick the document view and the buttons overlay into a parent view
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(mDocView);
// XXX buttons view is now action bar
//		layout.addView(mButtonsView);
		setContentView(layout);
    	
		//add document view
    	setMDocView(mDocView); 	
/*
 * end of MuPDF code
 */
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
        if (getCore() != null) {
        	if (!getCore().hasOutline() || //TODO move to NavigationViewActivity once thumbnails are implemented
        			(getCore().countPages() <= visiblePages)) { 
        		tmp = menu.findItem(R.id.action_navigation_view);
        		if (tmp != null) {
//        			tmp.setEnabled(false);
        			tmp.setVisible(false);
        		}
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
        if (/*!bookmarksEnabled*/ true) { //TODO activate when bookmarks are implemented
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
        } else {
        	tmp = menu.findItem(R.id.action_search);
        	SearchView searchView = (SearchView) tmp.getActionView();
        	searchView.setQueryHint(getString(R.string.search_placeholder));
        	searchView.setOnQueryTextListener(new OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String searchTerm) {
					cachedSearchTerm = searchTerm;
					search(searchTerm, SEARCH_FORWARD);
					return true;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					// TODO Auto-generated method stub
					return false;
				}
        	});
        	LinearLayout ll = (LinearLayout) searchView.findViewById(getResources().getIdentifier("android:id/search_plate", null, null));
        	ImageButton prev = new ImageButton(this);
        	prev.setBackground(null);
        	prev.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_previous_item));
        	prev.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					search(cachedSearchTerm, SEARCH_BACKWARD);
				}
			});
        	ll.addView(prev);
        	ImageButton next = new ImageButton(this);
        	next.setBackground(null);
        	next.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_next_item));
        	next.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					search(cachedSearchTerm, SEARCH_FORWARD);
				}
			});
        	ll.addView(next);        
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
		Intent myIntent = getIntent();
		Uri docUri = myIntent != null ? myIntent.getData() : null;
		if (docUri != null) {
			if (docUri.getScheme() == null) {
				docUri = Uri.parse("file://"+docUri.toString());
			}
		}
		
	    // Handle item selection
	    switch (item.getItemId()) {
	    	//up navigation
	    	case android.R.id.home:
	    		finish();
	    		return true;
	    	case R.id.action_navigation_view:
	    		if (getCore() != null) {
	    			OutlineItem outline[] = getCore().getOutline();
	    			if (outline != null) {
	    				OutlineActivityData.get().items = outline;
	    				Intent intent = new Intent(DocumentViewerActivity.this, NavigationViewActivity.class);
	    				//add relevant cordova options
	    				intent.putExtra("closeLabel", navigationViewCloseLabel);
	    				intent.putExtra("bookmarksEnabled", bookmarksEnabled);
	    				startActivityForResult(intent, getOUTLINEREQUEST());
	    			}
	    		}
	    		return true;
	    	case R.id.action_open_with:
	    		Intent openWithIntent = new Intent(Intent.ACTION_VIEW);
	    		openWithIntent.setDataAndType(docUri,"application/pdf"); //XXX will the app eventually support other document types?
	            openWithIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //XXX this is from the example, do we want this behaviour?
	    		startActivity(Intent.createChooser(openWithIntent, getString(R.string.open_with_chooser_title)));
	    		return true;
	    	case R.id.action_print:
	    		if (getCore() != null) {
	    			if (!getCore().fileFormat().startsWith("PDF")) {
	    				showInfo(getString(R.string.format_currently_not_supported));
	    				return true;
	    			}
	    		}
	    		
	    		Intent printIntent = new Intent(this, PrintActivity.class);
	    		printIntent.setDataAndType(docUri, "application/pdf");
	    		printIntent.putExtra("title", getMFileName());
	    		printIntent.putExtra("closeLabel", navigationViewCloseLabel);
	    		startActivityForResult(printIntent, getPRINTREQUEST());
	    		return true;
	    	case R.id.action_email:
	    		
	    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    		//XXX tried various methods to filter only email apps
	    		//filter email apps via mime type, still shows some non email apps + we need mime type for attachment
//	    		emailIntent.setType("message/rfc822");
	    		//filter email apps via mailto uri, only works with ACTION_VIEW (we need ACTION_SEND)
//	    		Uri data = Uri.parse("mailto:?");
//	    		emailIntent.setData(data);
	    		//filter email apps via category, only works with ACTION_MAIN (we need ACTION_SEND) and launches gmail without showing chooser first
//	    		emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
	    		//TODO research how other people do this (e.g. phonegap share plugin)
	    		
	    		//add document as attachment
	    		emailIntent.setType("application/pdf");
	    		emailIntent.putExtra(Intent.EXTRA_STREAM, docUri);
	    		//add some text to the email
	    		emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
	    		emailIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.email_text), getString(R.string.app_name)));
	    		//choose email app
	    		startActivity(Intent.createChooser(emailIntent, getString(R.string.email_chooser_title)));
	    		return true;
	    	case R.id.action_bookmark:
	    		//TODO
	    		return false;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == getPRINTREQUEST()) {
			if (resultCode == RESULT_CANCELED) {
				showInfo(getString(R.string.print_failed));
			}
			return; //prevent call to super
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showInfo(String message) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
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
    
    private void search(String searchTerm, int direction) {
		SearchTask st = getMSearchTask();
		int displayPage = getMDocView().getDisplayedViewIndex();
		SearchTaskResult r = SearchTaskResult.get();
		int searchPage = r != null ? r.pageNumber : -1;
		st.go(searchTerm, 1, displayPage, searchPage);
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
    
    public SearchTask getMSearchTask() {
    	return (SearchTask) getPrivateFieldOfSuper("mSearchTask");
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
    
    public int getPRINTREQUEST() {
    	return (Integer) getPrivateFieldOfSuper("PRINT_REQUEST");
    }
}
