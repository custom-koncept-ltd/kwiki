package koncept.kwiki;

import java.util.Map;

import koncept.kwiki.mojo.AbstractKwikiMojo;

import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * finally - this looks like a good resource:  http://docs.codehaus.org/display/MAVENUSER/Mojo+Developer+Cookbook
 * 
 * email lists here:
 * http://maven.apache.org/mail-lists.html
 * 
 * 
 * @author koncept
 *
 */
@Mojo(name="run", aggregator=true)
public class RunMojo extends AbstractKwikiMojo {

	/** @parameter default-value="${plugin.artifacts}" */
	private java.util.List pluginArtifacts;
	
	/** @component */
	private ArtifactMetadataSource artifactMetadataSource;
	
	/** @component */
	private org.apache.maven.artifact.resolver.ArtifactResolver resolver;
	
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			
			Map pluginContext = getPluginContext();
			
			getLog().info("pluginContext: " + pluginContext);
			getLog().info("pluginArtifacts: " + pluginArtifacts);
			for(Object dep: pluginArtifacts) {
				getLog().info("dep of type " + dep.getClass() + "  " + dep);
			}
			
//			new KwikiServer(new File(getDocsDir()), getPort()).start();
//			getLog().info("Started KWiki on port " + getPort());
			
			
			
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to start KWiki", e);
		}
	}
	
}
