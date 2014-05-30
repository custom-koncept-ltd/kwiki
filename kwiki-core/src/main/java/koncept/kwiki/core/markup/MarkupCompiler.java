package koncept.kwiki.core.markup;

import koncept.kwiki.core.WikiResource;

public interface MarkupCompiler {
	
	public boolean accepts(String type);
	
	public String toHtml(WikiResource resource) throws Exception;
}
