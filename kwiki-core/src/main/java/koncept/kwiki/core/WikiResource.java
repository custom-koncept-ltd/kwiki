package koncept.kwiki.core;

import java.io.InputStream;

import koncept.kwiki.core.document.DocumentVersion;

public interface WikiResource {
	
	public DocumentVersion getDocumentVersion();
	public WikiResourceDescriptor getResourceMeta();
	
	public String getType();
	public InputStream getStream();
	
}
