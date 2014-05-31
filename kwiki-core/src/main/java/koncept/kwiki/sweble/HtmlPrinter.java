package koncept.kwiki.sweble;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.lazy.encval.IllegalCodePoint;
import org.sweble.wikitext.lazy.parser.Bold;
import org.sweble.wikitext.lazy.parser.DefinitionDefinition;
import org.sweble.wikitext.lazy.parser.DefinitionList;
import org.sweble.wikitext.lazy.parser.DefinitionTerm;
import org.sweble.wikitext.lazy.parser.Enumeration;
import org.sweble.wikitext.lazy.parser.EnumerationItem;
import org.sweble.wikitext.lazy.parser.ExternalLink;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.Italics;
import org.sweble.wikitext.lazy.parser.Itemization;
import org.sweble.wikitext.lazy.parser.ItemizationItem;
import org.sweble.wikitext.lazy.parser.MagicWord;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.Section;
import org.sweble.wikitext.lazy.parser.SemiPre;
import org.sweble.wikitext.lazy.parser.SemiPreLine;
import org.sweble.wikitext.lazy.parser.Signature;
import org.sweble.wikitext.lazy.parser.Table;
import org.sweble.wikitext.lazy.parser.TableCaption;
import org.sweble.wikitext.lazy.parser.TableCell;
import org.sweble.wikitext.lazy.parser.TableHeader;
import org.sweble.wikitext.lazy.parser.TableRow;
import org.sweble.wikitext.lazy.parser.Url;
import org.sweble.wikitext.lazy.parser.Whitespace;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.parser.XmlElementClose;
import org.sweble.wikitext.lazy.parser.XmlElementEmpty;
import org.sweble.wikitext.lazy.parser.XmlElementOpen;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;
import org.sweble.wikitext.lazy.utils.XmlAttribute;
import org.sweble.wikitext.lazy.utils.XmlAttributeGarbage;
import org.sweble.wikitext.lazy.utils.XmlCharRef;
import org.sweble.wikitext.lazy.utils.XmlEntityRef;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;



public class HtmlPrinter
        extends
        	de.fau.cs.osr.ptk.common.PrinterBase
        
