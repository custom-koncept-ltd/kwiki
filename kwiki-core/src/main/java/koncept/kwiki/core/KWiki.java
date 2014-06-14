package koncept.kwiki.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import koncept.kwiki.core.html.BasicPageWrapper;
import koncept.kwiki.core.html.FullLeftbarMenu;
import koncept.kwiki.core.html.PageWrapper;
import koncept.kwiki.core.markup.HtmlCompiler;
import koncept.kwiki.core.markup.MarkdownCompiler;
import koncept.kwiki.core.markup.MarkupCompiler;
import koncept.kwiki.core.markup.MediaWikiMarkupCompiler;
import koncept.kwiki.core.markup.PlaintextCompiler;
import koncept.kwiki.core.resource.ResourceLocator;
import koncept.kwiki.core.util.WikiNameUtils;

public class KWiki {
	public static final String DEFAULT_DIRECTORY_FILE = "index";
	
	private final ResourceLocator resourceLocator;
	private final List<MarkupCompiler> producers = new ArrayList<MarkupCompiler>();
	private PageWrapper pageWrapper;
	private String directoryDefault = DEFAULT_DIRECTORY_FILE;
	
	public KWiki(ResourceLocator resourceLocator) throws Exception {
		this.resourceLocator = resourceLocator;
		producers.add(new MarkdownCompiler());
		producers.add(new MediaWikiMarkupCompiler());
		producers.add(new HtmlCompiler());
		producers.add(new PlaintextCompiler());
//		pageWrapper = new BasicPageWrapper();
		pageWrapper = new FullLeftbarMenu();
	}
	
	public WikiResource getResource(String resourceName) {
		if (!resourceName.startsWith("/")) throw new IllegalArgumentException("Invalid resource: " + resourceName);
		
		if (resourceName.endsWith("/")) {
			resourceName = resourceName + directoryDefault;
		}
		
		WikiResource resource = null;
		for(String fileType: supportedFileTypes()) {
			resource = resourceLocator.getResource(resourceName + "." + fileType);
			if (resource != null)
				break;
		}
		if (resource == null)
			resource = resourceLocator.getResource(resourceName);
		
		return resource;
	}
	
	private List<String> supportedFileTypes() {
		List<String> types = new ArrayList<>();
		for(MarkupCompiler producer: producers)
			types.addAll(producer.fileTypes());
		return types;
	}
	
	private MarkupCompiler getMarkupCompiler(String type) {
		for(MarkupCompiler producer: producers)
			if (producer.fileTypes().contains(type))
				return producer;
		return null;
	}
	
	public String toHtml(WikiResource resource) throws Exception {
		String html = null;
		MarkupCompiler compiler = getMarkupCompiler(WikiNameUtils.getExtension(resource.getName()).toLowerCase());
		if (compiler != null) html = compiler.toHtml(resource);
		if (html != null) html = pageWrapper.wrap(this, html, resource);
		return html;
	}

	public boolean isConsumable(String type) {
		return getMarkupCompiler(type) != null;
	}
	
	public void updateWikiResource(String resourceName, String wikitext) {
		throw new UnsupportedOperationException();
	}
	
	public void setDirectoryDefault(String directoryDefault) {
		this.directoryDefault = directoryDefault;
	}
	
	public String getDirectoryDefault() {
		return directoryDefault;
	}
	
	public PageWrapper getPageWrapper() {
		return pageWrapper;
	}
	
	public void setPageWrapper(PageWrapper pageWrapper) {
		this.pageWrapper = pageWrapper;
	}
	
	public List<KWikiListing> getListing() throws IOException {
		return KWikiListing.createListing(this, resourceLocator.rootListing());
	}
	
}
