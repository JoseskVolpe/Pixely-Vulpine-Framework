package pixelyvulpine.api.lcdui;

public interface ContentListener {
	
	/**
	 * Called when a content of this interface is pressed
	 * @param content
	 * @param key
	 * @return true if nothing happens and you wish to keep the background commands
	 */
	public boolean contentPressed(Content content);
	public boolean onContentLoad(Content content);
	public boolean onContentError(Content content, Throwable e);
	
}
