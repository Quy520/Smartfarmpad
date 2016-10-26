package com.smartfarm.tools;

import java.io.File;

public class FileUtils {
	
	 public static void deleteDir(File file) { 
		 
	        if (file.exists() == false) { 
	        	return;
	        } else { 
	            if (file.isFile()) { 
	                file.delete(); 
	                return; 
	            } 
	            if (file.isDirectory()) { 
	                File[] childFile = file.listFiles(); 
	                if (childFile == null || childFile.length == 0) { 
	                    file.delete(); 
	                    return; 
	                } 
	                for (File f : childFile) { 
	                	deleteDir(f); 
	                } 
	                file.delete(); 
	            } 
	        } 
	    } 
}
