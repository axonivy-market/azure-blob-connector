package com.axonivy.connector.azure.blob.demo.utils;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public final class FileTypeUtils {

	private FileTypeUtils() {

	}

	public static boolean isImage(String fileName) {
		String ext = FilenameUtils.getExtension(fileName);
		String extLowerCase = Objects.toString(ext, StringUtils.EMPTY).toLowerCase();
		return Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff").contains(extLowerCase);
	}
}
