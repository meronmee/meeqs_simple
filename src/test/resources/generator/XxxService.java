package ${basePackage!}.${modelVarName!}.service;

import java.util.List;
import java.util.Map;

import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};

/**
 * ${modelCNName!}服务端内部服务类
 * @author Meron
 *
 */
public interface ${modelClassName!}Service {
	/**
	 * 查询${modelCNName!}列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<${modelClassName!}> getList(int offset, int limit);
}
