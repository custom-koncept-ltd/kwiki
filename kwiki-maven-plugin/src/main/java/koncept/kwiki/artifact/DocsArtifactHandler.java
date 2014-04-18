package koncept.kwiki.artifact;

import org.apache.maven.artifact.handler.ArtifactHandler;

public class DocsArtifactHandler implements ArtifactHandler {

	public String getExtension() {
		return "jar";
	}

	public String getDirectory() {
		return null;
	}

	public String getClassifier() {
		return null;
	}

	public String getPackaging() {
		return "jar";
	}

	public boolean isIncludesDependencies() {
		return true;
	}

	public String getLanguage() {
		return null;
	}

	public boolean isAddedToClasspath() {
		return false;
	}

}
