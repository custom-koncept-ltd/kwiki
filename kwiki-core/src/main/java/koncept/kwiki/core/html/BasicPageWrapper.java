package koncept.kwiki.core.html;

import java.io.InputStream;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;

/**
 * Provides *basic* page wrapping functionality... <br/>
 * @author koncept
 *
 */
public class BasicPageWrapper implements PageWrapper {

	public String wrap(KWiki kwiki, String html, WikiResource page) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head><title>");
		sb.append(page.metadata().currentVersion().getDocumentName());
		sb.append("</title></head><body>");
		
		sb.append(html);
		
		sb.append("</body>");
		
		return sb.toString();
		
	}
	
	@Override
	public InputStream css() {
		return null;
	}
	
}
