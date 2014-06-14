package koncept.kwiki.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import koncept.kwiki.core.util.WikiNameUtils;

public class KWikiListing {

	private final KWiki kwiki;
	private final WikiResource resource;
	private final List<KWikiListing> children;
	
	private final String localName;
	private final String resourcePath;
	
	private KWikiListing(KWiki kwiki, WikiResource resource) throws IOException {
		this.kwiki = kwiki;
		this.resource = resource;
		children = Collections.emptyList();
		localName = localName(resource);
		resourcePath = WikiNameUtils.trimExtension(resource.getName());
	}
	
	private KWikiListing(KWiki kwiki, WikiResource resource, List<KWikiListing> children) {
		this.kwiki = kwiki;
		this.resource = resource;
		this.children = children; 
		resourcePath = resource.getName().substring(0, resource.getName().lastIndexOf("/") + 1);
		
		int localNameIndex = resourcePath.substring(0, resourcePath.length()-1).lastIndexOf("/");
		if (localNameIndex == -1) 
			localName = kwiki.getDirectoryDefault();
		else
			localName = resourcePath.substring(localNameIndex + 1, resourcePath.length() - 1);
	}
	
	public static List<KWikiListing> createListing(KWiki kwiki, List<WikiResource> resources) throws IOException {
		resources = new ArrayList<>(resources);
		Collections.sort(resources, new Comparator<WikiResource>() {
			@Override
			public int compare(WikiResource o1, WikiResource o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
			}
		});
		List<KWikiListing> listing = new ArrayList<>();
		
		WikiResource parent = null;
		
		for(int i = 0; i < resources.size(); i++) {
			WikiResource resource = resources.get(i);
			if (resource.isOpenable()) {
				String extension = WikiNameUtils.getExtension(resource.getName());
				if (!kwiki.isConsumable(extension)) {
					resources.remove(i);
					i--;
				} else {
					String localName = localName(resource);
					if (localName.equals(kwiki.getDirectoryDefault())) {
						resources.remove(i);
						i--;
						parent = resource;
					}
				}
			}
		}
		
		
		
		for(WikiResource resource: resources) {
			if (resource.isOpenable()) {
				listing.add(new KWikiListing(kwiki, resource));
			} else if (resource.isListable()) {
				listing.addAll(createListing(kwiki, resource.list()));
			}
		}
		
		if (parent != null) {
			return Arrays.asList(new KWikiListing(kwiki, parent, listing));
		}
		return listing;
	}
	
	private static String localName(WikiResource resource) {
		return WikiNameUtils.trimExtension(WikiNameUtils.getLocalName(resource.getName()));
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public WikiResource getResource() {
		return resource;
	}
	
	public String toHtml() throws Exception {
		return kwiki.toHtml(resource);
	}
	
	public String getLocalName() {
		return localName;
	}
	
	public List<KWikiListing> getChildren() {
		return children;
	}
	
	public List<String> flatten() {
		List<String> flatListing = new ArrayList<>();
		flatListing.add(resourcePath);
		for(KWikiListing child: getChildren()) {
			flatListing.addAll(child.flatten());
		}
		return flatListing;
	}
	
}
