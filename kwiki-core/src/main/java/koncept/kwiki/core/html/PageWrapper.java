package koncept.kwiki.core.html;

import koncept.kwiki.core.WikiResource;

/**
 * Provides *basic* page wrapping functionality... <br/>
 * @author koncept
 *
 */
public class PageWrapper {

	public String wrap(String html, WikiResource page) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head><title>");
		sb.append(page.getDocumentVersion().getDocumentName());
		sb.append("</title></head><body>");
		
		sb.append(html);
		
		sb.append("</body>");
		
		return sb.toString();
		
	}
	
}
