package koncept.kwiki.core.resource;

import koncept.kwiki.core.WikiResourceDescriptor;

public interface ResourceLocator {

	public WikiResourceDescriptor getResource(String resourceName);
	
}
