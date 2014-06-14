package koncept.kwiki.core.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import koncept.kwiki.core.WikiResource;

public class WikiResourceAssertions {

	public static void assertResourceDetails(List<WikiResource> resources) {
		for(WikiResource resource: resources)
			assertResourceDetails(resource);
	}
	
	
	public static void assertResourceDetails(WikiResource resource) {
		assertNotNull(resource);
		assertName(resource.getName());
		
	}
	
	
	private static void assertName(String name) {
		assertNotNull(name);
		assertFalse("contains a backslash: " + name, name.contains("\\"));
		assertTrue("must start with a slash: " + name, name.startsWith("/"));
		assertFalse("must not have two slashes in a row: " + name, name.contains("//"));
	}
	
}
