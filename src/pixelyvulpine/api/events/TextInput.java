package pixelyvulpine.api.events;

import pixelyvulpine.Config;

public class TextInput implements KeyEvent.Callback{

	public final CharSequence[] standardCharSequence= {
		new CharSequence('0', new char[] {' ', '\n', '0'}),
		new CharSequence('1', new char[] {'.', ',', '-', '?', '!', '@', '(', ')', '/', '_', '1'}),
		new CharSequence('2', new char[] {'a', 'b', 'c', '2'}),
		new CharSequence('3', new char[] {'d', 'e', 'f', '3'}),
		new CharSequence('4', new char[] {'g', 'h', 'i', '4'}),
		new CharSequence('5', new char[] {'j', 'k', 'l', '5'}),
		new CharSequence('6', new char[] {'m', 'n', 'o', '6'}),
		new CharSequence('7', new char[] {'p', 'q', 'r', 's', '7'}),
		new CharSequence('8', new char[] {'t', 'u', 'v', '8'}),
		new CharSequence('9', new char[] {'w', 'x', 'y', 'z', '9'})
	};
	
	public static interface OnTextInputListener{
		public boolean onCharAdded(char c) ;
		public boolean onCharChanged(char c);
		public void onCharFinished(char c);
		public void onCharErase();
	}
	
	private CharSequence[] charSequence = standardCharSequence;
	private OnTextInputListener listener;
	private KeyEvent lastEvent;
	private int clicks;
	protected Thread inputThread;
	protected Runnable inputRunnable = new Runnable() {
			public void run() {
				try {
					Thread.sleep(Config.getNumpadTextInputTimeout());
					if(lastEvent==null) return;
					if(listener!=null) {
						listener.onCharFinished(lastEvent.getChar());
					}
					lastEvent=null;
				}catch(InterruptedException e) {}
			}
	};
		
	protected void resetThread() {
		if(inputThread!=null) inputThread.interrupt();
		startThread();
	}
	
	protected void startThread() {
		inputThread = new Thread(inputRunnable);
		inputThread.start();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DEL:
				if(listener!=null) listener.onCharErase();
				if(inputThread!=null) inputThread.interrupt();
				lastEvent=null;
			return true;
		}
		
		if(listener!=null && getValidKey(keyCode)) {
			if(lastEvent==null || lastEvent.getChar() != event.getChar()) {
				
				if(lastEvent!=null) {
					listener.onCharFinished(lastEvent.getChar());
					if(inputThread!=null) inputThread.interrupt();
				}
				
				if(!listener.onCharAdded(getSelectedChar(event.getChar(), 0))) return false;
				
				lastEvent = event;
				startThread();
				clicks=0;
			}else {
				do {
					clicks++;
				}while(!listener.onCharChanged(getSelectedChar(event.getChar(), clicks)));
				resetThread();
			}
			return true;
		}
		
		return false;
	}

	public boolean onKeyRepeat(int keyCode, KeyEvent event) {
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DEL:
				if(listener!=null) listener.onCharErase();
			return true;
		}
		
		return finishSequence();
		
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(listener!=null && getValidKey(keyCode)) {
			
		}
		
		return false;
		
	}
	
	public boolean finishSequence() {
		
		if(listener!=null && lastEvent!=null && listener.onCharChanged(lastEvent.getChar())) {
			
			listener.onCharFinished(lastEvent.getChar());
			lastEvent=null;
			if(inputThread!=null) inputThread.interrupt();
			return true;
			
		}
		
		return false;
	}
	
	protected char getSelectedChar(char c, int index) {
		
		for(int i=0; i<charSequence.length; i++) {
			if(charSequence[i].key==c) {
				return charSequence[i].getChar(index);
			}
		}
		
		return c;
	}
	
	public final void setOnTextInputListener(OnTextInputListener listener) {
		this.listener=listener;
	}
	
	protected final boolean getValidKey(int keyCode) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				return false;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				return false;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				return false;
			case KeyEvent.KEYCODE_DPAD_UP:
				return false;
			case KeyEvent.KEYCODE_SOFT_LEFT:
				return false;
			case KeyEvent.KEYCODE_SOFT_RIGHT:
				return false;
			case KeyEvent.KEYCODE_ENDCALL:
				return false;
			case KeyEvent.KEYCODE_CALL:
				return false;
			case KeyEvent.KEYCODE_0:
				return true;
			case KeyEvent.KEYCODE_1:
				return true;
			case KeyEvent.KEYCODE_2:
				return true;
			case KeyEvent.KEYCODE_3:
				return true;
			case KeyEvent.KEYCODE_4:
				return true;
			case KeyEvent.KEYCODE_5:
				return true;
			case KeyEvent.KEYCODE_6:
				return true;
			case KeyEvent.KEYCODE_7:
				return true;
			case KeyEvent.KEYCODE_8:
				return true;
			case KeyEvent.KEYCODE_9:
				return true;
		}
		
		if(keyCode <0) return false;
		
		return true;
	}
	
	public static class CharSequence{
		public char key;
		public char[] sequence;
		
		public CharSequence(char key, char[] sequence) {
			this.key = key;
			this.sequence=sequence;
		}
		
		public char getChar(int sequence) {
			return this.sequence[sequence-this.sequence.length*(sequence/this.sequence.length)];
		}
	}

}
