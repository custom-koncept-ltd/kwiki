package koncept.kwiki.core.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.DocumentVersion;

public class SimpleFileSystemWikiResource implements WikiResource {

	private final File document;
	private final DocumentVersion documentVersion;
	private final WikiResourceDescriptor meta;
	
	public SimpleFileSystemWikiResource(File document, DocumentVersion documentVersion, WikiResourceDescriptor meta) {
		this.document = document;
		this.documentVersion = documentVersion;
		this.meta = meta;
	}

	public InputStream getStream() {
		try {
			return new FileInputStream(document);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getType() {
		String docName = document.getName();
		int dotIndex = docName.lastIndexOf(".");
		if (dotIndex != -1)
			return docName.substring(dotIndex + 1);
		return "";
	}

	public WikiResourceDescriptor getResourceMeta() {
		return meta;
	}
	
	public DocumentVersion getDocumentVersion() {
		return documentVersion;
	}
}
