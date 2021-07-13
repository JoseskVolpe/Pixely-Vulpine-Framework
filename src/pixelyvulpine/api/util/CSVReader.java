package pixelyvulpine.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public final class CSVReader {
	private boolean loaded;
	
	private CSVLine columns;
	private Vector rows;
	
	public CSVReader() {}
	
	public void read(InputStream is) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		
		columns = new CSVLine(isr);
		boolean ready;
		ready=columns.read();
		
		rows=new Vector(0,1);
		
		CSVLine temp;
		while(!ready) {
			temp = new CSVLine(isr);
			ready=temp.read();
			rows.addElement(temp);
		}
		
		
		is.close();
		
		loaded=true;
	}
	
	public void erase() {
		if(!loaded) return;
		columns.erase();
		columns=null;
		for(int i=0; i<rows.size(); i++) {
			((CSVLine)rows.elementAt(i)).erase();
		}
		rows.removeAllElements();
		rows=null;
		
		loaded=false;
	}
	
	public CSVLine getColumns() {
		return columns;
	}
	
	public CSVLine[] getRows() {
		CSVLine[] array = new CSVLine[rows.size()];
		for(int i=0; i<rows.size(); i++)
			array[i]=(CSVLine)rows.elementAt(i);
		
		return array;
	}
	
	public CSVLine getRow(int line) {
		return (CSVLine)rows.elementAt(line);
	}
	
	public int getRowsLength() {
		return rows.size();
	}
	
	public String getValue(String column, int line) {
		return getValue(columns.indexOf(column), line);
	}
	
	public String getValue(int column, int line) {
		return getRow(line).getElement(column);
	}
	
	public final boolean isLoaded() {
		return loaded;
	}
	
	public final static class CSVLine{
		private Vector rows = new Vector(0,1);
		private InputStreamReader isr;
		
		private CSVLine(InputStreamReader isr){
			this.isr=isr;
			
		}
		
		private boolean read() throws IOException {
			int b;
			char c;
			StringBuffer sb = new StringBuffer();
			while(true) {
				if((b = isr.read()) == -1 | (c=(char)b)=='\n') {
					rows.addElement(sb.toString());
					
					if(b==-1)
						return true;
					else
						return false;
				}
				
				if(c==',') {
					rows.addElement(sb.toString());
					if(sb.length()>0)
						sb.delete(0,  sb.length());
					
					continue;
				}
				
				sb.append(c);
			}
		}
		
		public int length() {
			return rows.size();
		}
		
		private void erase() {
			if(rows==null) return;
			rows.removeAllElements();
			rows.trimToSize();
			rows=null;
		}
		
		public String getElement(int column) {
			return (String)rows.elementAt(column);
		}
		
		public int indexOf(String element) {
			for(int i=0; i<length(); i++) {
				if(((String)rows.elementAt(i)).equals(element))
					return i;
			}
			return -1;
		}
		
		public String[] toArray() {
			String[] array = new String[length()];
			for(int i=0; i<length(); i++)
				array[i]=getElement(i);
			
			return array;
		}
		
	}
}
