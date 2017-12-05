package com.meronmee.core.utils;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * MultipartFile Map
 * @author Meron
 *
 */
public class MultiFileMap{
	MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();	

	public MultiFileMap(MultiValueMap<String, MultipartFile> multipartFiles) {
		this.multipartFiles = multipartFiles;
	}

	public MultipartFile getFile(String name) {
		return this.multipartFiles.getFirst(name);
	}
	
	public List<MultipartFile> getFiles(String name) {
		return this.multipartFiles.get(name);
	}	
}
