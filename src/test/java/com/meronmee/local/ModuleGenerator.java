package com.meronmee.local;

import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.common.util.FileUtil;
import com.meronmee.test.Log;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 快速生成一个新模块相关的基础代码，包括：Mapper文件、Model、Controller、Dao、Service等
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

        //----------api----------
        Log.info("生成Model...");  
        Template templateModel = freemarkerConfiguration.getTemplate("Xxx.java", ENCODING);
        String filePathModel = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/domain/"+modelClassName+".java");
        saveDataToFile(data, templateModel, filePathModel);

        Log.info("生成Api...");
        Template templateApi = freemarkerConfiguration.getTemplate("XxxApi.java", ENCODING);
        String filePathApi = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/api/"+modelClassName+"Api.java");
        saveDataToFile(data, templateApi, filePathApi);

        Log.info("生成Constant...");
        Template templateConst = freemarkerConfiguration.getTemplate("XxxConst.java", ENCODING);
        String filePathConst = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/constant/"+modelClassName+"Const.java");
        saveDataToFile(data, templateConst, filePathConst);

        //----------service----------
        Log.info("生成Mapper..."); 
        Template templateMapper = freemarkerConfiguration.getTemplate("XxxMapper.xml", ENCODING);
        String filePathMapper = BaseUtils.mergePath(mapperDir, data.get("modelClassName")+"Mapper.xml");        
        saveDataToFile(data, templateMapper, filePathMapper); 

        Log.info("生成Dao..."); 
        Template templateDao = freemarkerConfiguration.getTemplate("XxxDao.java", ENCODING);
        String filePathDao = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/dao/"+modelClassName+"Dao.java");
        saveDataToFile(data, templateDao, filePathDao);

        Log.info("生成ApiImpl...");
        Template templateApiImpl = freemarkerConfiguration.getTemplate("XxxApiImpl.java", ENCODING);
        String filePathApiImpl = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/service/impl/"+modelClassName+"ApiImpl.java");
        saveDataToFile(data, templateApiImpl, filePathApiImpl);

        Log.info("生成Service...");
        Template templateService = freemarkerConfiguration.getTemplate("XxxService.java", ENCODING);
        String filePathService = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/service/" + modelClassName+"Service.java");
        saveDataToFile(data, templateService, filePathService);

        Log.info("生成ServiceImpl...");
        Template templateServiceImpl = freemarkerConfiguration.getTemplate("XxxServiceImpl.java", ENCODING);
        String filePathServiceImpl = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/service/impl/"+modelClassName+"ServiceImpl.java");
        saveDataToFile(data, templateServiceImpl, filePathServiceImpl);

        //----------web----------
        Log.info("生成Controller...");
        Template templateController = freemarkerConfiguration.getTemplate("XxxAction.java", ENCODING);
        String filePathController = BaseUtils.mergePath(codeDir, "/"+data.get("modelVarName")+"/controller/"+modelClassName+"Action.java");
        saveDataToFile(data, templateController, filePathController);

        //----------manage----------
        Log.info("生成ManageController...");
        Template templateManageController = freemarkerConfiguration.getTemplate("ManageXxxAction.java", ENCODING);
        String filePathManageController = BaseUtils.mergePath(codeDir, "/manage/controller/Manage"+modelClassName+"Action.java");
        saveDataToFile(data, templateManageController, filePathManageController);

        Log.info("生成ManageService...");
        Template templateManageService = freemarkerConfiguration.getTemplate("ManageXxxService.java", ENCODING);
        String filePathManageService = BaseUtils.mergePath(codeDir, "/manage/service/Manage" + modelClassName+"Service.java");
        saveDataToFile(data, templateManageService, filePathManageService);

        Log.info("生成ManageServiceImpl...");
        Template templateManageServiceImpl = freemarkerConfiguration.getTemplate("ManageXxxServiceImpl.java", ENCODING);
        String filePathManageServiceImpl = BaseUtils.mergePath(codeDir, "/manage/service/impl/Manage"+modelClassName+"ServiceImpl.java");
        saveDataToFile(data, templateManageServiceImpl, filePathManageServiceImpl);

        Log.info("生成新模块基础代码完成！");     
	}
	
	//初始化相关变量
	private static void init() throws Exception{
		//获取项目根目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple
    	rootDir = new File("").getCanonicalPath();
    	//模板文件目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple\src\test\resources\generator
    	templateDir = BaseUtils.mergePath(rootDir, "src/test/resources/generator");
    	//模板文件目录，如：E:\WorkSpace\eclipse\mee\meeqs_simple\src\main\resources\service\mapper
    	mapperDir = BaseUtils.mergePath(rootDir, "src/main/resources/service/mapper");
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
