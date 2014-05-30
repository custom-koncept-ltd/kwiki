package koncept.kwiki.core.util;

public class InputStreamToString {

//	thanks to http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static String convertStreamToString(java.io.InputStream is, String encoding) {
	    java.util.Scanner s = new java.util.Scanner(is, encoding).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
}
