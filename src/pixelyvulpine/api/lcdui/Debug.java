package pixelyvulpine.api.lcdui;

import java.util.Random;
import java.util.Vector;

import pixelyvulpine.Config;
import pixelyvulpine.api.util.GraphicsFix;

public class Debug {
	
	private static int lid;
	
	private int id;
	private Object represents;
	
	private static Vector threads = new Vector(5);
	
	private static class ThreadStackTrace{
		Thread thread;
		Vector traces = new Vector(10);
		String task;
		Vector log = new Vector(20);
	}
	
	private static class ObjectTrace{
		Object object;
		String message;
	}
	
	private static class Log{ //TODO: Get log
		Object object;
		short TraceID;
		String message;
	}
	
	public Debug(Object represents) {
		this.represents=represents;
		id=lid;
		lid++;
	}
	
	public static class Attributes{
		
		private Vector attributes = new Vector(10);
		
		public void showAttribute(String name, Object value) {
			attributes.addElement(new Attribute(name, value));
		}
		
		public static class Attribute{
			Attribute(String name, Object value){
				this.name=name;
				this.value=value;
			}
			public String name;
			public Object value;
		}
	}
	
	public static interface Watcher{
		public void watchAttributes(final Attributes attributes) ;
	}
	
	public void paint(GraphicsFix g) {
		
		if(!Config.getDebugViews()) return;
		
		Random r = new Random(getName().hashCode());
		g.setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		int cx = g.getClipX();
		int cy = g.getClipY();
		int cw = g.getClipWidth();
		int ch = g.getClipHeight();
		g.setClip(0, 0, g.getDimensionWidth()+1, g.getDimensionHeight()+1);
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
	
	public static int traceObject(Object object) {
		return traceObject(object, "");
	}
	
	public static int traceObject(Object object, String message) {
		ThreadStackTrace t = getThreadStackTrace();
		return addObjectTrace(object, message, t);
	}
	
	private static int addObjectTrace(Object object, String message, ThreadStackTrace t) {
		
		ObjectTrace ot = new ObjectTrace();
		ot.object=object;
		ot.message=message;
		
		t.traces.addElement(ot);
		return t.traces.size();
	}
	
	public static void returnToTrace(int id) {
		ThreadStackTrace t = getThreadStackTrace();
		for(int i=t.traces.size()-1; i>id; i--) {
			t.traces.removeElementAt(i);
		}
	}
	
	public static void cleanThreadTrace() {
		int i;
		for(i=threads.size()-1; i>=0; i--)
			if(threads.elementAt(i)!=null && ((ThreadStackTrace)threads.elementAt(i)).thread==Thread.currentThread())
				break;
		if(i>=0)
			cleanThreadTrace(i);
	}
	
	public static void setTask(String task) {
		ThreadStackTrace t = getThreadStackTrace();
		t.task=task;
	}
	
	private static void cleanThreadTrace(int index) {
		ThreadStackTrace t = (ThreadStackTrace)threads.elementAt(index);
		t.traces.removeAllElements();
		t.log.removeAllElements();
	}
	
	public static void cleanInactiveThreads() {
		for(int i=0; i<threads.size(); i++) {
			if(threads.elementAt(i)!=null && !((ThreadStackTrace)threads.elementAt(i)).thread.isAlive()) {
				cleanThreadTrace(i);
				threads.setElementAt(null, i);
			}
		}
	}
	
	public static void logd(Object message) {
		ThreadStackTrace t = getThreadStackTrace();
		Log log = new Log();
		log.object = t.traces.lastElement();
		log.message = message.toString();
		log.TraceID = (short)(t.traces.size()-1);
		t.log.addElement(log);
	}
	
	public static void getThreadTrace(StringBuffer sb) {
		ThreadStackTrace t = getThreadStackTrace();
		sb.append("\n");
		sb.append("Thread: ");
		sb.append(t.thread);
		sb.append("\n");
		sb.append("Task: ");
		sb.append(t.task);
		sb.append("\n");
		ObjectTrace trace;
		int sbSize = sb.length();
		
		try {
			for(int i=0; i<t.traces.size(); i++) {
				sb.append("\n");
				trace = (ObjectTrace)t.traces.elementAt(i);
				sb.append(trace.object.getClass().getName());
				if(trace.message!=null && !trace.message.equals("")) {
					sb.append(" - ");
					sb.append(trace.message);
				}
			}
		}catch(OutOfMemoryError e) {
			sb.delete(sbSize-1, sb.length());
			sb.append("\nCould not get Thread Trace log\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
			sb.append("Last trace: ");
			trace = (ObjectTrace)t.traces.elementAt(t.traces.size()-1);
			sb.append(trace.object.getClass().getName());
			if(trace.message!=null && !trace.message.equals("")) {
				sb.append(" - ");
				sb.append(trace.message);
			}
			sb.append("\n");
		}catch(Throwable e) {
			sb.append("\nCould not get Thread Trace log\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
			sb.append("Last trace: ");
			try {
				trace = (ObjectTrace)t.traces.elementAt(t.traces.size()-1);
				sb.append(trace.object.getClass().getName());
				if(trace.message!=null && !trace.message.equals("")) {
					sb.append(" - ");
					sb.append(trace.message);
				}
			}catch(Throwable e2) {
				sb.append("ERROR");
			}
			sb.append("\n");
		}
	}
	
	private static int getThreadIndex() {
		int i;
		for(i=threads.size()-1; i>=0; i--)
			if(threads.elementAt(i)!=null && ((ThreadStackTrace)threads.elementAt(i)).thread==Thread.currentThread())
				break;
			
		if(i<0) {
			ThreadStackTrace t = new ThreadStackTrace();
			t.thread = Thread.currentThread();
			t.task = "unknown";
			int ni = threads.indexOf(null);
			if(ni<0)
				threads.addElement(t);
			else
				threads.setElementAt(t, ni);
			addObjectTrace(Thread.currentThread(), "", t);
			return threads.size()-1;
		}
		return i;
	}
	
	private static ThreadStackTrace getThreadStackTrace() {
		return (ThreadStackTrace)threads.elementAt(getThreadIndex());
	}

}
