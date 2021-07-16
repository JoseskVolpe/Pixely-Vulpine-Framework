package pixelyvulpine.api.events;

import pixelyvulpine.Config;
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
		public abstract void onLongPress(MotionEvent e);
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

		public void onLongPress(MotionEvent e) {}

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
		
		if(listener instanceof OnContextClickListener) {
			onContextClickListener=(OnContextClickListener) listener;
		}
		
		if(listener instanceof OnDoubleTapListener) {
			onDoubleTapListener = (OnDoubleTapListener) listener;
		}
		
	}
	
	public boolean isLongpressEnabled() {
		return longpressEnabled;
	}
	
	private boolean moved, movedSensi, down, show, longPress, fling, doubleTap;
	private MotionEvent downEvent, moveEvent, upEvent, tapEvent;
	private Thread showDelay, longPressDelay, tapDelay;
	
	private static void interrupt(Thread thread) {
		if(thread!=null)
			thread.interrupt();
	}
	
	private void setReturning(boolean to) {
		if(!returning) {
			returning=to;
		}
	}
	
	private boolean returning;
	public boolean onTouchEvent(MotionEvent event) {
		
		int lx, ly, c;
		returning=false;
		
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
					down=true;
					show=false;
					moved=false;
					movedSensi=false;
					fling=false;
					longPress=false;
					downEvent=event;
					upEvent=null;
					doubleTap=false;
					
					if(onDoubleTapListener!=null && tapEvent!=null && event.getEventTime()-tapEvent.getEventTime()<=Config.getDoubleTapTimeout() &&
							Math.max(Math.abs(event.getPointerCoords().x - tapEvent.getPointerCoords().x), Math.abs(event.getPointerCoords().y - tapEvent.getPointerCoords().y)) <= Config.getDoubleTapDistance()) {
						doubleTap=true;
						interrupt(tapDelay);
						setReturning(onDoubleTapListener.onDoubleTap(event));
						setReturning(onDoubleTapListener.onDoubleTapEvent(event));
					}else {
						tapEvent=null;
						interrupt(tapDelay);
					}
					
					setReturning(onGestureListener.onDown(event));
					
					final MotionEvent event_thread = event;
					
					longPressDelay = new Thread(new Runnable() {
						public void run() {
							try {
								if(isLongpressEnabled()) {
									Thread.sleep(Config.getLongPressTimeout());
									longPress=true;
									onGestureListener.onLongPress(event_thread);
								}
							}catch(InterruptedException e) {}
						}
					});
					showDelay = new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(50);
								show=true;
								onGestureListener.onShowPress(event_thread);
							}catch(InterruptedException e) {}
						}
					});
					
					longPressDelay.start();
					showDelay.start();
			break;
			case MotionEvent.ACTION_MOVE:
				moved=true;
				moveEvent=event;
				
				if(!doubleTap && !movedSensi && (Math.abs(event.getPointerCoords().x-downEvent.getPointerCoords().x)>=Config.getLongpressTouchDistance() || Math.abs(event.getPointerCoords().y-downEvent.getPointerCoords().y)>=Config.getLongpressTouchDistance())) {
					movedSensi=true;
					interrupt(longPressDelay);
					interrupt(showDelay);
				}
					
				
				if(!longPress) {
					
					c = event.getHistorySize();
					lx=downEvent.getPointerCoords().x;
					ly=downEvent.getPointerCoords().y;
					if(c>1) {
						lx=event.getHistoricalPointerCoords(c-2).x;
						ly=event.getHistoricalPointerCoords(c-2).y;
					}
					
					setReturning(onGestureListener.onScroll(downEvent, event, event.getPointerCoords().x - lx, event.getPointerCoords().y - ly));
					
					if(doubleTap && onDoubleTapListener!=null) {
						setReturning(onDoubleTapListener.onDoubleTapEvent(event));
					}
				}
			break;
			case MotionEvent.ACTION_UP:
				down=false;
				interrupt(longPressDelay);
				interrupt(showDelay);
				
				upEvent=event;
				
				c = event.getHistorySize();
				lx=downEvent.getPointerCoords().x;
				ly=downEvent.getPointerCoords().y;
				if(c>1) {
					lx=event.getHistoricalPointerCoords(c-2).x;
					ly=event.getHistoricalPointerCoords(c-2).y;
				}
				
				int dist = (int)(Math.max(Math.abs(event.getPointerCoords().x-lx), Math.abs(event.getPointerCoords().y-ly)));
				if(!fling && dist>=Config.getMinimumFlingVelocity() && dist <=Config.getMaximumFlingVelocity()) {
					fling=true;
					setReturning(onGestureListener.onFling(moveEvent, event, event.getPointerCoords().x-lx, event.getPointerCoords().y-ly)) ;
				}
				
				if(tapEvent==null && !movedSensi && event.getEventTime()-downEvent.getEventTime()<=Config.getDoubleTapTimeout()) {
					tapEvent=event;
					
					if(onDoubleTapListener!=null) {
						tapDelay = new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(Config.getDoubleTapTimeout());
									setReturning(onDoubleTapListener.onSingleTapConfirmed(downEvent));
									tapEvent=null;
								}catch(InterruptedException e) {}
							}
						});
						
						tapDelay.start();
						}
					
					setReturning(onGestureListener.onSingleTapUp(event));
				}
				
				if(doubleTap && onDoubleTapListener!=null) {
					setReturning(onDoubleTapListener.onDoubleTapEvent(event));
				}
				
			break;
		}
		
		
		return returning;
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
