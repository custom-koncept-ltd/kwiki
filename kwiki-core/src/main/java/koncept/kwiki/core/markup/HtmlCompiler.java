package koncept.kwiki.core.markup;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.util.InputStreamToString;

/**
 * converts HTML to HTML.</br>
 * <br/>
 * <br/>
 * ... just streams the source file.<br/>
 * Allows the use (or importing) or html resources to be used natively
 * @author koncept
 *
 */
public class HtmlCompiler implements MarkupCompiler {

	public static final List<String> FILE_EXTENSIONS =  Arrays.asList("htm", "html");
	
	public boolean accepts(String type) {
		return FILE_EXTENSIONS.contains(type);
	}

	public String toHtml(WikiResource resource) throws Exception {
		InputStream is = resource.getStream();
		try {
			return InputStreamToString.convertStreamToString(resource.getStream());
		} finally {
			is.close();
		}
	}

}
