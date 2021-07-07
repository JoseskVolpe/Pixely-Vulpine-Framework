package pixelyvulpine.api.events;

import pixelyvulpine.Config;
import pixelyvulpine.api.lcdui.Layout;

public class GestureDetector {
	
	private final static byte PIXELSENSIBILITY=8;

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
	
	private boolean moved, movedSensi, down, show, longPress, fling, doubleTap;
	private MotionEvent downEvent, moveEvent, upEvent, tapEvent;
	
	public void update() {
		if(down && !movedSensi) {
			if(!show && System.currentTimeMillis()-downEvent.getEventTime()>=100) {
				onGestureListener.onShowPress(downEvent);
				show=true;
			}
			if(!longPress && System.currentTimeMillis()-downEvent.getEventTime()>=Config.getLongPressTimeout()) {
				longPress=true;
				onGestureListener.onLongPress(downEvent);
			}
		}
		if(tapEvent!=null) {
			if(!doubleTap && onDoubleTapListener!=null && System.currentTimeMillis()-tapEvent.getEventTime()>=Config.getDoubleTapTimeout()) {
				onDoubleTapListener.onSingleTapConfirmed(tapEvent);
				tapEvent=null;
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(!onGestureListener.onDown(event)) {
					down=true;
					show=false;
					moved=false;
					movedSensi=false;
					fling=false;
					longPress=false;
					downEvent=event;
					upEvent=null;
					doubleTap=false;
					
					if(onDoubleTapListener!=null && tapEvent!=null && event.getEventTime()-tapEvent.getEventTime()<=Config.getDoubleTapTimeout()) {
						doubleTap=true;
						onDoubleTapListener.onDoubleTap(event);
					}else {
						tapEvent=null;
					}
				}
			break;
			case MotionEvent.ACTION_MOVE:
				moved=true;
				moveEvent=event;
				
				if(!movedSensi && Math.abs(event.getPointerCoords().x-downEvent.getPointerCoords().x)>=PIXELSENSIBILITY || Math.abs(event.getPointerCoords().y-downEvent.getPointerCoords().y)>=PIXELSENSIBILITY)
					movedSensi=true;
					
				int c = MotionEvent.getHistorySize();
				int lx=downEvent.getPointerCoords().x;
				int ly=downEvent.getPointerCoords().y;
				if(c>1) {
					lx=MotionEvent.getHistoricalPointerCoords(c-2).x;
					ly=MotionEvent.getHistoricalPointerCoords(c-2).y;
				}
				if(!onGestureListener.onScroll(downEvent, event, event.getPointerCoords().x - lx, event.getPointerCoords().y - ly)) {
					
				}
				int dist = (int)(Math.max(Math.abs(event.getPointerCoords().x-lx)/(float)Math.min(context.getWidth(), context.getHeight()), Math.abs(event.getPointerCoords().y-ly)/(float)Math.min(context.getWidth(), context.getHeight()))*100);
				if(!fling && dist>=Config.getMinimumFlingVelocity() && dist <=Config.getMaximumFlingVelocity()) {
					fling=true;
					if(!onGestureListener.onFling(downEvent, moveEvent, event.getPointerCoords().x-lx, event.getPointerCoords().y-ly)) {
						
					}
				}
				if(doubleTap && onDoubleTapListener!=null) {
					onDoubleTapListener.onDoubleTapEvent(event);
				}
			break;
			case MotionEvent.ACTION_UP:
				down=false;
				
				upEvent=event;
				if(tapEvent==null && !movedSensi && event.getEventTime()-downEvent.getEventTime()<=Config.getDoubleTapTimeout()) {
					tapEvent=event;
					onGestureListener.onSingleTapUp(event);
				}
				
			break;
		}
		
		
		return false;
	}
	
	public void setContextClickListener(OnContextClickListener onContextClickListener) {
		this.onContextClickListener = onContextClickListener;
	}
	
	public void setIsLongpressEnabled(boolean longpressEnabled) {
		this.longpressEnabled=longpressEnabled;
	}
	
	public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
		this.onDoubleTapListener=onDoubleTapListener;
	}
	
}
