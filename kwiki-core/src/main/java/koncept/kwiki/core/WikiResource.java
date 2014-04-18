package koncept.kwiki.core;

import koncept.kwiki.core.document.DocumentVersion;

public interface WikiResource {
	
	public DocumentVersion getDocumentVersion();
	public WikiResourceDescriptor getResourceMeta();
	
}
