package koncept.kwiki.core;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;

import org.junit.Before;
import org.junit.Test;

public class KWikiTest {

	KWiki kwiki;
	File testResourcesDir;
	
	@Before
	public void init() throws Exception {
		URL fileUrl = getClass().getClassLoader().getResource("KwikiRoot.wikitext");
		testResourcesDir = new File(fileUrl.getFile()).getParentFile();
		kwiki = new KWiki(new SimpleFileSystemResourceLocator(testResourcesDir));
	}
	
	@Test
	public void noResourceTest() {
		WikiResource resource = kwiki.getResource("/missing");
		assertNull(resource);
	}
	
	@Test
	public void canFindCoreResource() throws Exception {
		WikiResource resource = kwiki.getResource("/KwikiRoot");
		assertNotNull(resource);
		String html = kwiki.toHtml(resource);
//		System.out.println(html);
		Assert.assertNotNull(html);
	}
	
	@Test
	public void findMarkdownResource() throws Exception {
		WikiResource resource = kwiki.getResource("/md/syntax");
		assertNotNull(resource);
		String html = kwiki.toHtml(resource);
//		System.out.println(html);
		Assert.assertNotNull(html);
	}
	
}
