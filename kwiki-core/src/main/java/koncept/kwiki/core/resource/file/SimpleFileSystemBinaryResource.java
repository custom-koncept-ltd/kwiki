package koncept.kwiki.core.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.BinaryResource;
import koncept.kwiki.core.document.DocumentVersion;

public class SimpleFileSystemBinaryResource implements BinaryResource {

	private final File document;
	private final DocumentVersion documentVersion;
	private final WikiResourceDescriptor meta;
	
	public SimpleFileSystemBinaryResource(File document, DocumentVersion documentVersion, WikiResourceDescriptor meta) {
		this.document = document;
		this.documentVersion = documentVersion;
		this.meta = meta;
	}

	public InputStream getBytes() {
		try {
			return new FileInputStream(document);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public WikiResourceDescriptor getResourceMeta() {
		return meta;
	}
	
	public DocumentVersion getDocumentVersion() {
		return documentVersion;
	}
}
