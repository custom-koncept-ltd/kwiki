package koncept.kwiki.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.List;

import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;

import org.junit.Before;
import org.junit.Test;

public class KWikiListingTest {

	KWiki kwiki;
	File testResourcesDir;
	
	@Before
	public void init() throws Exception {
		URL fileUrl = getClass().getClassLoader().getResource("KwikiRoot.wikitext");
		testResourcesDir = new File(fileUrl.getFile()).getParentFile();
		kwiki = new KWiki(new SimpleFileSystemResourceLocator(testResourcesDir));
	}
	
	@Test
	public void listingIsNotNullOrEmpty() throws Exception {
		List<KWikiListing> listing = kwiki.getListing();
		assertNotNull(listing);
		assertFalse(listing.isEmpty());
	}
	
	@Test
	public void listingIsTopLevelOnly() throws Exception {
		List<KWikiListing> listing = kwiki.getListing();
		assertThat(listing.size(), is(1));
		assertThat(listing.get(0).getResourcePath(), is("/"));
		assertThat(listing.get(0).getLocalName(), is(kwiki.getDirectoryDefault()));
	}
	
	@Test
	public void flatten() throws Exception {
		KWikiListing listing = kwiki.getListing().get(0);
		assertThat(listing.flatten().size(), is(6)); //6 FILES in the test dir
		assertThat(listing.flatten().toString(),
				is("[/, "
				+ "/koncept/kwiki/core/, "
				+ "/koncept/kwiki/core/output, "
				+ "/koncept/kwiki/core/SwebleTest, "
				+ "/KwikiRoot, "
				+ "/md/syntax]"));
	}
	
	@Test
	public void matchesExpectedDirectoryStructure() throws Exception {
		// pre tested = we start with a top level element
		KWikiListing root = kwiki.getListing().get(0);
		assertThat(root.getLocalName(), is("index"));
		
		List<KWikiListing> rootChildren = root.getChildren();
		for(int i = 0; i < rootChildren.size(); i++) {
			KWikiListing rootChild = rootChildren.get(i);
			switch(i) {
			case 0:
				assertThat(rootChild.getLocalName(), is("core"));
				assertThat(rootChild.getResourcePath(), is("/koncept/kwiki/core/"));
				List<KWikiListing> coreChildren = rootChild.getChildren();
				assertFalse(coreChildren.isEmpty());
				for(int j = 0; j < coreChildren.size(); j++) {
					KWikiListing coreChild = coreChildren.get(j);
					switch(j) {
					case 0:
						assertThat(coreChild.getLocalName(), is("output"));
						assertThat(coreChild.getResourcePath(), is("/koncept/kwiki/core/output"));
						assertTrue(coreChild.getChildren().isEmpty());
						break;
					case 1:
						assertThat(coreChild.getLocalName(), is("SwebleTest"));
						assertThat(coreChild.getResourcePath(), is("/koncept/kwiki/core/SwebleTest"));
						assertTrue(coreChild.getChildren().isEmpty());
						break;
					default:
						fail("oversize coreChildren: " + coreChildren.size());
					}
				}
				break;
			case 1:
				assertThat(rootChild.getLocalName(), is("KwikiRoot"));
				assertThat(rootChild.getResourcePath(), is("/KwikiRoot"));
				assertTrue(rootChild.getChildren().isEmpty());
				break;
			case 2:
				assertThat(rootChild.getLocalName(), is("syntax"));
				assertThat(rootChild.getResourcePath(), is("/md/syntax"));
				assertTrue(rootChild.getChildren().isEmpty());
				break;
			default:
				fail("oversize rootChildren: " + rootChildren.size());
			}
		}
	}
	
}
