package pixelyvulpine.test.scenes;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import pixelyvulpine.Config;
import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Content.OnTouchListener;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;
import pixelyvulpine.test.App;

public class TouchTest extends Layout{

	private Label touchInfo;
	private Button bulge;
	
	private StringBuffer downInfo=new StringBuffer(), moveInfo=new StringBuffer(), upInfo=new StringBuffer();
	
	private TouchListener listener = new TouchListener();
	private GestureDetector gesture = new GestureDetector(this, listener);
	
	private boolean screen;
	
	public TouchTest(App app) {
		super(app);
		
		animation = ANIMATION_SLIDE_LEFT;
		
		this.setTitle("¡Boop your phone!");
		
		Label l = new Label(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,0,90,40)), this.getTitle());
		l.setColor(new Color(255,255,255));
		l.setFontStyle(Font.STYLE_BOLD);
		l.impact();
		l.setPositioning(Content.POSITIONING_ANCHORED);
		l.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		Canvas LCanvas = new Canvas(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0,0, 0, l.getFont().getHeight())));
		LCanvas.addContent(l);
		LCanvas.setBackgroundColor(null);
		LCanvas.setForegroundColor(null);
		this.addContent(LCanvas);
		
		bulge = new Button(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100, 0), new DimensionAttributes.Offset(0,0,0,Font.getDefaultFont().getHeight())), "Bulgue 7w7");
		bulge.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(Content view, MotionEvent event) {
				screen=false;
	            gesture.onTouchEvent(event);
				return true;
			}
			
		});
		addContent(bulge);
		
		touchInfo = new Label(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0,0, 0, -45)));
		touchInfo.setText("");
		touchInfo.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN, Font.SIZE_SMALL));
		touchInfo.setColor(new Color(255,255,255));
		touchInfo.setMultiline(true);
		addContent(touchInfo);
		
	}
	
	public void Setup() {
		
		Config.setShowTouch(true);
		setFullScreenMode(true);
		
	}
	
	public void paintLayout(Graphics g) {
		
		
		
	}

	protected boolean onTouchEvent(MotionEvent event) {
		
		screen=true;
		gesture.onTouchEvent(event);
		
		return true;
		
	}
	
	private void updateInfo(String message, MotionEvent e) {
		
		StringBuffer info=null;
		switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				info = downInfo;
				break;
			case MotionEvent.ACTION_MOVE:
				info= moveInfo;
				break;
				
			case MotionEvent.ACTION_UP:
				info= upInfo;
				break;
		}
		
		System.out.println(MotionEvent.actionToString(e.getAction())+":\n"+message);
		
		if(info.length()>0)
			info.delete(0, info.length());
		
		info.append(message);
		touchInfo.setText("Screen touch: "+String.valueOf(screen)+"\nDown:\n"+downInfo+"\n\nMove:\n"+moveInfo+"\n\nUp:\n"+upInfo);
		
		
	}
	
	class TouchListener extends GestureDetector.SimpleOnGestureListener{
		public boolean onDown(MotionEvent e) {
			updateInfo("onDown()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			updateInfo("onFling()\n"+e1.getPointerCoords().x+"X"+e1.getPointerCoords().y+"\n"+e2.getPointerCoords().x+"X"+e2.getPointerCoords().y+"\n"+velocityX+"X"+velocityY, e2);
			return false;
		}

		public void onLongPress(MotionEvent e) {
			updateInfo("onLongPress()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			updateInfo("onScroll()\n"+e1.getPointerCoords().x+"X"+e1.getPointerCoords().y+"\n"+e2.getPointerCoords().x+"X"+e2.getPointerCoords().y+"\n"+distanceX+"X"+distanceY, e2);
			return false;
		}

		public void onShowPress(MotionEvent e) {
			updateInfo("onShowPress()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
		}

		public boolean onSingleTapUp(MotionEvent e) {
			updateInfo("onSingleTapUp()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}

		public boolean onDoubleTap(MotionEvent e) {
			updateInfo("onDoubleTap()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}

		public boolean onDoubleTapEvent(MotionEvent e) {
			updateInfo("onDoubleTapEvent()\n"+MotionEvent.actionToString(e.getAction())+"\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}

		public boolean onSingleTapConfirmed(MotionEvent e) {
			updateInfo("onSingleTapConfirmed()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}

		public boolean onContextClick(MotionEvent e) {
			updateInfo("onContextClick()\n"+e.getPointerCoords().x+"X"+e.getPointerCoords().y, e);
			return false;
		}
	}

}
