package koncept.kwiki.core;

import java.util.List;

import koncept.kwiki.core.document.DocumentVersion;


public interface WikiResourceDescriptor {

	List<DocumentVersion> previousVersions();
	DocumentVersion currentVersion();
	
	WikiResource get(DocumentVersion version);

}
