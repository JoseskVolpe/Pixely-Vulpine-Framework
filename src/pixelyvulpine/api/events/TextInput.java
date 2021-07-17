package pixelyvulpine.api.events;

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
	
	private CharSequence[] charSequence = standardCharSequence;
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onKeyRepeat(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
	public static class CharSequence{
		public char key;
		public char[] sequence;
		
		public CharSequence(char key, char[] sequence) {
			this.key = key;
			this.sequence=sequence;
		}
	}

}
