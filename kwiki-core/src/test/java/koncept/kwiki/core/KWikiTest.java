package koncept.kwiki.core;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;
import koncept.kwiki.core.document.WikiDocument;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;

import org.junit.Before;
import org.junit.Test;

public class KWikiTest {

	KWiki kwiki;
	
	@Before
	public void init() throws Exception {
		URL fileUrl = getClass().getClassLoader().getResource("KwikiRoot.kwiki");
		File dir = new File(fileUrl.getFile()).getParentFile();
		kwiki = new KWiki(new SimpleFileSystemResourceLocator(dir));
	}
	
	@Test
	public void noResourceTest() {
		WikiResourceDescriptor resource = kwiki.getResource("missing");
		assertNull(resource);
	}
	
	@Test
	public void canFindCoreResource() throws Exception {
		WikiResourceDescriptor resource = kwiki.getResource("KwikiRoot");
		assertNotNull(resource);
		
		WikiResource currentVersion = resource.getCurrentVersion();
		assertTrue(WikiDocument.class.isAssignableFrom(currentVersion.getClass()));
		String html = kwiki.toHtml((WikiDocument)resource.getCurrentVersion());
//		System.out.println(html);
		Assert.assertNotNull(html);
	}
	
}
