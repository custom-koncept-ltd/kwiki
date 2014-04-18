package koncept.kwiki.core.resource.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.DocumentVersion;
import koncept.kwiki.core.document.WikiDocument;
import koncept.kwiki.core.util.InputStreamLineIterator;

public class SimpleFileSystemWikiDocument implements WikiDocument {

	private final File document;
	private final DocumentVersion documentVersion;
	private final WikiResourceDescriptor meta;
	
	public SimpleFileSystemWikiDocument(File document, DocumentVersion documentVersion, WikiResourceDescriptor meta) {
		this.document = document;
		this.documentVersion = documentVersion;
		this.meta = meta;
	}

	public Iterable<String> getLines() {
		return new Iterable<String>() {
			public Iterator<String> iterator() {
				try {
					return new InputStreamLineIterator(new FileReader(document));
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		};
		
	}
	
	public WikiResourceDescriptor getResourceMeta() {
		return meta;
	}
	
	public DocumentVersion getDocumentVersion() {
		return documentVersion;
	}

}
