package koncept.kwiki.core.markup;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.util.InputStreamToString;

/**
 * converts text to html by wrapping it in a 'pre' tag
 * @author nicholas.krul@gmail.com
 */
public class PlaintextCompiler implements MarkupCompiler {

	public static final List<String> FILE_EXTENSIONS =  Arrays.asList("txt", "text");
	
	public List<String> fileTypes() {
		return FILE_EXTENSIONS;
	}

	public String toHtml(WikiResource resource) throws Exception {
		InputStream is = null;
		try {
			is = resource.open();
			return "<pre>" + 
					ensureNonClosingHtml(
							InputStreamToString.convertStreamToString(
									is))
					+ "</pre>";
		} finally {
			if (is != null)
				is.close();
		}
	}
	
	private String ensureNonClosingHtml(String in) {
		return in;
	}

}
