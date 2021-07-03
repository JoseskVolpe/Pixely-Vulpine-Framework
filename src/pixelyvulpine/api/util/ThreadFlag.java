package pixelyvulpine.api.util;

public class ThreadFlag {
	
	private boolean terminated=false;
	
	public void Terminate() {
		terminated=true;
	}
	
	public boolean isTerminated() {
		return terminated;
	}

}
