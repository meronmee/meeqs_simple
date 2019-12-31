package ${basePackage!}.manage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackage!}.manage.service.Manage${modelClassName!}Service;
import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};
import ${basePackage!}.${modelVarName!}.dao.${modelClassName!}Dao;

/**
 * ${modelCNName!}管理端端服务类
 * @author Meron
 *
 */
@Service
public class Manage${modelClassName!}ServiceImpl  implements Manage${modelClassName!}Service{
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
	
	
}
