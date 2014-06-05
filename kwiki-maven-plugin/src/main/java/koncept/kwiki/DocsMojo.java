package koncept.kwiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import koncept.kwiki.core.KWiki;
import koncept.kwiki.core.WikiResource;
import koncept.kwiki.core.resource.ResourceLocator;
import koncept.kwiki.core.resource.file.SimpleFileSystemResourceLocator;
import koncept.kwiki.mojo.AbstractKwikiMojo;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.artifact.AttachedArtifact;

@Mojo(name="docs")
public class DocsMojo extends AbstractKwikiMojo {
	
	@Parameter(defaultValue="userdocs")
	public String classifier;
	
	private KWiki kwiki;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File docsDir = new File(getDocsDir());
		if (!docsDir.exists()) {
			getLog().info(getDocsDir() + " directory does not exist. Skipping");
			return;
		} else if (!docsDir.isDirectory()) {
			getLog().info(getDocsDir()  + " is not a directory. Skipping");
		}
		
		String outputJarName = getMavenProject().getArtifactId() +
				"-" + getMavenProject().getVersion() +
				"-" + classifier +
				".jar";
		File outputJarFile = new File(new File(getTargetDir()), outputJarName);
		
		ZipOutputStream out = null;
		try {
			outputJarFile.getParentFile().mkdirs();
			out = new ZipOutputStream(new FileOutputStream(outputJarFile));

			//write out a manifest
			out.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			IOUtils.writeLines(Arrays.asList(
					"Manifest-Version: 1.0",
					"Built-By: " + System.getProperty("user.name"),
					"Build-Jdk: " + System.getProperty("java.version"),
					"Created-By: Koncept KWiki Maven Plugin"
//					Archiver-Version: Plexus Archiver
					), "\n", out);
			
			append(docsDir, docsDir, out);
		} catch (ZipException e) {
			throw new MojoFailureException("Error writing to jar file", e);
		} catch (IOException e) {
			throw new MojoFailureException("Error writing to jar file", e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					throw new MojoFailureException("Error closing jar file", e);
				}
		}

//		attatch it
		AttachedArtifact artifact = new AttachedArtifact(getMavenProject().getArtifact(), "jar", classifier, new DefaultArtifactHandler("jar"));
		artifact.setResolved(true);
		artifact.setFile(outputJarFile);
		getMavenProject().addAttachedArtifact(artifact);
	}
	
	private KWiki getKwiki() throws MojoFailureException {
		if (kwiki == null) try {
			ResourceLocator locator = new SimpleFileSystemResourceLocator(new File(getDocsDir()));
			kwiki = new KWiki(locator);
		} catch (Exception e) {
			throw new MojoFailureException("Unable to instantiate KWiki", e);
		}
		return kwiki;
	}
	
	private void append(File root, File file, ZipOutputStream out) throws IOException, MojoFailureException {
		if (file.isDirectory()) {
			File[] listing = file.listFiles();
			Arrays.sort(listing);
			for(File listed: listing) {
				append(root, listed, out);
			}
		} else if (file.isFile()){
			String relativeDir = file.getParentFile().getAbsolutePath().substring(root.getAbsolutePath().length());
			
			//annoying directory squiggles...
			if (!relativeDir.equals("")) {
				if (relativeDir.startsWith("/") || relativeDir.startsWith("\\"))
					relativeDir = relativeDir.substring(1);
				if (!(relativeDir.endsWith("/") || relativeDir.endsWith("\\")))
					relativeDir = relativeDir + "/";
			}
			String type = getFileSuffix(file);
			if(getKwiki().isConsumable(type)) {
				String pageName = file.getName().substring(0, file.getName().length() - type.length());
				WikiResource wikiResource = getKwiki().getResource(relativeDir + pageName);
//				WikiResource wikiResource = wikiResourceDescriptor.getCurrentVersion();
				String html = null;
				try {
					html = getKwiki().toHtml(wikiResource);
				} catch (Exception e) {
					throw new MojoFailureException("Error converting wiki page", e);
				}
				
				if (html != null) {
					out.putNextEntry(new ZipEntry(relativeDir + pageName + ".html"));
					IOUtils.write(html, out);
				} 
			} else { //NOT a consumable type for KWiki - include it as a binary fine
				out.putNextEntry(new ZipEntry(relativeDir + file.getName()));
				IOUtils.copy(new FileInputStream(file), out);
			}
		} else throw new MojoFailureException("Not a directory or a file?!?! : " + file.getAbsolutePath());
	}
	
	private String getFileSuffix(File file) {
		String docName = file.getName();
		int dotIndex = docName.lastIndexOf(".");
		if (dotIndex != -1)
			return docName.substring(dotIndex + 1);
		return "";
	}

}
