package koncept.kwiki;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.resource.ResourceLocator;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;
import koncept.kwiki.http.KwikiServer;
import koncept.kwiki.mojo.AbstractKwikiMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

@Mojo(name="run", aggregator=true)
public class RunMojo extends AbstractKwikiMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			HttpServerProvider provider = HttpServerProvider.provider();
			
			HttpServer server = provider.createHttpServer(new InetSocketAddress("localhost", getPort()), 0);
			
			ExecutorService executor = Executors.newFixedThreadPool(5);
			server.setExecutor(executor);
			
			ResourceLocator locator = new SimpleFileSystemResourceLocator(new File(getDocsDir()));
			KWiki kwiki = new KWiki(locator);
			server.createContext("/", new KwikiServer(kwiki, executor));
			server.start();
			getLog().info("Started KWiki on port " + getPort());
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to start KWiki", e);
		}
	}
	
}
