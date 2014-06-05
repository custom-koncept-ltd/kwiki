package koncept.kwiki.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface WikiResource {
	
	/**
	 * Gets the metadata for this resource.<br>
	 * Can return null if there is 'no' metadata
	 * @return metadata, or null
	 */
	public WikiResourceDescriptor metadata();
	
	/**
	 * The full path to this resource (starting with a slash)
	 * @return
	 */
	public String getName();
	
	public boolean isListable();
	public List<WikiResource> list() throws IOException;
	
	public boolean isOpenable();
	public InputStream open() throws IOException;
	
}
