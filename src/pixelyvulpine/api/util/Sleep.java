package pixelyvulpine.api.util;

public class Sleep {
	
	public static final void sleep(long millis){
		
		try {
			Thread.sleep(millis);
		}catch(InterruptedException e) {
			/*ignore*/
		}
		
	}

}
