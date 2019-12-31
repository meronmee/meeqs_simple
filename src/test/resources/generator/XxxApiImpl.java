package ${basePackage!}.${modelVarName!}.service.impl;

import ${basePackage!}.${modelVarName!}.api.${modelClassName!}Api;
import ${basePackage!}.${modelVarName!}.dao.${modelClassName!}Dao;
import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};
import ${basePackage!}.core.api.domain.RequestInfo;
import ${basePackage!}.core.common.util.BaseUtils;
import ${basePackage!}.core.common.util.ShortCache;
import ${basePackage!}.core.service.database.DateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * ${modelCNName!} 接口服务实现
 * @author Meron
 * @date 2019-12-30 19:02
 */
@Service
public class ${modelClassName!}ApiImpl implements ${modelClassName!}Api{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ${modelClassName!}Dao ${modelVarName!}Dao;
	@Autowired
	private DateService dataService;

    /**
     * 查询用户
     */
    @Override
    public ${modelClassName!} retrieve(Long id){
        if(BaseUtils.isNull0(id)){
            return null;
        }
        return dataService.retrieveModel(${modelClassName!}.class, id);
    }

}
