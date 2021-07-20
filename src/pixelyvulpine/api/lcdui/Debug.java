package pixelyvulpine.api.lcdui;

import java.util.Random;

import pixelyvulpine.Config;
import pixelyvulpine.api.util.GraphicsFix;

public class Debug {
	
	private static int lid;
	
	private int id;
	private Object represents;
	
	public Debug(Object represents) {
		this.represents=represents;
		id=lid;
		lid++;
	}
	
	public void paint(GraphicsFix g) {
		
		if(!Config.getDebugViews()) return;
		
		Random r = new Random(getName().hashCode());
		g.setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		int cx = g.getClipX();
		int cy = g.getClipY();
		int cw = g.getClipWidth();
		int ch = g.getClipHeight();
		g.setClip(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		g.drawRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		g.setClip(cx, cy, cw, ch);
	}
	
	public int getId() {
		return id;
	}
	
	public Object getRepresentedObject() {
		return represents;
	}
	
	public String getName() {
		return represents.getClass().getName();
	}

}
