# Java SSM 快速开发框架
- Maven（分环境配置、打包）
- Spring（IOC DI AOP 声明式事务处理）
- Spring MVC（支持Restful风格）
- Spring Shiro（权限控制）
- Mybatis（封装公共操作 + 最少配置方案）
- Druid（数据源配置 sql性能监控)
- Sharding-Sphere（读写分离）
- FreeMarker（模板引擎）
- Logback（日志框架）
- Redis\Caffeine（分布式缓存\本地缓存）
- 统一的异常处理
- 常用工具封装
- 准微服务的形式拆分：目录结构按照微服务的规范划分，要改造成微服务项目，So Easy！

# TODO
- 常用基础服务封装
- Web端组件封装
- H5框架结构封装
- H5预加载组件封装

# 核心组件版本（皆为2019-12-31最新版本）
名称 | 版本
------------ | -------------
Spring | 5.2.2.RELEASE
Shiro | 1.4.2
Mybatis | 3.5.3
ShardingSphere | 4.0.0-RC3
Redis | 3.2.0
FreeMarker | 2.3.29
Logback | 1.2.3


# 目录结构
<pre>
|--src/main/java		- Java主程序目录
|	|-- com.meronmee	- 基础包名，可以通过提供的工具ModifyBasePackage.java来修改
|		|-- com.meronmee.core	- 核心代码包
|			|-- com.meronmee.core.api		- api模块，主要包含Service接口定义，实体对象，常量等。该模块需要保证尽量轻量，尽量不依赖其他模块或jar包
|			|-- com.meronmee.core.service	- 服务层公共代码组件，比如数据库操作、服务层特有的工具等
|			|-- com.meronmee.core.web	    - Web层公共代码组件，主要是MVC、Shiro相关的
|			`-- com.meronmee.core.common	- 服务层和Web层共用的代码组件，包括一些常用工具类等
|		|-- com.meronmee.base	- 基础业务代码包
|			|-- com.meronmee.demo.api		    - 基础业务相关的api
|			|-- com.meronmee.demo.constant	    - 基础业务相关的常量字典
|			|-- com.meronmee.demo.dao	        - 基础业务相关的服务层数据库DAO
|			|-- com.meronmee.demo.service	    - 基础业务相关的服务层服务
|			`-- com.meronmee.demo.controller    - 基础业务相关的Web层控制器
|		|-- com.meronmee.demo	- 示例业务代码包
|			|-- com.meronmee.demo.api		    - 示例业务相关的api
|			|-- com.meronmee.demo.constant	    - 示例业务相关的常量字典
|			|-- com.meronmee.demo.dao	        - 示例业务相关的服务层数据库DAO
|			|-- com.meronmee.demo.service	    - 示例业务相关的服务层服务
|			`-- com.meronmee.demo.controller    - 示例业务相关的Web层控制器
|		`-- com.meronmee.manage- 管理端相关代码包
|			|-- com.meronmee.demo.service	    - 管理端相关的服务层服务
|			`-- com.meronmee.demo.controller    - 管理端相关的Web层控制器
|
|
|--src/main/resources	- 配置文件目录
|	|-- common.config	- Web和Service层共用的配置参数，其中common.all.properties是所有环境公用的，common.properties_*是分环境的
|	|-- common.spring	- Web和Service层共用的Spring配置文件
|	|-- service.config	- Service层配置参数，其中service.all.properties是所有环境公用的，service.properties_*是分环境的
|	|-- service.mapper	- Mybatis数据库SQL Mapper文件
|	|-- service.spring	- Service层使用的Spring配置文件
|	|-- web.config	- Web层配置参数，其中web.all.properties是所有环境公用的，web.properties_*是分环境的
|	|-- web.spring	- Web层使用的Spring配置文件
|	|-- logback.xml	- logback配置文件
|	`-- spring.xml	- Spring主配置文件
|	
|--src/test/java		- 测试目录
|	|-- com.meronmee.test	- 测试代码目录
|	`-- com.meronmee.local	- 本地工具目录
|	    |-- ModifyBasePackage	- 可以修改基础包名com.meronmee为想要的报名
|	    `-- ModuleGenerator	    - 按照规范目录结构创建一个新模块
|		
`--src/test/resources	- 测试配置文件目录
	|-- doc	- 相关文档
	`-- sql	- 数据库脚本
</pre>

# 使用说明
1. 下载代码后先运行 src/test/java/com/meronmee/local/ModifyBasePackage.java 修改基础包名
2. 修改pom.xml中的artifactId
3. 到此已经可以跑起来了
4. 使用src/test/java/com/meronmee/local/ModuleGenerator.java可以生成新模块的基础代码
5. 可使用 build_*.bat 打包对应环境下的WAR发布包。其中, 
  - build_dev.bat 用于打包开发版本（使用src/main/resources/*/config/*.properties_dev配置信息）
  - build_test.bat 用于打包测试版本（使用src/main/resources/*/config/*.properties_test配置信息）
  - build_prep.bat 用于打包预生产版本（使用src/main/resources/*/config/*.properties_prep配置信息）
  - build_prod.bat 用于打包生产版本（使用src/main/resources/*/config/*.properties_prod配置信息）
	
# 开发说明
1. 所有模块都应该按照api、constant、dao、service、controller这样的结构来搭建，其中api和constant属于api层，需要保持干净轻量。dao、service属于服务层，可以直接访问这个模块对应的数据库表，但是不能访问非本模块的表，非本模块的表需要通过其他模块的服务来间接访问。服务层的应该包含大部分层的业务逻辑。service下也可以有非api的接口和实现，这类接口和实现应该只给本模块使用，不应对其他模块开放。模块之间的交互都必须通过api进行。controller属于Web层，对外提供http接口，web层不应该有太多的业务逻辑，一般只是做输入参数的整理校验，输出参数的整理，核心业务逻辑都应该放到服务层，然后通过api来和各模块的服务层交互。
2. Web层绝对不能直接访问数据库！！
