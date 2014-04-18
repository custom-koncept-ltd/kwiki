package koncept.kwiki.core;

import java.io.StringWriter;

import koncept.kwiki.core.document.WikiDocument;
import koncept.kwiki.core.resource.ResourceLocator;
import koncept.kwiki.sweble.HtmlPrinter;

import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;

public class KWiki {

	ResourceLocator resourceLocator = null;
	SimpleWikiConfiguration config;
	
	private final ThreadLocal<Compiler> compiler = new ThreadLocal<Compiler>() {
		protected Compiler initialValue() {
			return new Compiler(config);
		};
	};
	
	public KWiki(ResourceLocator resourceLocator) throws Exception {
		this.resourceLocator = resourceLocator;
		config = new SimpleWikiConfiguration(
				"classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
	}
	
	public WikiResourceDescriptor getResource(String resourceName) {
		return resourceLocator.getResource(resourceName);
	}
	
	public String toHtml(WikiDocument wikiDoc) throws Exception {
		PageTitle pageTitle = PageTitle.make(config, wikiDoc.getDocumentVersion().getDocumentName());
		PageId pageId = new PageId(pageTitle, wikiDoc.getDocumentVersion().getDocumentVersion());
		Compiler compiler = this.compiler.get();
		String wikitext = getWikitext(wikiDoc);
		CompiledPage cp = compiler.postprocess(pageId, wikitext, null);
		
		StringWriter w = new StringWriter();

		HtmlPrinter p = new HtmlPrinter(w, pageTitle.getFullTitle());
		p.setCssResource("/org/sweble/wikitext/engine/utils/HtmlPrinter.css",
				"");
		p.setStandaloneHtml(true, "");
		
		//<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		p.go(cp.getPage());

		return w.toString();
	}
	
	private String getWikitext(WikiDocument wikiDoc) {
		StringBuilder sb = new StringBuilder();
		for(String line: wikiDoc.getLines()) {
			sb.append(line);
//			sb.append(System.getProperty("line.separator"));
			sb.append("\n");
		}
		return sb.toString();
	}

	
	public void updateWikiResource(String resourceName, String wikitext) {
		throw new UnsupportedOperationException();
	}
	
}