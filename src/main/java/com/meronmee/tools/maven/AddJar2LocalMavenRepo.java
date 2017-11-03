package com.meronmee.tools.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 向本地仓库里面添加jar包
 * 
 */
public class AddJar2LocalMavenRepo {
	/**
	 * 控制台输出日志
	 * @param objs
	 */
	public static void log(Object... objs){
		System.out.println(join(objs));
	}
	/**
	 * 连接字符串
	 * @param objs
	 */
	public static String join(Object... objs){
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs){
			sb.append(String.valueOf(obj));
		}
		return sb.toString();
	}
	public static boolean isBlank(String str){
		if(str == null){
			return true;
		}
		str = str.trim();
		return 	str.length() == 0;	
	}
	public static boolean isNotBlank(String str){		
		return !isBlank(str);	
	}
	
	/**
	 * 输出异常
	 * @param msg
	 */
	public static void error(String msg){
		throw new IllegalArgumentException(msg);
	}
	
	public static void main(String[] args){
		try{
			/*
			String dir = "E:\\WorkSpace\\eclipse\\demo\\demo\\src\\main\\java\\com\\demo\\tools\\maven";
			
			System.out.println("1:" + Thread.currentThread().getContextClassLoader().getResource(""));  
			System.out.println("2:" + AddJar2LocalMavenRepo.class.getClassLoader().getResource(""));  
			System.out.println("3:" + ClassLoader.getSystemResource(""));  
			System.out.println("4:" + AddJar2LocalMavenRepo.class.getResource("").getPath());//IdcardClient.class文件所在路径  
			System.out.println("5:" + AddJar2LocalMavenRepo.class.getResource("/")); // Class包所在路径，得到的是URL对象，用url.getPath()获取绝对路径String  
			System.out.println("6:" + new File("").getAbsolutePath());  
			*/
			
			String dir = ClassLoader.getSystemResource("").toString();
			File conf = new File(join(dir, File.separator, "conf.xml"));			
			
			if(!conf.exists()){
				error("请在当前目录下个配置conf.xml文件");
			}
            
            String jar = null;
            String groupId = null;
            String artifactId = null;
            String version = null;
           
            //解析conf.xml文件
	        //--------------------
			BufferedReader reader = null;  
	        try {  
	            reader = new BufferedReader(new FileReader(conf));  	            
	            String line = null;  
	            while((line=reader.readLine()) != null) {
	            	line = line.trim();
	            	if(isBlank(line)){
	            		continue;
	            	}
	            	if(line.contains("dependency")){
	            		continue;
	            	}
	            	if(line.contains("<jar>")){
	            		String jarName = line.replaceAll("^.*<jar>(.+)</jar>.*$", "$1");
	            		jar = join(dir, File.separator, jarName);
	            	}
	            	if(line.contains("<groupId>")){
	            		groupId = line.replaceAll("^.*<groupId>(.+)</groupId>.*$", "$1");
	            	}
	            	if(line.contains("<artifactId>")){
	            		artifactId = line.replaceAll("^.*<artifactId>(.+)</artifactId>.*$", "$1");
	            	}
	            	if(line.contains("<version>")){
	            		version = line.replaceAll("^.*<version>(.+)</version>.*$", "$1");
	            	}
                }                  
	        } catch (IOException e) { 
	            e.printStackTrace();  
	        } finally {  
                reader.close();  
            }   
	        
	        //配置参数校验
	        //--------------------
	        if(isBlank(jar)){
	        	error("conf.xml文件中缺少有效的<jar>信息");
	        }
	        if(isBlank(groupId)){
	        	error("conf.xml文件中缺少有效的<groupId>信息");
	        }
	        if(isBlank(artifactId)){
	        	error("conf.xml文件中缺少有效的<artifactId>信息");
	        }
	        if(isBlank(version)){
	        	error("conf.xml文件中缺少有效的<version>信息");
	        }
	        
	        //组装命令
	        //--------------------
	        String mvnArgs = join("install:install-file",
	        		" -Dfile=", jar, 
	        		" -DgroupId=", groupId, 
	        		" -DartifactId=", artifactId, 
	        		" -Dversion=", version, 
	        		" -Dpackaging=jar"	        		
	        		);
	        log("mvnArgs:", mvnArgs);
	        
	        ProcessBuilder builder = new ProcessBuilder("mvn", mvnArgs); 
	        builder.start(); 
			builder.redirectErrorStream(true);
		}catch(Exception e){
			log("向本地仓库里面添加jar包出错，", e.getMessage());
		}		
	}
}
