package koncept.kwiki.core.resource;

import java.util.List;

import koncept.kwiki.core.WikiResource;

public interface ResourceLocator {
	
	/**
	 * @return a directory listing of resources available in this resource locator
	 */
	public List<WikiResource> rootListing();
	
	/**
	 * Expects a URL-like resourceName<br>
	 * these will all resolve to *DIFFERENT* resource: <ul>
	 * <li>/</li>
	 * <li>/info</li>
	 * <li>/info/</li>
	 * <li>/info/url</li>
	 * </ul><br>
	 * 
	 * <b>Note</b> that this is the same as the way apache resolves directories.<br>
	 * Therefore, the following should resolve to the same file (assuming index.html as the default listing name):
	 * <ul><li>/listing/</li></listing/index</li></ul>
	 * 
	 * @param resourceName
	 * @return the found resource, or null if it doesn't exist
	 */
	public WikiResource getResource(String resourceName);

	
}
