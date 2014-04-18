package koncept.kwiki.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

public class InputStreamLineIterator implements Iterator<String> {
	
	BufferedReader reader;
	String next;
	
	public InputStreamLineIterator(InputStream is) {
		this.reader = new BufferedReader(new InputStreamReader(is));
		scroll();
	}
	
	public InputStreamLineIterator(Reader in) {
		if (in instanceof BufferedReader) {
			reader = (BufferedReader)in;
		} else {
			reader = new BufferedReader(in);
		}
		scroll();
	}
	
	private void scroll() {
		try {
			next = reader.readLine();
			
			if (next == null) {
				reader.close();
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Unable to fully read character stream", e);
		}
	}
	
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return next != null;
	}
	
	public String next() {
		String next = this.next;
		scroll();
		return next;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	
}
