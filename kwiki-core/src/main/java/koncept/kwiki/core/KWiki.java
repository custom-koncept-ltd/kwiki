package koncept.kwiki.core;

import java.util.ArrayList;
import java.util.List;

import koncept.kwiki.core.markup.MarkdownCompiler;
import koncept.kwiki.core.markup.MarkupCompiler;
import koncept.kwiki.core.markup.MediaWikiMarkupCompiler;
import koncept.kwiki.core.resource.ResourceLocator;

public class KWiki {

	private final ResourceLocator resourceLocator;
	private final List<MarkupCompiler> producers = new ArrayList<MarkupCompiler>();
	
	public KWiki(ResourceLocator resourceLocator) throws Exception {
		this.resourceLocator = resourceLocator;
		producers.add(new MediaWikiMarkupCompiler());
		producers.add(new MarkdownCompiler());
	}
	
	public WikiResourceDescriptor getResource(String resourceName) {
		return resourceLocator.getResource(resourceName);
	}
	
	public boolean isConsumable(String type) {
		return getMarkupCompiler(type) != null;
	}
	
	private MarkupCompiler getMarkupCompiler(String type) {
		for(MarkupCompiler producer: producers)
			if (producer.accepts(type))
				return producer;
		return null;
	}
	
	public String toHtml(WikiResource resource) throws Exception {
		MarkupCompiler compiler = getMarkupCompiler(resource.getType());
		if (compiler != null) return compiler.toHtml(resource);
		return null;
	}
	
	public void updateWikiResource(String resourceName, String wikitext) {
		throw new UnsupportedOperationException();
	}
	
}
