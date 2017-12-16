package com.meronmee.local;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.meronmee.core.utils.BaseUtils;
import com.meronmee.test.Log;

/**
 * 修改基础包名 com.meronmee 为自定义的包名
 * @author Meron
 *
 */
public class ModifyBasePackage {
	/**当前基础包名*/
	public static final String BASE_PACKAGE = "com.meronmee";
	/**目标基础包名*/
	public static final String NEW_BASE_PACKAGE = "com.abc.def";
	/**文件编码*/
	public static final String ENCODING = "UTF-8";  
	
	//------------------------
	private static final Pattern targetNamePattern = Pattern.compile("^.+\\.(java|jsp|md|xml(_.+)?|properties(_.+)?)$", Pattern.CASE_INSENSITIVE);  
	private static final Pattern basePackagePattern = Pattern.compile(BASE_PACKAGE.replaceAll("\\.", "\\\\."));
	private static final Pattern basePackageDirPattern = Pattern.compile(BASE_PACKAGE.replaceAll("\\.", "/"));
	private static final String newBasePackageDir = NEW_BASE_PACKAGE.replaceAll("\\.", "/");
	
	
	public static void main(String[] args) throws Exception {
		run();		
	}
	
	private static void run()  throws Exception{
		String baseDir = getBasrDir();//如：E:\WorkSpace\eclipse\mee\meeqs_simple
		
		Log.info("处理文件...");
		processFile(new File(BaseUtils.mergePath(baseDir, "pom.xml")));
		processFile(new File(BaseUtils.mergePath(baseDir, "README.md")));
		processFile(new File(BaseUtils.mergePath(baseDir, "src")));
		Log.info("处理文件完成");

		Log.info("处理文件夹...");
		processFolder(baseDir);
		Log.info("处理文件夹完成");
		
	}
	/**
	 * 重命名文件夹<p>
	 * 只处理src/main/java/和src/test/java/下面的Java包目录
	 * @param baseDir
	 * @throws Exception
	 */
	private static void processFolder(String baseDir) throws Exception{
		/*
			不能直接	重命名src/main/java/a/b/c 为src/main/java/x/y/z，这样会留下/a/b目录
		 */
		
		//重命名src/main/java/a/b/c 到 临时目录src/main/java2/x/y/z
		String srcDir = BaseUtils.mergePath(baseDir, "src/main/java/", BASE_PACKAGE.replaceAll("\\.", "/"));
		String testDir = BaseUtils.mergePath(baseDir, "src/test/java/", BASE_PACKAGE.replaceAll("\\.", "/"));
		
		String srcTargetDir =  BaseUtils.mergePath(baseDir, "src/main/java2/", NEW_BASE_PACKAGE.replaceAll("\\.", "/"));
		String testTargetDir =  BaseUtils.mergePath(baseDir, "src/test/java2/", NEW_BASE_PACKAGE.replaceAll("\\.", "/"));
		
		FileUtils.moveDirectory(new File(srcDir), new File(srcTargetDir));
		FileUtils.moveDirectory(new File(testDir), new File(testTargetDir));
		
		//删除src/main/java/和src/test/java/
		File srcMainFile = new File(BaseUtils.mergePath(baseDir, "src/main/java/"));
		File srcTestFile = new File(BaseUtils.mergePath(baseDir, "src/test/java/"));
		FileUtils.deleteDirectory(srcMainFile);
		FileUtils.deleteDirectory(srcTestFile);
		
		//删除临时目录
		FileUtils.moveDirectory(new File(BaseUtils.mergePath(baseDir, "src/main/java2/")), srcMainFile);
		FileUtils.moveDirectory(new File(BaseUtils.mergePath(baseDir, "src/test/java2/")), srcTestFile);
	}
	
	/**
	 * 处理文件的内容，替换相关包路径和包名
	 * @param file 目标文件，如果是文件夹，则递归处理其下的子文件
	 */
	private static void processFile(File file){
		if(file == null || !file.exists()){
			return;
		}
		
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File thisFile : files){
				processFile(thisFile);
			}
		} else {
			String fileName = file.getAbsolutePath();
			if(!isTarget(fileName)){
				//Log.info("跳过文件:", fileName);				
				return;
			}

			String content = read2String(file);
			if(BaseUtils.isBlank(content)){
				//Log.info("忽略文件[空文件]:", fileName);	
				return;
			}
			
			String newContent = basePackagePattern.matcher(content).replaceAll(NEW_BASE_PACKAGE);
			newContent = basePackageDirPattern.matcher(newContent).replaceAll(newBasePackageDir);
			
			if(!content.equals(newContent)){
				Log.info("处理文件:", fileName);
				write2File(file, newContent);
			} else {
				//Log.info("忽略文件[无变化]:", fileName);				
			}
			
			content = null;
			newContent = null;
		}
	}
	
	/**
	 * 判断是否是要处理的文件
	 * @param fileName
	 * @return
	 */
	private static boolean isTarget(String fileName){
		if(BaseUtils.isBlank(fileName)){
			return false;
		}
		return targetNamePattern.matcher(fileName).matches();
	}
	/**
	 * 获取工程根目录
	 * @return
	 */
	private static String getBasrDir(){
	   String currentDir = new File("").getAbsolutePath();//获取绝对路径
	   return currentDir;
	}	
	
	/**
	 * 将文件内容到String中
	 */
	private static String read2String(File file) {
    	try{
    		/*    		
	        Long filelength = file.length();  
	        byte[] filecontent = new byte[filelength.intValue()];  
	        FileInputStream in = new FileInputStream(file);  
	        in.read(filecontent);  
	        in.close();  
	        
	        return new String(filecontent, ENCODING);  
	        */
	        
	        return FileUtils.readFileToString(file, ENCODING);
    	}catch(Exception e){
    		Log.info("读取文件失败:", file.getAbsolutePath());
    		e.printStackTrace();
    		return null;
    	}
    }  
	/**
	 * 将String写入文件
	 * @param file
	 * @param data
	 */
	private static void write2File(File file, String data) {
    	if(BaseUtils.isBlank(data)){
    		 return;
    	}

     	try{
     		/*
	    	 Writer writer = new FileWriter(file);  
	         writer.write(fileContent);  
	         writer.close(); 
	         */
     		
     		FileUtils.writeStringToFile(file, data, ENCODING);
    	}catch(Exception e){
    		Log.info("写入文件失败:", file.getAbsolutePath());
    		e.printStackTrace();
    	}
    }  
   
    
}
