package de.sitewaerts.cleverdox.viewer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Scroller;

import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.ReaderView;
/**
 * 
 * modifications of MuPDF
 * 
 * - double tap to zoom (from zReader)
 * 
 * @author Philipp Bohnenstengel (raumobil GmbH)
 *
 */
public class SDVMuPDFReaderView extends MuPDFReaderView implements
		OnDoubleTapListener {
	
	/**
	 *  variables used by zReader code for zoom
	 */
	private int pageWidth, pageHeight, pageTop, pageLeft, pageRight, pageBottom;
	private int fitLeft = 0;
	private int fitTop = 0;


	public SDVMuPDFReaderView(Activity act) {
		super(act);
	}
	
	/**
	 * set variables for zReader zoom code
	 * https://github.com/LiEo-HiDev/zReader-mupdf/blob/master/zPDFReader/src/com/artifex/mupdfdemo/ReaderView.java#L641
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		View cv;
		Point cvOffset;
		
		// Ensure current view is present
		int cvLeft, cvRight, cvTop, cvBottom;
		boolean notPresent = (getMChildViews().get(getMCurrent()) == null);
		cv = getOrCreateChild(getMCurrent());
		// When the view is sub-screen-size in either dimension we
		// offset it to center within the screen area, and to keep
		// the views spaced out
		cvOffset = subScreenSizeOffset(cv);
		if (notPresent) {
			// Main item not already present. Just place it top left
			cvLeft = cvOffset.x;
			cvTop = cvOffset.y;
		} else {
			// Main item already present. Adjust by scroll offsets
			cvLeft = cv.getLeft() + getMXScroll();
			cvTop = cv.getTop() + getMYScroll();
		}
		// Scroll values have been accounted for
		setMXScroll(0);
		setMYScroll(0);
		cvRight = cvLeft + cv.getMeasuredWidth();
		cvBottom = cvTop + cv.getMeasuredHeight();
		
		if (!getMUserInteracting() && getMScroller().isFinished()) {
			Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
			cvRight += corr.x;
			cvLeft += corr.x;
			cvTop += corr.y;
			cvBottom += corr.y;
		} else if (cv.getMeasuredHeight() <= getHeight()) {
			// When the current view is as small as the screen in height, clamp
			// it vertically
			Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
			cvTop += corr.y;
			cvBottom += corr.y;
		}

		cv.layout(cvLeft, cvTop, cvRight, cvBottom);

		if (getMCurrent() > 0) {
			View lv = getOrCreateChild(getMCurrent() - 1);
			Point leftOffset = subScreenSizeOffset(lv);
			int gap = leftOffset.x + getGAP() + cvOffset.x;
			lv.layout(cvLeft - lv.getMeasuredWidth() - gap, (cvBottom + cvTop - lv.getMeasuredHeight()) / 2, cvLeft
					- gap, (cvBottom + cvTop + lv.getMeasuredHeight()) / 2);
		}

		if (getMCurrent() + 1 < getMAdapter().getCount()) {
			View rv = getOrCreateChild(getMCurrent() + 1);
			Point rightOffset = subScreenSizeOffset(rv);
			int gap = cvOffset.x + getGAP() + rightOffset.x;
			rv.layout(cvRight + gap, (cvBottom + cvTop - rv.getMeasuredHeight()) / 2, cvRight + rv.getMeasuredWidth()
					+ gap, (cvBottom + cvTop + rv.getMeasuredHeight()) / 2);
		}
		
		pageTop = cvTop;
		pageRight = cvRight;
		pageLeft = cvLeft;
		pageBottom = cvBottom;
		pageWidth = cvRight-cvLeft;
		pageHeight = cvBottom-cvTop;
		if(getMScale() == 1.0f){
			fitLeft = cvLeft;
			fitTop = cvTop;
		}

		invalidate();
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return true;
	}

	/**
	 * Double tap to zoom implementation from zReader
	 * https://github.com/LiEo-HiDev/zReader-mupdf/blob/master/zPDFReader/src/com/artifex/mupdfdemo/ReaderView.java#L880
	 */
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (getMScale() != 1.0f) {
			setMScale(1.0f);
			View v = getMChildViews().get(getMCurrent());
			if (v != null) {
				setMXScroll((-pageLeft) + fitLeft);
				setMYScroll((-pageTop) + fitTop);
			}
		} else {
			if (e.getX() >= pageLeft && e.getX() <= pageRight) {
				setMScale(2.0f);
			}
			View v = getMChildViews().get(getMCurrent());
			if (v != null) {
				float spX = e.getX() - (float) fitLeft;
				float spY = e.getY() - (float) fitTop;

				float lpX = spX * getMScale();
				float lpY = spY * getMScale();
				
				//http://stackoverflow.com/a/1016941/3070886
				WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				int displayWidth = size.x;
				int displayHeight = size.y;

				int Left = (int) ((displayWidth / 2) - lpX);
				int Top = (int) ((displayHeight / 2) - lpY);
//
				if ((displayWidth / 2) > lpX) {
					Left = -fitLeft;
				} else if (e.getX() > (displayWidth / 2)) {
					Left = Left + fitLeft;
				}
				if ((displayHeight / 2) > lpY) {
					Top = -fitTop;
				} else if (e.getY() > (displayHeight / 2)) {
					Top = Top + fitTop;
				}

				setMXScroll(Left);
				setMYScroll(Top);
			}
			//TODO double page mode
//			if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
//					&& MuPDFActivity.dPageMode == MuPDFCore.SINGLE_PAGE_MODE) {
//				mScale = (float) MuPDFActivity.disPlayWidth / (float) pageWidth;
//				mXScroll = -(int) ((pageWidth * mScale / 2) - (pageWidth / 2));
//				int pointPageY = (int) ((e.getY() - pageTop) / 1.5 * mScale);
//				if (((pageHeight * mScale) - MuPDFActivity.disPlayHeight) < pointPageY) {
//					pointPageY = (int) ((pageHeight * mScale) - MuPDFActivity.disPlayHeight);
//				}
//				mYScroll = -pointPageY;
//			}
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}
	
    /**
     * XXX here be dragons
     * using reflection to be able to extend MuPDF code with its private fields everywhere...
     * @param fieldname
     * @return value of requested field (has to be cast to field class or null if field does not exist
     */
    private Object getPrivateFieldOfReaderView(String fieldname) {
    	Class<?> rv = ReaderView.class;
    	try {
    		Field f = rv.getDeclaredField(fieldname);
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
    private boolean setPrivateFieldOfReaderView(String fieldname, Object value) {
    	Class<?> rv = ReaderView.class;
    	try {
    		Field f = rv.getDeclaredField(fieldname);
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
    
    public float getMScale() {
    	return (Float) getPrivateFieldOfReaderView("mScale");
    }
    
	private void setMScale(float f) {
		setPrivateFieldOfReaderView("mScale", f);
	}

	private SparseArray<View> getMChildViews() {
		return (SparseArray<View>) getPrivateFieldOfReaderView("mChildViews");
	}

	private int getMCurrent() {
		return (Integer) getPrivateFieldOfReaderView("mCurrent");
	}

	private int getMXScroll() {
		return (Integer) getPrivateFieldOfReaderView("mXScroll");
	}

	private void setMXScroll(int i) {
		setPrivateFieldOfReaderView("mXScroll", i);
	}
	
	private int getMYScroll() {
		return (Integer) getPrivateFieldOfReaderView("mYScroll");
	}

	private void setMYScroll(int i) {
		setPrivateFieldOfReaderView("mYScroll", i);
	}

	private boolean getMUserInteracting() {
		return (Boolean) getPrivateFieldOfReaderView("mUserInteracting");
	}

	private Scroller getMScroller() {
		return (Scroller) getPrivateFieldOfReaderView("mScroller");
	}
	
	private Adapter getMAdapter() {
		return (Adapter) getPrivateFieldOfReaderView("mAdapter");
	}

	private int getGAP() {
		return (Integer) getPrivateFieldOfReaderView("GAP");
	}
		
	private View getOrCreateChild(int i) {
    	Class<?> rv = ReaderView.class;
    	try {
			Method gocc = rv.getDeclaredMethod("getOrCreateChild", int.class);
			gocc.setAccessible(true);
			return (View) gocc.invoke(this, i);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	private Point subScreenSizeOffset(View v) {
    	Class<?> rv = ReaderView.class;
    	try {
			Method ssso = rv.getDeclaredMethod("subScreenSizeOffset", View.class);
			ssso.setAccessible(true);
			return (Point) ssso.invoke(this, v);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	private Point getCorrection(Rect bounds) {
    	Class<?> rv = ReaderView.class;
    	try {
			Method gc = rv.getDeclaredMethod("getCorrection", Rect.class);
			gc.setAccessible(true);
			return (Point) gc.invoke(this, bounds);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	private Rect getScrollBounds(int left, int top, int right, int bottom) {
    	Class<?> rv = ReaderView.class;
    	try {
			Method gsb = rv.getDeclaredMethod("getScrollBounds", int.class, int.class, int.class, int.class);
			gsb.setAccessible(true);
			return (Rect) gsb.invoke(this, left, top, right, bottom);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			return null;
		}
    }
}
