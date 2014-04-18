package koncept.kwiki.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.WikiResourceDescriptor;
import koncept.kwiki.core.document.BinaryResource;
import koncept.kwiki.core.document.WikiDocument;
import koncept.kwiki.core.resource.ResourceLocator;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class KwikiServer implements HttpHandler{

	private final ExecutorService executor;
	private final KWiki kwiki;
	
	public static void main(String[] args) throws Exception  {		
		HttpServerProvider provider = HttpServerProvider.provider();
		
		int port = 8080;
		
		HttpServer server = provider.createHttpServer(new InetSocketAddress("localhost", port), 0);
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		server.setExecutor(executor);
		
		ResourceLocator locator = new SimpleFileSystemResourceLocator(new File("."));
		KWiki kwiki = new KWiki(locator);
		server.createContext("/", new KwikiServer(kwiki, executor));
		server.start();
		System.out.println("Started KWiki on port " + port);
	}
	
	public KwikiServer(KWiki kwiki, ExecutorService executor) {
		this.kwiki = kwiki;
		this.executor = executor;
	}
	
	
	public void handle(HttpExchange exchange) throws IOException {
		URI requestUri = exchange.getRequestURI();
		
		if (requestUri.getPath().equals("/favicon.ico")) {
			exchange.sendResponseHeaders(404, 0);
			exchange.close();
		}
		
		String query = requestUri.getQuery();
		System.out.println("query = ");
		
		if (query != null) {
			if (query.equals("stop"))
				executor.shutdown();
				
		}
		
		System.out.println("request path: " + requestUri.getPath());
		
		WikiResourceDescriptor resourceDescriptor = kwiki.getResource(requestUri.getPath().substring(1));
		
		if (resourceDescriptor == null) {
			String notFound = "404: Not Found";
			byte[] b = notFound.getBytes();
			exchange.sendResponseHeaders(404, b.length);
			exchange.getResponseBody().write(b);
		} else {
			WikiResource resource = resourceDescriptor.getCurrentVersion();
			if (resource instanceof BinaryResource) {
				InputStream in = ((BinaryResource)resource).getBytes();
				IOUtils.copy(in, exchange.getResponseBody());
			} else if (resource instanceof WikiDocument) {
				byte[] b;
				try {
				String html = kwiki.toHtml((WikiDocument)resource);
				b = html.getBytes();
				exchange.sendResponseHeaders(200, b.length);
				} catch (Exception e) {
					b = e.getMessage().getBytes();
					exchange.sendResponseHeaders(500, b.length);
				} finally{}
				exchange.getResponseBody().write(b);
			}
		}
		exchange.close();
	}
}
