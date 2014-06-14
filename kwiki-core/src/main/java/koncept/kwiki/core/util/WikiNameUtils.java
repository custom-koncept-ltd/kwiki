package koncept.kwiki.core.util;


public class WikiNameUtils {
	
	/**
	 * Returns a files extension - everything after the last dot
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex == -1) return "";
		return filename.substring(dotIndex + 1);
	}
	
	/**
	 * returns the local part of the wiki path (does not
	 * @param absoluteWikiPath
	 * @return
	 */
	public static String getLocalName(String absoluteWikiPath) {
		int dotIndex = absoluteWikiPath.lastIndexOf("/");
		if (dotIndex == -1) return "";
		return absoluteWikiPath.substring(dotIndex + 1);
	}
	
	
	public static String trimExtension(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex == -1) return filename;
		return filename.substring(0, dotIndex);
	}
}
