package koncept.kwiki.http;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class KwikiServer {

	private final HttpServer server;
	private final KWiki kwiki;
	
	public static void main(String[] args) throws Exception  {		
		File location = null;
		if (args.length == 0) {
			location = new File(".");
		} else if (args.length == 1) {
			location = new File(args[0]);
		} else
			throw new RuntimeException("unable to run KWiki");
		int port = 8080;
		KwikiServer server = new KwikiServer(location, port);
		server.start();
		System.out.println("Started KWiki on port " + port + " serving " + location.getAbsolutePath());
	}
	
	private static KWiki fileSystemKwiki(File location) throws Exception {
		return new KWiki(new SimpleFileSystemResourceLocator(location));
	}
	
	private static HttpServer server(int port) throws IOException {
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer server = provider.createHttpServer(new InetSocketAddress("localhost", port), 0);
		return server;
	}
	
	public KwikiServer(File rootDir, int port) throws Exception {
		this(fileSystemKwiki(rootDir), port);
	}
	
	public KwikiServer(KWiki kwiki, int port) throws Exception {
		this(kwiki, server(port));
	}
	
	public KwikiServer(File rootDir, HttpServer server) throws Exception {
		this(fileSystemKwiki(rootDir), server);
	}

	public KwikiServer(KWiki kwiki, HttpServer server) {
		this.server = server;
		this.kwiki = kwiki;
		ExecutorService executorService = null;
		if (server.getExecutor() == null) {
			executorService = Executors.newFixedThreadPool(5);
			server.setExecutor(executorService);
		} else if (server.getExecutor() instanceof ExecutorService) {
			executorService = (ExecutorService)server.getExecutor();
		} else {
			//can't set executor... so the ?stop scommand won't work
			throw new RuntimeException("won't start something I can't stop");
		}

		server.createContext("/", new KwikiResourceHandler(this));
	}
	
	public KWiki getKwiki() {
		return kwiki;
	}
	
	public void start() {
		server.start();
	}
	
	public void stop() {
		server.stop(0);
	}
	
	
}
