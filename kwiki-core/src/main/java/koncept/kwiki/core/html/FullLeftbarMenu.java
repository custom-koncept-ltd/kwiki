package koncept.kwiki.core.html;

import java.io.IOException;
import java.io.InputStream;
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

	private static final String nl = "\n";
	
	public String wrap(KWiki kwiki, String html, WikiResource page) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head>");
		sb.append(nl);
		sb.append("<title>");
		sb.append(page.metadata().currentVersion().getDocumentName());
		sb.append("</title>");
		sb.append(nl);
		sb.append("<link rel=\"stylesheet\" href=\"/style.css\" type=\"text/css\"/>");
		sb.append(nl);
		sb.append("</head><body>");
		sb.append(nl);		

		sb.append("<div id=\"navbar\">");
		sb.append(nl);
		sb.append(getListMenu(" ", kwiki.getListing()));
		sb.append("</div>");
		sb.append(nl);
		
		sb.append("<div id=\"main\">");
		sb.append(nl);
		sb.append(html);
		sb.append(nl);
		sb.append("</div>");
		sb.append(nl);
		sb.append("</body>");
		sb.append(nl);
		return sb.toString();
		
	}
	
	
	public String getListMenu(String offset, List<KWikiListing> listings) {
		if (listings.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(offset);
		sb.append("<ul>");
		sb.append(nl);
		for(KWikiListing listing: listings) {
			sb.append(offset);
			sb.append("<li>");
			sb.append("<a href=\"");
			sb.append(listing.getResourcePath());
			sb.append("\">");
			sb.append(listing.getLocalName());
			sb.append("</a>");
			
			if (!listing.getChildren().isEmpty()) {
				sb.append(nl);
				sb.append(getListMenu(offset + " ", listing.getChildren()));
				sb.append(offset);
			}
			
			sb.append("</li>");
			sb.append(nl);
		}
		sb.append(offset);
		sb.append("</ul>");
		sb.append(nl);
		return sb.toString();
	}
	
	@Override
	public InputStream css() {
		return getClass().getResourceAsStream("/FullLeftbarMenu.css");
	}
}
