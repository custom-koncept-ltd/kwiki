package koncept.kwiki.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.html.PageWrapper;

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

		
		// TODO: need an 'admin control' section
		String query = requestUri.getQuery();
		if (query != null) {
			if (query.equals("stop")) {
				server.stop();
				exchange.sendResponseHeaders(200, 0);
			}
			exchange.getResponseBody().flush();
			exchange.close();
			return;
		}

		WikiResource resource = kwiki.getResource(requestUri.getPath());

		if (resource == null) {
			if (!fallThroughToSystemDefault(exchange)) { //no resource found - provide a system default (if applicable)
				System.out.println("NOT FOUND:: " + requestUri);
				String notFound = "404: Not Found";
				byte[] b = notFound.getBytes();
				exchange.sendResponseHeaders(404, b.length);
				exchange.getResponseBody().write(b);
			}
		} else {
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
				sendAndCloseStream(exchange, resource.open());
			}
		}
		exchange.close();
	}

	public boolean fallThroughToSystemDefault(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		
		if (path.equals("/style.css")) {
			PageWrapper pageWrapper = kwiki.getPageWrapper();
			InputStream is = pageWrapper == null ? null : pageWrapper.css();
			if (is == null) {
				exchange.sendResponseHeaders(404, 0);
			} else {
				sendAndCloseStream(exchange, is);
			}
			exchange.getResponseBody().flush();
			return true;
		} else if (path.equals("/favicon.ico")) {
			sendAndCloseStream(exchange, getClass().getResourceAsStream("/kwiki-favicon.ico"));
			return true;
		} else if (path.equals("/favicon.png")) {
			sendAndCloseStream(exchange, getClass().getResourceAsStream("/kwiki-favicon.png"));
			return true;
		}
		
		
		return false;
	}
	
	private void sendAndCloseStream(HttpExchange exchange, InputStream in) throws IOException {
		if (in == null){
			exchange.sendResponseHeaders(404, -1);
			return;
		}
		exchange.sendResponseHeaders(200, 0); // unknown length
		try {
			IOUtils.copy(in, exchange.getResponseBody());
		} finally {
			if (in != null)
				in.close();
		}
	}
}
