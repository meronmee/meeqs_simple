package com.meronmee.local;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.FileUtil;
import com.meronmee.test.Log;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 快速生成一个新模块相关的基础代码，包括：Mapper文件、Model、Controller、Dao、Service
 * Model、Dao统一放在base一级包下面
 * 
 * @author Meron
 *
 */
public class ModuleGenerator { 
	//新模块配置信息
	//---------------------------	
	/**当前基础包名*/
	public static final String BASE_PACKAGE = "com.meronmee";
	
	/**目标模块英文名称*/
	public static final String MODULE_EN_NAME = "order";
	
	/**目标模块中文名称*/
	public static final String MODULE_CN_NAME = "订单";
	
	/**目标模块所属一级包, app:客户端功能, manage:管理端功能. 多个用逗号隔开 */
	public static final String MODULE_ROOT_PACKAGE = "app,manage";
	

	//内部变量
	//---------------------------	
	public static final String ENCODING = "UTF-8";
	public static FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
	public static Configuration freemarkerConfiguration = null;
	public static String rootDir = null;
	public static String codeDir = null;
	public static String mapperDir = null;
	public static String templateDir = null;
	
	
	public static void main(String[] args) throws Exception {
		run();		
	}
	
	private static void run() throws Exception{
		//初始化变量
		init();
		
		//准备模板参数
		String modelClassName = String.valueOf(MODULE_EN_NAME.charAt(0)).toUpperCase()+MODULE_EN_NAME.substring(1);
        Map<String, Object> data = new HashMap<>();
        data.put("basePackage", BASE_PACKAGE);//如：com.meronmee
        data.put("modelClassName", modelClassName);//如：Order
        data.put("modelVarName", StringUtils.uncapitalize(MODULE_EN_NAME));//如：order
        data.put("modelCNName", MODULE_CN_NAME);//如：订单
           
        Log.info("生成Model...");  
        Template templateModel = freemarkerConfiguration.getTemplate("Xxx.java", ENCODING);
        String filePathModel = BaseUtils.mergePath(codeDir, "/base/model/"+modelClassName+".java");        
        saveDataToFile(data, templateModel, filePathModel);     
         
        Log.info("生成Mapper..."); 
        Template templateMapper = freemarkerConfiguration.getTemplate("XxxMapper.xml", ENCODING);
        String filePathMapper = BaseUtils.mergePath(mapperDir, data.get("modelClassName")+"Mapper.xml");        
        saveDataToFile(data, templateMapper, filePathMapper); 
       

        Log.info("生成Dao..."); 
        Template templateDao = freemarkerConfiguration.getTemplate("XxxDao.java", ENCODING);
        String filePathDao = BaseUtils.mergePath(codeDir, "/base/dao/"+modelClassName+"Dao.java");        
        saveDataToFile(data, templateDao, filePathDao);
               
        String[] rootPackages = MODULE_ROOT_PACKAGE.split(",");
        for(String rootPackage : rootPackages){
        	 if(StringUtils.isBlank(rootPackage)){
        		continue;
        	 }
        	 rootPackage = rootPackage.trim();
        	 String  moduleRootPackage = StringUtils.uncapitalize(rootPackage);
        	 String  moduleClassPrefix = String.valueOf(rootPackage.charAt(0)).toUpperCase()+rootPackage.substring(1);
        	 
        	 data.put("moduleRootPackage", moduleRootPackage);//如: app
             data.put("moduleClassPrefix", moduleClassPrefix);//如: App
             
             Log.info("生成"+moduleRootPackage+"下的Controller..."); 
             Template templateController = freemarkerConfiguration.getTemplate("XxxAction.java", ENCODING);
             String filePathController = BaseUtils.mergePath(codeDir, moduleRootPackage, "/controller/"+moduleClassPrefix+modelClassName+"Action.java");        
             saveDataToFile(data, templateController, filePathController);     
             
             Log.info("生成"+moduleRootPackage+"下的Service..."); 
             Template templateService = freemarkerConfiguration.getTemplate("XxxService.java", ENCODING);
             String filePathService = BaseUtils.mergePath(codeDir, moduleRootPackage, "/service/"+moduleClassPrefix+modelClassName+"Service.java");        
             saveDataToFile(data, templateService, filePathService); 
                    
             Log.info("生成"+moduleRootPackage+"下的ServiceImpl..."); 
             Template templateServiceImpl = freemarkerConfiguration.getTemplate("XxxServiceImpl.java", ENCODING);
             String filePathServiceImpl = BaseUtils.mergePath(codeDir, moduleRootPackage, "/service/impl/"+moduleClassPrefix+modelClassName+"ServiceImpl.java");        
             saveDataToFile(data, templateServiceImpl, filePathServiceImpl); 
        }
        
        Log.info("生成新模块基础代码完成！");     
	}
	
	//初始化相关变量
	private static void init() throws Exception{
		//获取项目根目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple
    	rootDir = new File("").getCanonicalPath();
    	//模板文件目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple\src\test\resources\generator
    	templateDir = BaseUtils.mergePath(rootDir, "src/test/resources/generator");
    	//模板文件目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple\src\main\resources\mapper
    	mapperDir = BaseUtils.mergePath(rootDir, "src/main/resources/mapper");
    	//生成代码文件基础目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple\src\main\java\com\meronmee
    	codeDir = BaseUtils.mergePath(rootDir, "src/main/java", BASE_PACKAGE.replace('.', '/'));
    	
        try {
            freemarkerConfiguration = factory.createConfiguration();
            
            //fileTemplateLoader = new FileTemplateLoader(new File(templateDir));
	        freemarkerConfiguration.setTemplateLoader(new FileTemplateLoader(new File(templateDir)));          
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
    
	/**
     * 保存数据到文件中
     * @param data - 数据
     * @param template - 数据模板
     * @param filePath - 结果文件路径
     */
    private static void saveDataToFile(Map<String, Object> data, Template template, String filePath) throws Exception{
    	if(template == null || BaseUtils.isBlank(filePath)){
    		return;
    	}    
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        //创建模板文件
        File file =  new File(filePath);
        File fileDir = file.getParentFile();
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
        file.createNewFile();
        
        FileUtil.writeStringToFile(file, content, ENCODING); //将文档内容写入文档
    }
    
}
