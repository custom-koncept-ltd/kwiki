package koncept.kwiki.sweble;

import java.io.IOException;
import java.io.Writer;

import org.sweble.wikitext.engine.Page;

public class HtmlPrinter extends org.sweble.wikitext.engine.utils.HtmlPrinter {

	String classPrefix= "";
	String articleTitle = null;
			
	public HtmlPrinter(Writer writer, String articleTitle) {
		super(writer, articleTitle);
		this.articleTitle = articleTitle;
	}
	
	
	@Override
	public void setStandaloneHtml(boolean standaloneHtml, String classPrefix) {
		super.setStandaloneHtml(standaloneHtml, classPrefix);
		this.classPrefix = classPrefix;
	}
	public void visit(Page page) throws IOException
	{
		
		boolean standalone = isStandaloneHtml();
//		setStandaloneHtml(false, classPrefix);
		
		if (standalone) {
			print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			printNewline(true);
			print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
			printNewline(true);
			print("\t\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
			printNewline(true);
			print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			printNewline(true);
			print("\t<head>");
			printNewline(true);
			print("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"); // nK: closed meta tag
			printNewline(true);
			print("\t\t<title>");
			print(escHtml(articleTitle));
			print("</title>");
			printNewline(false);
			if (getCssResource() != null) {
				print("\t<style type=\"text/css\">");
				printNewline(true);
				print("\t\t<!--");
				printNewline(false);
				incIndent("\t\t\t");
				print(indentText(loadFromResource(getCssResource())));
				decIndent();
				printNewline(false);
				print("\t\t-->");
				printNewline(true);
				print("\t</style>");
			} else if (getCssFile() != null) {
				print("\t<style type=\"text/css\">");
				printNewline(true);
				print("\t\t<!--");
				printNewline(false);
				incIndent("\t\t\t");
				print(indentText(load(getCssFile())));
				decIndent();
				printNewline(false);
				print("\t\t-->");
				printNewline(true);
				print("\t</style>");
			} else if (getCssLink() != null) {
				print("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"");
				print(getCssLink());
				print("\">");
			}
			printNewline(false);
			print("\t</head>");
			printNewline(true);
			print("\t<body>");
			printNewline(true);
		}
		print("<div class=\"");
		print(classPrefix);
		print("content\">");
		printNewline(true);
		print("\t<h1 class=\"");
		print(classPrefix);
		print("article-heading\">");
		print(escHtml(articleTitle));
		print("</h1>");
		printNewline(true);
		print("\t<div class=\"");
		print(classPrefix);
		print("article-content\">");
		printNewline(false);
		incIndent("\t\t");
		iterate(page.getContent());
		decIndent();
		printNewline(false);
		print("\t</div>");
		printNewline(true);
		print("</div>");
		printNewline(true);
//		print("</div>"); //nK removed unbalanced tag
//		printNewline(false); //nK removed unbalanced tag
		if (standalone) {
			print("\t</body>");
			printNewline(true);
			print("</html>");
		}
		printNewline(false);
		printNewline(false);

//		setStandaloneHtml(standalone, classPrefix);
		
	}
	
	
}
