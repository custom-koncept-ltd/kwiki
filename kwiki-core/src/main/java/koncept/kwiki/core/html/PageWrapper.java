package koncept.kwiki.core.html;

import java.io.IOException;
import java.io.InputStream;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;

public interface PageWrapper {

	public String wrap(KWiki kwiki, String html, WikiResource page) throws IOException;
	
	public InputStream css();
}
