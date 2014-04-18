package koncept.kwiki.core.resource.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.DocumentVersion;

public class SimpleFileSystemResource implements WikiResourceDescriptor {
	
	private final File document;
	private final SimpleFileSystemResourceLocator locator;
	
	public SimpleFileSystemResource(File document, SimpleFileSystemResourceLocator locator) {
		this.document = document;
		this.locator = locator;
	}

	public List<DocumentVersion> getVersions() {
		return Arrays.asList(new DocumentVersion(getDocumentName(), 0));
	}
	
	public WikiResource getCurrentVersion() {
		if (document.getName().endsWith(locator.getFileExtension())) {
			return new SimpleFileSystemWikiDocument(document, new DocumentVersion(getDocumentName(), 0), this);
		}
		return new SimpleFileSystemBinaryResource(document, new DocumentVersion(getDocumentName(), 0), this);
	}
	
	private String getDocumentName() {
		if (document.getName().endsWith(locator.getFileExtension())) {
			int trimLength = locator.getFileExtension().length();
			String documentName = document.getName().substring(0, document.getName().length() - trimLength);
			if (documentName.equals(locator.getDirectoryDefault())) {
				documentName = document.getParentFile().getName();
			}
			return documentName;
		}
		return document.getName();
	}
	
	public WikiResource getVersion(DocumentVersion version) {
		if (version.getDocumentVersion() == 0) {
			return getCurrentVersion();
		}
		throw new RuntimeException("No version to fetch");
	}
	
	

}
