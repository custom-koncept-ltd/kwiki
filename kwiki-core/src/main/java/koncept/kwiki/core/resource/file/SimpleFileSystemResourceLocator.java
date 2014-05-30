package koncept.kwiki.core.resource.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.resource.ResourceLocator;

public class SimpleFileSystemResourceLocator implements ResourceLocator {

	public static final String DEFAULT_DIRECTORY_FILE = "index";

	private final File baseDirectory;
	private String directoryDefault = DEFAULT_DIRECTORY_FILE;

	public SimpleFileSystemResourceLocator(File baseDirectory) {
		this.baseDirectory = baseDirectory;
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
		return new SimpleFileSystemResourceDescriptor(file);
	}

	protected File getWikiResource(String resourceName) {
		String fileName = null;
		String path = null;
		
		int lastSlash = resourceName.lastIndexOf("/");
		if (lastSlash == -1) {
			fileName = resourceName;
			path = "";
		} else {
			path = resourceName.substring(0, lastSlash);
			fileName = resourceName.substring(lastSlash + 1);
		}
		return getWikiResource(path, fileName, true);
	}
	
	protected File getWikiResource(final String path, final String fileName, boolean directoryPeek) {
		File relativeRoot = new File(baseDirectory, path);
		if (!relativeRoot.exists()) return null; //no path - don't bother looking for files
		File[] fileArray = relativeRoot.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (fileName.equals(name)) return true;
				int dotIndex = name.lastIndexOf(".");
				if (dotIndex == -1) return false;
				return fileName.equals(name.substring(0, dotIndex));
			}
		});
		List<File> files = new ArrayList<File>();
		Collections.addAll(files,  fileArray);
		Collections.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
			}
		});
		
		for(File file: files) {
			if (file.isFile())
				return file;
			if (directoryPeek && file.getName().equals(fileName) && file.isDirectory()) {
				File inner = getWikiResource(path + "/" + fileName, directoryDefault, false);
				if (inner != null)
					return inner;
			}
		}
		return null;
	}

}
