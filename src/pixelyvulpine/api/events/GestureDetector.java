package pixelyvulpine.api.events;

import pixelyvulpine.api.lcdui.Layout;

public class GestureDetector {

	public interface OnContextClickListener{
		public abstract boolean onContextClick(MotionEvent e);
	}
	
	public interface OnDoubleTapListener{
		public abstract boolean onDoubleTap(MotionEvent e);
		public abstract boolean onDoubleTapEvent(MotionEvent e);
		public abstract boolean onSingleTapConfirmed(MotionEvent e);
	}
	
	public interface OnGestureListener{
		public abstract boolean onDown(MotionEvent e);
		public abstract boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
		public abstract boolean onLongPress(MotionEvent e);
		public abstract boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
		public abstract void onShowPress(MotionEvent e);
		public abstract boolean onSingleTapUp(MotionEvent e);
	}
	
	public static class SimpleOnGestureListener implements OnContextClickListener, OnDoubleTapListener, OnGestureListener{

		public boolean onDown(MotionEvent e) {
			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}

		public boolean onLongPress(MotionEvent e) {
			return false;
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		public void onShowPress(MotionEvent e) {}

		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		public boolean onDoubleTap(MotionEvent e) {
			return false;
		}

		public boolean onDoubleTapEvent(MotionEvent e) {
			return false;
		}

		public boolean onSingleTapConfirmed(MotionEvent e) {
			return false;
		}

		public boolean onContextClick(MotionEvent e) {
			return false;
		}
		
	}

	private boolean longpressEnabled = true;
	private Layout context;
	private OnGestureListener onGestureListener;
	private OnContextClickListener onContextClickListener;
	private OnDoubleTapListener onDoubleTapListener;
	
	public GestureDetector(Layout context, OnGestureListener listener) {
		this.context=context;
		this.onGestureListener=listener;
	}
	
	public boolean isLongpressEnabled() {
		return longpressEnabled;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
	}
	
	public void setContextClickListener(OnContextClickListener onContextClickListener) {
		
	}
	
	public void setIsLongpressEnabled(boolean longpressEnabled) {
		this.longpressEnabled=longpressEnabled;
	}
	
	public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
		
	}
	
}
