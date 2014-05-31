package koncept.kwiki.core.markup;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import koncept.kwiki.core.WikiResource;

import org.markdown4j.Markdown4jProcessor;

public class MarkdownCompiler implements MarkupCompiler {

	public static final List<String> FILE_EXTENSIONS =  Arrays.asList("md", "markdown");
	
	private final Markdown4jProcessor processor;
	
	public MarkdownCompiler() {
		processor = new Markdown4jProcessor();
	}
	
	public boolean accepts(String type) {
		return FILE_EXTENSIONS.contains(type);
	}

	public String toHtml(WikiResource resource) throws Exception {
		InputStream is = resource.getStream();
		try {
			return processor.process(is);
		} finally {
			is.close();
		}
	}

}
