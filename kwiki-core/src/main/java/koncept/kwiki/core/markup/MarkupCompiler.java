package koncept.kwiki.core.markup;

import java.util.List;

import koncept.kwiki.core.WikiResource;

public interface MarkupCompiler {
	
	public List<String> fileTypes();
	
	public String toHtml(WikiResource resource) throws Exception;
}
