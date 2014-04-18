package koncept.kwiki.core.document;

import koncept.kwiki.core.WikiResource;

public interface WikiDocument extends WikiResource {

	public Iterable<String> getLines();
	
}
