package pixelyvulpine.api.lcdui;

import java.util.Random;
import java.util.Vector;

import pixelyvulpine.Config;
import pixelyvulpine.api.lcdui.Debug.Attributes.Attribute;
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
		String name;
	}
	
	private static class Log{
		Object object;
		short TraceID;
		String traceName;
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
		
		public void showAttribute(String name, boolean value) {
			showAttribute(name, String.valueOf(value));
		}
		
		public void showAttribute(String name, long value) {
			showAttribute(name, String.valueOf(value));
		}
		
		public void showAttribute(String name, double value) {
			showAttribute(name, String.valueOf(value));
		}
		
		public void showAttribute(String name, char value) {
			showAttribute(name, String.valueOf(value));
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
	
	private static int addObjectTrace(Object object, String name, ThreadStackTrace t) {
		
		ObjectTrace ot = new ObjectTrace();
		ot.object=object;
		ot.name=name;
		
		t.traces.addElement(ot);
		return t.traces.size()-1;
	}
	
	public static void removeFromTrace(int id) {
		
		ThreadStackTrace t = getThreadStackTrace();
		for(int i=t.traces.size()-1; i>=id; i--) 
			t.traces.removeElementAt(i);
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
		
		t.task = "unknown";
		addObjectTrace(Thread.currentThread(), "", t);
		
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
		log.object = ((ObjectTrace)t.traces.lastElement()).object;
		log.message = message.toString();
		log.TraceID = (short)(t.traces.size()-1);
		log.traceName = ((ObjectTrace)t.traces.elementAt(log.TraceID)).name;
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
		sb.append("Traces:");
		ObjectTrace trace;
		int sbSize = sb.length();
		
		try {
			for(int i=0; i<t.traces.size(); i++) {
				sb.append("\n");
				sb.append(i);
				sb.append(" - ");
				trace = (ObjectTrace)t.traces.elementAt(i);
				sb.append(trace.object.getClass().getName());
				if(trace.name!=null && !trace.name.equals("")) {
					sb.append(" - ");
					sb.append(trace.name);
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
			if(trace.name!=null && !trace.name.equals("")) {
				sb.append(" - ");
				sb.append(trace.name);
			}
			sb.append("\n");
		}catch(Throwable e) {
			sb.append("\nCould not get full Thread Trace log\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
			sb.append("Last trace: ");
			try {
				trace = (ObjectTrace)t.traces.elementAt(t.traces.size()-1);
				sb.append(trace.object.getClass().getName());
				if(trace.name!=null && !trace.name.equals("")) {
					sb.append(" - ");
					sb.append(trace.name);
				}
			}catch(Throwable e2) {
				sb.append("ERROR");
			}
			sb.append("\n");
		}
	}
	
	public static void getTraceLog(StringBuffer sb) {
		getLog(sb, getThreadStackTrace().traces);
	}
	
	public static void getThreadLog(StringBuffer sb) {
		getLog(sb, null);
	}
	
	private static void getLog(StringBuffer sb, Vector trace) {
		ThreadStackTrace t = getThreadStackTrace();
		
		int sbSize = sb.length();
		int added=0;
		Log log;
		try {
			for(int i=0; i<t.log.size(); i++) {
				
				log = (Log)t.log.elementAt(i);
				
				if(trace!=null && !traceHasObject(trace, log.object))
					continue;
				
				sb.append("\n* ");
				sb.append(log.object.getClass().getName());
				sb.append(" from trace \"");
				sb.append(log.traceName);
				sb.append("\" (ID ");
				sb.append(log.TraceID);
				sb.append("):\n=>     ");
				StringBuffer message=new StringBuffer();
				for(int j=0; j<log.message.length(); j++) {
					message.append(log.message.charAt(j));
					if(log.message.charAt(j)=='\n')
						message.append("       ");
				}
				sb.append(message.toString());
				added++;
			}
			
			if(added<=0)
				sb.append("\nLog is empty");
		}catch(OutOfMemoryError e) {
			sb.delete(sbSize-1, sb.length());
			sb.append("\nCould not get log\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
		}catch(Throwable e) {
			sb.append("\nCould not get full log\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
		}
	}
	
	public static void watchLastTrace(StringBuffer sb) {
		ThreadStackTrace t = getThreadStackTrace();
		
		try {
			ObjectTrace ot = (ObjectTrace) t.traces.elementAt(t.traces.size()-1);
			sb.append("\nWatching ");
			sb.append(ot.object.getClass().getName());
			sb.append(":");
			if(ot.object instanceof Watcher) {
				Attributes a = new Attributes();
				((Watcher) ot.object).watchAttributes(a);
				
				if(a.attributes.size()>0) {
					
					Attributes.Attribute at;
					for(int i=0; i<a.attributes.size(); i++) {
						at = (Attribute) a.attributes.elementAt(i);
						sb.append("\n* ");
						sb.append(at.name);
						sb.append(" = ");
						sb.append(at.value);
					}
					
					return;
				}
			}
			
			sb.append("\nObject has no watchable attribute or is not Debug.Watcher implemented");
			
		}catch(Throwable e) {
			sb.append("\nCould not watch last trace\n");
			sb.append(e.toString());
			sb.append(": ");
			sb.append(e.getMessage());
			sb.append("\n");
		}
	}
	
	private static boolean traceHasObject(Vector traces, Object obj) {
		for(int i=0; i<traces.size(); i++) {
			if(((ObjectTrace)traces.elementAt(i)).object==obj)
				return true;
		}
		
		return false;
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
