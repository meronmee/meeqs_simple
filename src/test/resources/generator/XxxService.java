package ${basePackage!}.${moduleRootPackage!}.service;

import java.util.List;
import java.util.Map;

import ${basePackage!}.base.model.${modelClassName!};

/**
 * ${modelCNName!} ${moduleRootPackage!}端服务接口
 * @author Meron
 *
 */
public interface ${moduleClassPrefix!}${modelClassName!}Service {
	/**
	 * 查询${modelCNName!}列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<${modelClassName!}> getList(int offset, int limit);
	
	/**
	 * 查询${modelCNName!}列表
	 * @return
	 */
	public List<Map<String, Object>> getList();
}
