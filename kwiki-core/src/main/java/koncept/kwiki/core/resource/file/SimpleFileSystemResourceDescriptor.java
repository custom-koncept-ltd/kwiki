package koncept.kwiki.core.resource.file;

import java.util.Collections;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.DocumentVersion;

public class SimpleFileSystemResourceDescriptor implements WikiResourceDescriptor {
	
	private final SimpleFileSystemResourceLocator resourceLocator;
	private final WikiResource currentVersion;
	
	public SimpleFileSystemResourceDescriptor(SimpleFileSystemResourceLocator resourceLocator, WikiResource currentVersion) {
		this.resourceLocator = resourceLocator;
		this.currentVersion = currentVersion;
	}
	
	@Override
	public List<DocumentVersion> previousVersions() {
		return Collections.emptyList();
	}
	
	@Override
	public DocumentVersion currentVersion() {
		//TODO: use the last / bits
		return new DocumentVersion(currentVersion.getName(), 0);
	}
	
	@Override
	public WikiResource get(DocumentVersion version) {
		return currentVersion;
	}
	
	protected SimpleFileSystemResourceLocator getResourceLocator() {
		return resourceLocator;
	}
	

}
