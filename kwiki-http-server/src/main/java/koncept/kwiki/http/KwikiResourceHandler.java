package koncept.kwiki.http;

import java.io.IOException;
import java.net.URI;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class KwikiResourceHandler implements HttpHandler {

	private final KWiki kwiki;
	private final KwikiServer server;
	

	public KwikiResourceHandler(KwikiServer server) {
		this.kwiki = server.getKwiki();
		this.server = server;
	}
	
	
	public void handle(HttpExchange exchange) throws IOException {
		URI requestUri = exchange.getRequestURI();
		
		if (requestUri.getPath().equals("/favicon.ico")) {
			exchange.sendResponseHeaders(404, 0);
			exchange.close();
		}
		
		//TODO: need an 'admin control' section
		String query = requestUri.getQuery();
		if (query != null) {
			if (query.equals("stop"))
				server.stop();
				
		}
		
		WikiResourceDescriptor resourceDescriptor = kwiki.getResource(requestUri.getPath().substring(1));
		
		if (resourceDescriptor == null) {
			String notFound = "404: Not Found";
			byte[] b = notFound.getBytes();
			exchange.sendResponseHeaders(404, b.length);
			exchange.getResponseBody().write(b);
		} else {
			WikiResource resource = resourceDescriptor.getCurrentVersion();
			String html = null;
			try {
				html = kwiki.toHtml(resource);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (html != null) {
				byte[] b = html.getBytes();
				exchange.sendResponseHeaders(200, b.length);
				exchange.getResponseBody().write(b);
			} else {
				exchange.sendResponseHeaders(200, -1); //unknown length
				IOUtils.copy(resource.getStream(), exchange.getResponseBody());
			}
		}
		exchange.close();
	}
}
