package koncept.kwiki.core.resource.file;

import java.io.File;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.resource.ResourceLocator;

public class SimpleFileSystemResourceLocator implements ResourceLocator {
	private final File baseDirectory;


	public SimpleFileSystemResourceLocator(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public List<WikiResource> rootListing() {
		return new SimpleFileSystemResource(baseDirectory, baseDirectory, this).list();
	}
	
	public WikiResource getResource(String resourceName) {
		resourceName = resourceName.substring(1);
		File file = new File(baseDirectory, resourceName);
		if (file.exists()) return new SimpleFileSystemResource(baseDirectory, file, this);
		return null;
	}

}
