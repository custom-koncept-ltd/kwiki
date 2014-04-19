package koncept.kwiki.core.resource.file;

import java.io.File;

import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.resource.ResourceLocator;

public class SimpleFileSystemResourceLocator implements ResourceLocator {

	public static final String DEFAULT_EXTENSION = ".kwiki"; //sweble's is wikitext
	public static final String DEFAULT_DIRECTORY_FILE = "index";

	private final File baseDirectory;
	private String fileExtension = DEFAULT_EXTENSION;
	private String directoryDefault = DEFAULT_DIRECTORY_FILE;

	public SimpleFileSystemResourceLocator(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFileExtension() {
		return fileExtension;
	}

	public void setDirectoryDefault(String directoryDefault) {
		this.directoryDefault = directoryDefault;
	}
	public String getDirectoryDefault() {
		return directoryDefault;
	}

	public WikiResourceDescriptor getResource(String resourceName) {
		if (resourceName.endsWith("/") || resourceName.endsWith("\\"))
			resourceName = resourceName.substring(0, resourceName.length() - 1);
		File file = getWikiResource(resourceName);
		if (file == null)
			return null;
		return new SimpleFileSystemResource(file, this);
	}

	protected File getWikiResource(String resourceName) {
		File file = new File(baseDirectory, resourceName + fileExtension);
		if (file.exists())
			return file;
		file = new File(new File(baseDirectory, resourceName), directoryDefault
				+ fileExtension);
		if (file.exists())
			return file;
		return null;
	}

}
