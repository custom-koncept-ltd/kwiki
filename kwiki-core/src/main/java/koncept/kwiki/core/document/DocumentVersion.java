package koncept.kwiki.core.document;

public class DocumentVersion {

	private final String documentName;

	/**
	 * A documentVersion of 0 indicates 'none' or 'current'
	 */
	private final int documentVersion;

	public DocumentVersion(String documentName, int documentVersion) {
		this.documentName = documentName;
		this.documentVersion = documentVersion;
	}
	
	public String getDocumentName() {
		return documentName;
	}
	
	public int getDocumentVersion() {
		return documentVersion;
	}

}
