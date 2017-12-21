# Java SSM 快速开发框架
- Maven（分环境配置、打包）
- Spring（IOC DI AOP 声明式事务处理）
- Spring MVC（支持Restful风格）
- Sping Shiro权限控制
- Mybatis（封装公共操作 + 最少配置方案）
- Druid（数据源配置 sql性能监控)
- FreeMarker（模板引擎）
- Logback（日志框架）
- 统一的异常处理

# 核心组件版本（皆为2017-12-21最新版本）
名称 | 版本
------------ | -------------
Spring | 5.0.2.RELEASE
Shiro | 1.3.2
Mybatis | 3.4.5
FreeMarker | 2.3.27-incubating
Logback | 1.2.3


# 目录结构
<pre>
|--src/main/java		- Java主程序目录
|	|-- com.meronmee	- 基础包名，可以通过提供的工具ModifyBasePackage.java来修改
|		|-- com.meronmee.core	- 核心代码包
|		|-- com.meronmee.app	- 客户端相关代码包
|		|-- com.meronmee.manage- 管理端相关代码包
|		|-- com.meronmee.base	- app和manage公用的业务层代码包
|		`-- com.meronmee.demo	- 示例目录，打包发布时会排除
|			|-- com.meronmee.demo.code		- 模板目录，一个小的代码库，贴近实际代码，直接copy使用
|			|-- com.meronmee.demo.example	- 范例目录，注释讲解比较详细
|			`-- com.meronmee.demo.test		- 测试目录
|
|
|--src/main/resources	- 主配置文件目录
|	|-- system.properties*	- 系统配置文件
|	|-- setting.properties*	- 业务配置文件
|	`-- spring-*.xml	- spring相关的配置文件
|	
|--src/test/java		- 测试文件目录
|	|-- com.meronmee.local	- 本地工具目录
|	`-- com.meronmee.test	- 测试代码目录
|		
`--src/test/resources	- 测试配置文件目录
	|-- doc	- 相关文档
	`-- sql	- 数据库脚本
</pre>

# 使用说明
1. 下载代码后先运行 src/test/java/com/meronmee/local/ModifyBasePackage.java 修改基础包名
2. 修改pom.xml中的artifactId
3. 到此已经可以跑起来了
4. 可使用 build_*.bat 打包对应环境下的WAR发布包。其中, 
  - build_dev.bat 用于打包开发版本（使用src/main/resources/config/*.properties_dev配置信息）
  - build_test.bat 用于打包测试版本（使用src/main/resources/config/*.properties_test配置信息）
  - build_prep.bat 用于打包预生产版本（使用src/main/resources/config/*.properties_prep配置信息）
  - build_prod.bat 用于打包生产版本（使用src/main/resources/config/*.properties_prod配置信息）
	

