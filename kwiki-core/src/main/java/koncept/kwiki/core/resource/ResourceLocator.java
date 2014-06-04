package koncept.kwiki.core.resource;

import java.util.List;

import koncept.kwiki.core.WikiResourceDescriptor;

public interface ResourceLocator {

	public WikiResourceDescriptor getResource(String resourceName);
	
	
	public interface ResourceListing {
		public String getResourceName();
		public List<ResourceListing> getChildResources();
	}
	
}
