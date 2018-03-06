package ${basePackage!}.${moduleRootPackage!}.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackage!}.${moduleRootPackage!}.service.${moduleClassPrefix!}${modelClassName!}Service;
import ${basePackage!}.base.dao.${modelClassName!}Dao;
import ${basePackage!}.base.model.${modelClassName!};
import ${basePackage!}.base.service.BaseService;
import ${basePackage!}.core.dao.helper.Pager;
import ${basePackage!}.core.dao.helper.SqlKey;

@Service
public class ${moduleClassPrefix!}${modelClassName!}ServiceImpl extends BaseService implements ${moduleClassPrefix!}${modelClassName!}Service{
	@Autowired
	private ${modelClassName!}Dao ${modelVarName!}Dao;
	
	/**
	 * 查询${modelCNName!}列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<${modelClassName!}> getList(int offset, int limit) {		
		return ${modelVarName!}Dao.getList(offset, limit);
	}	
	/**
	 * 查询${modelCNName!}列表
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getList() {
		Map<String, Object> params = new HashMap<>();
		params.put("nickname", "小米");    	
		Pager<Map<String, Object>> pager = this.service.query(SqlKey.demo_demoQueryCount, SqlKey.demo_demoQueryList, 1, 3, params);
		return pager.getRecords();
	}
	
	
	
}
