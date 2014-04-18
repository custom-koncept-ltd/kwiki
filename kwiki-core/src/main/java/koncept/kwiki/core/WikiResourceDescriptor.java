package koncept.kwiki.core;

import java.util.List;

import koncept.kwiki.core.document.DocumentVersion;


public interface WikiResourceDescriptor {

	public List<DocumentVersion> getVersions(); //includes current version
	
	public WikiResource getCurrentVersion();
	public WikiResource getVersion(DocumentVersion version);
}
