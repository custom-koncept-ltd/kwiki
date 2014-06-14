package koncept.kwiki.core.resource.file;

import static koncept.kwiki.core.resource.WikiResourceAssertions.assertResourceDetails;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;
import java.util.List;

import koncept.kwiki.core.WikiResource;

import org.junit.Before;
import org.junit.Test;

public class SimpleFileSystemResourceLocatorTest {

	SimpleFileSystemResourceLocator locator;
	File testResourcesDir;
	
	@Before
	public void init() throws Exception {
		URL fileUrl = getClass().getClassLoader().getResource("KwikiRoot.wikitext");
		testResourcesDir = new File(fileUrl.getFile()).getParentFile();
		locator = new SimpleFileSystemResourceLocator(testResourcesDir);
	}
	
	@Test
	public void rootListing() throws Exception {
		List<WikiResource> rootListing = locator.rootListing();
		assertNotNull(rootListing);
		assertThat(rootListing.size(), is(4)); //there are 4 directory entries
		assertResourceDetails(rootListing);
	}
	

	
	
	
}
