package com.axonivy.cloud.storage.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.environment.Ivy;

public class UploadUtils {
	
	public static String getFileNameFromUrl(String url) {
		try {
			return Paths.get(new URI(url).getPath()).getFileName().toString();
		} catch (URISyntaxException e) {
			Ivy.log().warn("Can not get file name from " + url);
		}
		return StringUtils.EMPTY;
	}
}
