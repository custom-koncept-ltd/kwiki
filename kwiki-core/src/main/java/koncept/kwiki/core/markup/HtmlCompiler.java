package koncept.kwiki.core.markup;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.util.InputStreamToString;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * converts HTML to HTML.</br>
 * <br/>
 * <br/>
 * ... just streams the source file.<br/>
 * Allows the use (or importing) or html resources to be used natively
 * @author nicholas.krul@gmail.com
 *
 */
public class HtmlCompiler implements MarkupCompiler {

	public static final List<String> FILE_EXTENSIONS =  Arrays.asList("htm", "html");
	
	public List<String> fileTypes() {
		return FILE_EXTENSIONS;
	}

	public String toHtml(WikiResource resource) throws Exception {
		String html = readString(resource);
		Document htmlDoc = Jsoup.parse(html);
		return htmlDoc.body().html();
	}
	
	public String readString(WikiResource resource) throws Exception {
		InputStream is = null;
		try {
			is = resource.open();
			return InputStreamToString.convertStreamToString(is);
		} finally {
			if (is != null)
				is.close();
		}
	}

}