{
	public static String print(AstNode node, String articleTitle)
	{
		StringWriter writer = new StringWriter();
		new HtmlPrinter(writer, articleTitle).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, AstNode node, String articleTitle)
	{
		new HtmlPrinter(writer, articleTitle).go(node);
		return writer;
	}


	
	// =========================================================================
	
	public void visit(AstNode astNode) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		print(astNode.getClass().getSimpleName());
		print("</span>");

	}
	public void visit(NodeList l) throws IOException
	{
		iterate(l);

	}
	public void visit(Page page) throws IOException
	{
		printNewline(false);
		if (standaloneHtml) {
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
			print("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
			printNewline(true);
			if (articleTitle != null) {
				print("\t\t<title>");
				print(escHtml(articleTitle));
				print("</title>");
				printNewline(false);
			}
			if (cssResource != null) {
				print("\t<style type=\"text/css\">");
				printNewline(true);
				print("\t\t<!--");
				printNewline(false);
				incIndent("\t\t\t");
				print(indentText(loadFromResource(cssResource)));
				decIndent();
				printNewline(false);
				print("\t\t-->");
				printNewline(true);
				print("\t</style>");
			} else if (cssFile != null) {
				print("\t<style type=\"text/css\">");
				printNewline(true);
				print("\t\t<!--");
				printNewline(false);
				incIndent("\t\t\t");
				print(indentText(load(cssFile)));
				decIndent();
				printNewline(false);
				print("\t\t-->");
				printNewline(true);
				print("\t</style>");
			} else if (cssLink != null) {
				print("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"");
				print(cssLink);
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
		if (articleTitle != null) {
			print("\t<h1 class=\"");
			print(classPrefix);
			print("article-heading\">");
			print(escHtml(articleTitle));
			print("</h1>");
			printNewline(true);
		}
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
//		printNewline(true);
//		print("</div>");
		printNewline(false);
		if (standaloneHtml) {
			print("\t</body>");
			printNewline(true);
			print("</html>");
		}
		printNewline(false);
		printNewline(false);

	}
	public void visit(Text text) throws IOException
	{
		print(escHtml(text.getContent()));

	}
	public void visit(Italics n) throws IOException
	{
		print("<i>");
		iterate(n.getContent());
		print("</i>");

	}
	public void visit(Bold n) throws IOException
	{
		print("<b>");
		iterate(n.getContent());
		print("</b>");

	}
	public void visit(Whitespace n) throws IOException
	{
		iterate(n.getContent());

	}
	public void visit(Paragraph p) throws IOException
	{
		printNewline(false);
		renderBlockLevelElementsFirst(p);
		printNewline(false);
		if (!isParagraphEmpty(p)) {
			print("<p>");
			printNewline(false);
			incIndent("\t");
			iterate(p.getContent());
			decIndent();
			printNewline(false);
			print("</p>");
		}
		printNewline(false);
		printNewline(false);

	}
	public void visit(SemiPre sp) throws IOException
	{
		printNewline(false);
		print("<pre>");
		iterate(sp.getContent());
		print("</pre>");
		printNewline(false);

	}
	public void visit(SemiPreLine line) throws IOException
	{
		iterate(line.getContent());
		print("\n");

	}
	public void visit(Section s) throws IOException
	{
		printNewline(false);
		print("<div class=\"");
		print(classPrefix);
		print("section\">");
		printNewline(true);
		print("\t<h");
		print(s.getLevel());
		print(">");
		iterate(s.getTitle());
		print("</h");
		print(s.getLevel());
		print(">");
		printNewline(true);
		print("\t<div class=\"");
		print(classPrefix);
		print("section-body\">");
		printNewline(false);
		incIndent("\t\t");
		iterate(s.getBody());
		decIndent();
		printNewline(false);
		print("\t</div>");
		printNewline(true);
		print("</div>");
		printNewline(false);

	}
	public void visit(XmlComment e) throws IOException
	{

	}
	public void visit(XmlElement e) throws IOException
	{
		print("<");
		print(e.getName());
		iterate(e.getXmlAttributes());
		if (e.getEmpty()) {
			print(" />");
		} else {
			print(">");
			iterate(e.getBody());
			print("</");
			print(e.getName());
			print(">");
		}

	}
	public void visit(XmlAttribute a) throws IOException
	{
		print(" ");
		print(a.getName());
		print("=\"");
		iterate(a.getValue());
		print("\"");

	}
	public void visit(XmlAttributeGarbage g) throws IOException
	{

	}
	public void visit(XmlCharRef ref) throws IOException
	{
		print("&#");
		print(ref.getCodePoint());
		print(";");

	}
	public void visit(XmlEntityRef ref) throws IOException
	{
		print("&");
		print(ref.getName());
		print(";");

	}
	public void visit(DefinitionList n) throws IOException
	{
		printNewline(false);
		print("<dl>");
		printNewline(false);
		incIndent("\t");
		iterate(n.getContent());
		decIndent();
		printNewline(false);
		print("</dl>");
		printNewline(false);

	}
	public void visit(DefinitionTerm n) throws IOException
	{
		printNewline(false);
		print("<dt>");
		iterate(n.getContent());
		print("</dt>");
		printNewline(false);

	}
	public void visit(DefinitionDefinition n) throws IOException
	{
		printNewline(false);
		print("<dd>");
		iterate(n.getContent());
		print("</dd>");
		printNewline(false);

	}
	public void visit(Enumeration n) throws IOException
	{
		printNewline(false);
		print("<ol>");
		printNewline(false);
		incIndent("\t");
		iterate(n.getContent());
		decIndent();
		printNewline(false);
		print("</ol>");
		printNewline(false);

	}
	public void visit(EnumerationItem n) throws IOException
	{
		printNewline(false);
		print("<li>");
		iterate(n.getContent());
		print("</li>");
		printNewline(false);

	}
	public void visit(Itemization n) throws IOException
	{
		printNewline(false);
		print("<ul>");
		printNewline(false);
		incIndent("\t");
		iterate(n.getContent());
		decIndent();
		printNewline(false);
		print("</ul>");
		printNewline(false);

	}
	public void visit(ItemizationItem n) throws IOException
	{
		printNewline(false);
		print("<li>");
		iterate(n.getContent());
		print("</li>");
		printNewline(false);

	}
	public void visit(ExternalLink link) throws IOException
	{
		print("<a href=\"");
		print(link.getTarget().getProtocol());
		print(":");
		print(link.getTarget().getPath());
		print("\">");
		if (!link.getTitle().isEmpty()) {
			iterate(link.getTitle());
		} else {
			printExternalLinkNumber(link);
		}
		print("</a>");

	}
	public void visit(Url url) throws IOException
	{
		print("<a href=\"");
		print(url.getProtocol());
		print(":");
		print(url.getPath());
		print("\">");
		print(url.getProtocol());
		print(":");
		print(url.getPath());
		print("</a>");

	}
	public void visit(InternalLink n) throws IOException
	{
		print("<a href=\"");
		print(makeLinkTarget(n));
		print("\">");
		print(n.getPrefix());
		if (n.getTitle().getContent().isEmpty()) {
			print(makeLinkTitle(n));
		} else {
			iterate(n.getTitle().getContent());
		}
		print(n.getPostfix());
		print("</a>");

	}
	public void visit(Table table) throws IOException
	{
		printNewline(false);
		print("<table");
		iterate(table.getXmlAttributes());
		print(">");
		printNewline(false);
		incIndent("\t");
		iterate(table.getBody());
		decIndent();
		printNewline(false);
		printNewline(true);
		print("</table>");
		printNewline(false);

	}
	public void visit(TableCaption caption) throws IOException
	{
		printNewline(false);
		print("<caption");
		iterate(caption.getXmlAttributes());
		print(">");
		printNewline(false);
		incIndent("\t");
		iterate(caption.getBody());
		decIndent();
		printNewline(false);
		print("</caption>");
		printNewline(false);

	}
	public void visit(TableRow row) throws IOException
	{
		printNewline(false);
		print("<tr");
		iterate(row.getXmlAttributes());
		print(">");
		printNewline(false);
		incIndent("\t");
		iterate(row.getBody());
		decIndent();
		printNewline(false);
		print("</tr>");
		printNewline(false);

	}
	public void visit(TableHeader header) throws IOException
	{
		printNewline(false);
		print("<th");
		iterate(header.getXmlAttributes());
		print(">");
		printNewline(false);
		incIndent("\t");
		iterate(header.getBody());
		decIndent();
		printNewline(false);
		print("</th>");
		printNewline(false);

	}
	public void visit(TableCell cell) throws IOException
	{
		printNewline(false);
		print("<td");
		iterate(cell.getXmlAttributes());
		print(">");
		printNewline(false);
		incIndent("\t");
		iterate(cell.getBody());
		decIndent();
		printNewline(false);
		print("</td>");
		printNewline(false);

	}
	public void visit(HorizontalRule rule) throws IOException
	{
		printNewline(false);
		print("<hr />");
		printNewline(false);

	}
	public void visit(Signature sig) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("signature\">");
		print(makeSignature(sig));
		print("</span>");

	}
	public void visit(Redirect n) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("redirect\">&#x21B3; ");
		print(n.getTarget());
		print("</span>");

	}
	public void visit(IllegalCodePoint n) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("illegal\">");
		print(asXmlCharRefs(n.getCodePoint()));
		print("</span>");

	}
	public void visit(MagicWord n) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("magic-word\">__");
		print(n.getWord());
		print("__</span>");

	}
	public void visit(TagExtension n) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		if (renderTagExtensions) {
			if (n.getBody().isEmpty()) {
				print("&lt;");
				print(n.getName());
				iterate(n.getXmlAttributes());
				print(" />");
			} else {
				print("&lt;");
				print(n.getName());
				iterate(n.getXmlAttributes());
				print(">");
				print(escHtml(n.getBody()));
				print("&lt;/");
				print(n.getName());
				print(">");
			}
		} else {
			if (n.getXmlAttributes().isEmpty()) {
				print("&lt;");
				print(n.getName());
			} else {
				print("&lt;");
				print(n.getName());
				print(" ...");
			}
			if (n.getBody().isEmpty()) {
				print("/>");
			} else {
				print(">...&lt;/");
				print(n.getName());
				print(">");
			}
		}
		print("</span>");

	}
	public void visit(XmlElementEmpty e) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		print("&lt;");
		print(e.getName());
		iterate(e.getXmlAttributes());
		print(" />");
		print("</span>");

	}
	public void visit(XmlElementOpen e) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		print("&lt;");
		print(e.getName());
		iterate(e.getXmlAttributes());
		print(">");
		print("</span>");

	}
	public void visit(XmlElementClose e) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		print("&lt;/");
		print(e.getName());
		print(">");
		print("</span>");

	}
	public void visit(Template tmpl) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		if (renderTemplates) {
			print("{");
			print("{");
			iterate(tmpl.getName());
			iterate(tmpl.getArgs());
			print("}}");
		} else {
			if (tmpl.getArgs().isEmpty()) {
				print("{");
				print("{");
				iterate(tmpl.getName());
				print("}}");
			} else {
				print("{");
				print("{");
				iterate(tmpl.getName());
				print("|...}}");
			}
		}
		print("</span>");

	}
	public void visit(TemplateParameter param) throws IOException
	{
		print("<span class=\"");
		print(classPrefix);
		print("unknown-node\">");
		if (renderTemplates) {
			print("{");
			print("{");
			print("{");
			iterate(param.getName());
			dispatch(param.getDefaultValue());
			iterate(param.getGarbage());
			print("}}}");
		} else {
			if (param.getDefaultValue() == null) {
				print("{");
				print("{");
				print("{");
				iterate(param.getName());
				print("}}}");
			} else {
				print("{");
				print("{");
				print("{");
				iterate(param.getName());
				print("|...}}}");
			}
		}
		print("</span>");

	}
	public void visit(TemplateArgument arg) throws IOException
	{
		print("|");
		if (arg.getHasName()) {
			iterate(arg.getValue());
		} else {
			iterate(arg.getName());
			print("=");
			iterate(arg.getValue());
		}

	}

	
	// =========================================================================
	


	private String classPrefix;

	private String articleTitle = "";

	private boolean renderTemplates = false;

	private boolean renderTagExtensions = false;

	private List<ExternalLink> numberedLinks = new ArrayList<ExternalLink>();

	private boolean standaloneHtml = true;

	private String cssLink;

	private File cssFile;

	private String cssResource;

	// ===========================================================================

	public HtmlPrinter(Writer writer, String articleTitle)
	{
		super(writer);

		this.articleTitle = articleTitle;

		setCssResource("HtmlPrinter.css", "");
	}

	// ===========================================================================

	private void setClassPrefix(String classPrefix)
	{
		if (classPrefix != null)
		{
			this.classPrefix = classPrefix;
			if (!classPrefix.isEmpty())
				this.classPrefix += '-';
		}
	}

	public boolean isStandaloneHtml()
	{
		return standaloneHtml;
	}

	public void setStandaloneHtml(boolean standaloneHtml, String classPrefix)
	{
		this.standaloneHtml = standaloneHtml;
		setClassPrefix(classPrefix);
	}

	public String getCssLink()
	{
		return cssLink;
	}

	public void setCssLink(String cssLink, String classPrefix)
	{
		this.cssFile = null;
		this.cssResource = null;
		this.cssLink = cssLink;
		setClassPrefix(classPrefix);
	}

	public File getCssFile()
	{
		return cssFile;
	}

	public void setCssFile(File cssFile, String classPrefix)
	{
		this.cssResource = null;
		this.cssLink = null;
		this.cssFile = cssFile;
		setClassPrefix(classPrefix);
	}

	public String getCssResource()
	{
		return cssResource;
	}

	public void setCssResource(String cssResource, String classPrefix)
	{
		this.cssFile = null;
		this.cssLink = null;
		this.cssResource = cssResource;
		setClassPrefix(classPrefix);
	}

	public void setRenderTemplates(boolean renderTemplates)
	{
		this.renderTemplates = renderTemplates;
	}

	public void setRenderTagExtensions(boolean renderTagExtensions)
	{
		this.renderTagExtensions = renderTagExtensions;
	}

	// ===========================================================================

	/*
	private void iterate(List<? extends AstNode> list)
	{
		for (AstNode n : list)
			dispatch(n);
	}
	*/

	private String asXmlCharRefs(String codePoint)
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < codePoint.length(); ++i)
		{
			b.append("&#");
			b.append((int) codePoint.charAt(i));
			b.append(";");
		}
		return b.toString();
	}

	@SuppressWarnings("unchecked")
	private void renderBlockLevelElementsFirst(Paragraph p)
	{
		List<AstNode> l = (List<AstNode>) p.getAttribute("blockLevelElements");
		if (l == null)
			return;

		for(AstNode n : l)
			dispatch(n);
	}

	@SuppressWarnings("unchecked")
	private boolean isParagraphEmpty(Paragraph p)
	{
		if (!p.isEmpty())
		{
			List<AstNode> l = (List<AstNode>) p.getAttribute("blockLevelElements");
			if (l == null || p.size() - l.size() > 0)
				return false;
		}
		return true;
	}

	private void printExternalLinkNumber(ExternalLink link)
	{
		numberedLinks.add(link);
		print(numberedLinks.size());
	}

	private String makeLinkTitle(InternalLink n)
	{
		return n.getTarget();
	}

	private String makeLinkTarget(InternalLink n)
	{
		return n.getTarget();
	}

	private String makeSignature(Signature sig)
	{
		return "[SIG]";
	}

}


