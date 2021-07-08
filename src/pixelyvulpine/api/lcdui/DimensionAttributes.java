package pixelyvulpine.api.lcdui;

public class DimensionAttributes{
	
	public static class Scaled{
		public byte x, y, width, height;
		public Scaled(int x, int y, int width, int height) {
			this.x = (byte)x;
			this.y = (byte)y;
			this.width=(byte)width;
			this.height=(byte)height;
		}
	}
	
	public static class Offset{
		public short x, y, width, height;
		public Offset(int x, int y, int width, int height) {
			this.x = (short)x;
			this.y = (short)y;
			this.width = (short)width;
			this.height = (short)height;
		}
	}
	
	protected Scaled scaled;
	protected Offset offset;
	
	public DimensionAttributes() {
		scaled = new Scaled(0,0,0,0);
		offset = new Offset(0,0,0,0);
	}
	
	public DimensionAttributes(Scaled scaled, Offset offset) {
		this.scaled=scaled;
		this.offset=offset;
	}
	
	public DimensionAttributes(Scaled scaled) {
		this.scaled=scaled;
		offset = new Offset(0,0,0,0);
	}
	
	public DimensionAttributes(Offset offset) {
		this.offset=offset;
		scaled = new Scaled(0,0,0,0);
	}
	
	public final Scaled getScaledDimension() {
		return scaled;
	}
	
	public final Offset getOffsetDimension() {
		return offset;
	}
	
}