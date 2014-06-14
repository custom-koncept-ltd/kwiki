package koncept.kwiki.core.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;

public class SimpleFileSystemResource implements WikiResource {

	private final SimpleFileSystemResourceDescriptor metadata;
	private final File rootDirectory;
	private final File file;
	
	public SimpleFileSystemResource (File rootDirectory, File file, SimpleFileSystemResourceLocator resourceLocator) {
		this.rootDirectory = rootDirectory;
		this.file = file;
		metadata = new SimpleFileSystemResourceDescriptor(resourceLocator, this);
	}
	
	public SimpleFileSystemResource (File rootDirectory, File file, SimpleFileSystemResourceDescriptor metadata) {
		this.rootDirectory = rootDirectory;
		this.file = file;
		this.metadata = metadata;
	}
	
	@Override
	public WikiResourceDescriptor metadata() {
		return metadata;
	}
	
	@Override
	public String getName() {
		return file.getAbsolutePath().substring(rootDirectory.getAbsolutePath().length()).replaceAll("\\\\", "/");
	}

	@Override
	public boolean isListable() {
		return file.isDirectory();
	}

	@Override
	public List<WikiResource> list() {
		if (!isListable()) throw new IllegalStateException();
		List<WikiResource> resources = new ArrayList<>();
		List<File> files = new ArrayList<File>();
		Collections.addAll(files,  file.listFiles());
		Collections.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
			}
		});
		for(File child: files) {
			resources.add(new SimpleFileSystemResource(rootDirectory, child, metadata.getResourceLocator()));
		}
		return resources;
	}

	@Override
	public boolean isOpenable() {
		return file.isFile();
	}

	@Override
	public InputStream open() throws IOException {
		if (!isOpenable()) throw new IllegalStateException();
		return new FileInputStream(file);
	}

}
