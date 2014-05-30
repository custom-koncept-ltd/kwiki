package koncept.kwiki.core.resource.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.DocumentVersion;

public class SimpleFileSystemResourceDescriptor implements WikiResourceDescriptor {
	
	private final File document;
	
	public SimpleFileSystemResourceDescriptor(File document) {
		this.document = document;
	}

	public List<DocumentVersion> getVersions() {
		return Arrays.asList(new DocumentVersion(getDocumentName(), 0));
	}
	
	public WikiResource getCurrentVersion() {
		return new SimpleFileSystemWikiResource(document, new DocumentVersion(getDocumentName(), 0), this);
	}
	
	private String getDocumentName() {
		String docName = document.getName();
		int dotIndex = docName.lastIndexOf(".");
		if (dotIndex != -1)
			return docName.substring(0, dotIndex);
		return docName;
	}
	
	public WikiResource getVersion(DocumentVersion version) {
		if (version.getDocumentVersion() == 0) {
			return getCurrentVersion();
		}
		throw new RuntimeException("No version to fetch");
	}
	
	

}
