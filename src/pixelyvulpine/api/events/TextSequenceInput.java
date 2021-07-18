package pixelyvulpine.api.events;

import pixelyvulpine.Config;

public class TextSequenceInput implements KeyEvent.Callback{
	
	public final static byte INPUT_SHIFT=0;
	public final static byte INPUT_LOWERCASE=1;
	public final static byte INPUT_CAPSLOCK=2;
	public final static byte INPUT_NUMERIC=3;

	public static final CharSequence[] standardCharSequence= {
		new CharSequence('0', new char[] {' ', '\n', '0'}),
		new CharSequence('1', new char[] {'.', ',', ':', ';', '+', '-', '?', '!', '@', '(', ')', '/', '_', '1'}),
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
	private CharSequence lastCharSeq;
	private short clicks;
	protected Thread inputThread;
	protected byte inputMode;
	
	protected boolean[] modes= {
		true,
		true,
		true,
		true
	};
	
	protected Runnable inputRunnable = new Runnable() {
			public void run() {
				try {
					Thread.sleep(Config.getNumpadTextInputTimeout());
					if(lastEvent==null) return;
					if(listener!=null) {
						listener.onCharFinished(lastEvent.getChar());
						if(inputMode==INPUT_SHIFT)
							setInputMode(INPUT_LOWERCASE);
					}
					lastEvent=null;
				}catch(InterruptedException e) {}
			}
	};
	
	public TextSequenceInput() {}
	public TextSequenceInput(CharSequence[] charSequence) {
		this.charSequence=charSequence;
	}
		
	protected void resetThread() {
		if(inputThread!=null) inputThread.interrupt();
		startThread();
	}
	
	protected void startThread() {
		inputThread = new Thread(inputRunnable);
		inputThread.start();
	}
	
	private boolean pound;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DEL:
				if(listener!=null) listener.onCharErase();
				if(inputThread!=null) inputThread.interrupt();
				lastEvent=null;
			return true;
			case KeyEvent.KEYCODE_POUND:
				pound=false;
			return true;
				
		}
		
		if(inputMode == INPUT_NUMERIC) {
			switch(keyCode) {
				case KeyEvent.KEYCODE_0:
					addChar('0');
					return true;
				case KeyEvent.KEYCODE_1:
					addChar('1');
					return true;
				case KeyEvent.KEYCODE_2:
					addChar('2');
					return true;
				case KeyEvent.KEYCODE_3:
					addChar('3');
					return true;
				case KeyEvent.KEYCODE_4:
					addChar('4');
					return true;
				case KeyEvent.KEYCODE_5:
					addChar('5');
					return true;
				case KeyEvent.KEYCODE_6:
					addChar('6');
					return true;
				case KeyEvent.KEYCODE_7:
					addChar('7');
					return true;
				case KeyEvent.KEYCODE_8:
					addChar('8');
					return true;
				case KeyEvent.KEYCODE_9:
					addChar('9');
					return true;
			}
		}
		
		if(listener!=null && getValidKey(keyCode)) {
			if(lastEvent==null || lastEvent.getChar() != event.getChar()) {
				
				if(lastEvent!=null) {
					char c = lastEvent.getChar();
					if(inputMode == INPUT_CAPSLOCK || inputMode == INPUT_SHIFT)
						c=String.valueOf(c).toUpperCase().charAt(0);
					
					listener.onCharFinished(c);
					if(inputThread!=null) inputThread.interrupt();
					lastEvent=null;
					if(inputMode==INPUT_SHIFT)
						setInputMode(INPUT_LOWERCASE);
				}
				
				CharSequence seq = getSequence(event.getChar());
				if(seq == null) if(listener.onCharAdded(event.getChar())){
					listener.onCharFinished(event.getChar());
					return true;
				}else {
					return false;
				}
				
				boolean added=false;
				short start=0;
				for(short i=0; i<seq.sequence.length; i++) {
					char c = seq.getChar(i);
					if(inputMode == INPUT_CAPSLOCK || inputMode == INPUT_SHIFT)
						c=String.valueOf(c).toUpperCase().charAt(0);
					
					if(listener.onCharAdded(c)) {
						added=true;
						start=i;
						break;
					}
				}
				
				if(!added) return false;
				
				resetThread();
				lastCharSeq = seq;
				lastEvent = event;
				clicks=start;
			}else {
				
				char c;
				
				do {
					clicks++;
					c = lastCharSeq.getChar(clicks);
					if(inputMode == INPUT_CAPSLOCK || inputMode == INPUT_SHIFT)
						c=String.valueOf(c).toUpperCase().charAt(0);
				}while(!listener.onCharChanged(c));
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
			case KeyEvent.KEYCODE_POUND:
				if(!pound) {
					addChar('#');
					pound=true;
				}
			return true;
			
		}
		
		if(!getValidKey(keyCode)) return false;
		
		if(lastEvent==null && getSequence(event.getChar())==null) {
			return listener.onCharAdded(event.getChar());
		}
		
		if(listener!=null && lastEvent!=null && listener.onCharChanged(lastEvent.getChar())) {
			
			listener.onCharFinished(lastEvent.getChar());
			lastEvent=null;
			if(inputThread!=null) inputThread.interrupt();
			
			return true;
			
		}
		
		return false;
		
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_POUND:
				if(!pound) {
					int newI = getInputMode()+1;
					if(newI>3) newI=0;
					setInputMode((byte)newI);
				}
			return true;
		}
		
		if(listener!=null && lastEvent!=null && getSequence(event.getChar())!=null && clicks<=0 && getValidKey(keyCode)) {
			startThread();
		}
		
		return false;
		
	}
	
	public final void addChar(char c) {
		if(listener.onCharAdded(c))
			listener.onCharFinished(c);
	}
	
	public boolean finishSequence() {
		
		if(listener!=null && lastEvent!=null) {
			
			listener.onCharFinished(lastCharSeq.getChar(clicks));
			lastEvent=null;
			if(inputThread!=null) inputThread.interrupt();
			
			return true;
			
		}
		
		return false;
	}
	
	public CharSequence getSequence(char c) {
		
		for(int i=0; i<charSequence.length; i++) {
			if(charSequence[i].key==c) {
				return charSequence[i];
			}
		}
		
		return null;
	}
	
	public CharSequence[] getCharSequences() {
		return charSequence;
	}
	
	public boolean isSelecting() {
		return lastEvent!=null;
	}
	
	public void setCharSequences(CharSequence[] charSequence) {
		this.charSequence=charSequence;
	}
	
	public final void setOnTextInputListener(OnTextInputListener listener) {
		this.listener=listener;
	}
	
	public final void setInputMode(byte inputMode) {
		
		inputMode = (byte)(inputMode-modes.length*(inputMode/modes.length));
		
		if(!modes[inputMode]) {
			setInputMode((byte)(inputMode+1));
			return;
		}
		
		this.inputMode = inputMode;
	}
	
	public final byte getInputMode() {
		return inputMode;
	}
	
	public final void enableMode(byte inputMode) {
		modes[inputMode]=true;
	}
	
	public final void disableMode(byte inputMode) {
		modes[inputMode]=false;
	}
	
	public final void resetModes() {
		for(int i=0; i<modes.length; i++) {
			modes[i]=false;
		}
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
