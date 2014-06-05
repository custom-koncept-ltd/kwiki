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
	
	public List<String> fileTypes() {
		return FILE_EXTENSIONS;
	}

	public String toHtml(WikiResource resource) throws Exception {
		InputStream is = null;
		try {
			is = resource.open();
			return processor.process(is);
		} finally {
			if (is != null)
				is.close();
		}
	}

}
