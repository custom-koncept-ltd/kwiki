package koncept.kwiki.core.html;

import java.io.IOException;
import java.util.List;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.KWikiListing;
import koncept.kwiki.core.WikiResource;

/**
 * Provides *basic* page wrapping functionality... <br/>
 * @author koncept
 *
 */
public class FullLeftbarMenu implements PageWrapper {

	public String wrap(KWiki kwiki, String html, WikiResource page) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head><title>");
		sb.append(page.metadata().currentVersion().getDocumentName());
		sb.append("</title></head><body>");
		
		sb.append("<div>");
		sb.append(getListMenu(kwiki.getListing()));
		sb.append("</div>");
		
		sb.append("<div>");
		sb.append(html);
		sb.append("</div>");
		
		sb.append("</body>");
		
		return sb.toString();
		
	}
	
	
	public String getListMenu(List<KWikiListing> listings) {
		if (listings.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for(KWikiListing listing: listings) {
			sb.append("<li>");
			sb.append("<a href=\"");
			sb.append(listing.getResourcePath());
			sb.append("\">");
			sb.append(listing.getLocalName());
			sb.append(getListMenu(listing.getChildren()));
			sb.append("</a>");
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	
}
