package ${basePackage!}.manage.service;

import java.util.List;
import java.util.Map;

import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};

/**
 * ${modelCNName!}管理端服务类
 * @author Meron
 *
 */
public interface Manage${modelClassName!}Service {
	/**
	 * 查询${modelCNName!}列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<${modelClassName!}> getList(int offset, int limit);
}
