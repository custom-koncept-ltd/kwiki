package koncept.kwiki;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import koncept.kwiki.mojo.AbstractKwikiMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="stop", aggregator=true)
public class StopMojo extends AbstractKwikiMojo {
/*
 add to settings.xml:
 
  <pluginGroups>
    <pluginGroup>koncept.kwiki</pluginGroup>
  </pluginGroups
 */
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			Socket s = new Socket("localhost", getPort());
			PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			String newLine = "\r\n";
			out.print("GET /?stop HTTP/0.9" + newLine);
			out.print(newLine);
			out.print(newLine);
			s.close(); //don't care about the response
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to stop KWiki", e);
		}
	}
	
	public int getPort() {
		String value = System.getProperty("kwikiport");
		if (value != null && !value.equals("")) {
			return new Integer(value);
		}
		return 8080; //because you won't have anything else on this port...  :/
	}
	
}
