package koncept.kwiki.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractKwikiMojo extends AbstractMojo {

	@Parameter(defaultValue="8080")
	public int kwikiport;
	
	public String getDocsDir() {
		String scriptDir = getMavenProject().getBuild().getScriptSourceDirectory();
		String srcDir = getMavenProject().getBuild().getSourceDirectory();
		
		int commonLength = -1;
		for(int i = 0; i < srcDir.length(); i++) {
			if (srcDir.charAt(i) == scriptDir.charAt(i))
				commonLength = i;
			else
				break;
		}
		commonLength++;
		
		return srcDir.substring(0, commonLength) + "docs";
	}
	
	public String getTargetDir() {
		return getMavenProject().getBuild().getDirectory();
	}
	
	public PluginDescriptor getPluginDescriptor() {
		return (PluginDescriptor)getPluginContext().get("pluginDescriptor");
	}
	
	public MavenProject getMavenProject() {
		return (MavenProject)getPluginContext().get("project");
	}

	public int getPort() {
		String value = System.getProperty("kwikiport");
		if (value != null && !value.equals("")) {
			return new Integer(value);
		}
		return kwikiport; //8080 default: because you won't have anything else on this port...  :/
	}

}
