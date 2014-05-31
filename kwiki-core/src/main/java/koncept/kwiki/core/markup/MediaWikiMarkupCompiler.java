package koncept.kwiki.core.markup;

import java.io.InputStream;
import java.io.StringWriter;

import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.util.InputStreamToString;
import koncept.kwiki.sweble.HtmlPrinter;

import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;

public class MediaWikiMarkupCompiler implements MarkupCompiler {

	public static final String FILE_EXTENSION = "wikitext";
	
	private final SimpleWikiConfiguration config;
	private final Compiler compiler;
	
	public MediaWikiMarkupCompiler() throws Exception {
		config = new SimpleWikiConfiguration(
				"classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		compiler = new Compiler(config);
	}
	
	public boolean accepts(String type) {
		return FILE_EXTENSION.equals(type);
	}

	public String toHtml(WikiResource resource) throws Exception {
		PageTitle pageTitle = PageTitle.make(config, resource.getDocumentVersion().getDocumentName());
		PageId pageId = new PageId(pageTitle, resource.getDocumentVersion().getDocumentVersion());
		InputStream is = resource.getStream();
		try {
			String wikitext = InputStreamToString.convertStreamToString(is);
			CompiledPage cp = compiler.postprocess(pageId, wikitext, null);
			
			StringWriter w = new StringWriter();
	
			HtmlPrinter p = new HtmlPrinter(w, null); //pageTitle.getFullTitle());
//			p.setCssResource("/org/sweble/wikitext/engine/utils/HtmlPrinter.css", ""); //
			p.setStandaloneHtml(false, "");
			p.go(cp.getPage());
	
			return w.toString();
		} finally {
			is.close();
		}
	}

}
