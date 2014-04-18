package koncept.kwiki.core.document;

import java.io.InputStream;

import koncept.kwiki.core.WikiResource;

public interface BinaryResource extends WikiResource {

	public InputStream getBytes();
	
}
