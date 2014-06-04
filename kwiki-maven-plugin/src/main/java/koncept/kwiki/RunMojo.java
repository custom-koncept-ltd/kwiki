package koncept.kwiki;

import java.io.File;

import koncept.kwiki.http.KwikiServer;
import koncept.kwiki.mojo.AbstractKwikiMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="run", aggregator=true)
public class RunMojo extends AbstractKwikiMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			new KwikiServer(new File(getDocsDir()), getPort()).start();
			getLog().info("Started KWiki on port " + getPort());
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to start KWiki", e);
		}
	}
	
}
