package ${basePackage!}.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ${basePackage!}.base.model.${modelClassName!};

/**
 * ${modelCNName!} Dao<p>
 * 	· 类路径需要和src/main/resources/mapper/${modelClassName!}Mapper.xml中的namespace一致<p>
 * 	· 方法和参数要和${modelClassName!}Mapper.xml中的ID参数一致<p>
 * 	· 无需增加Dao实现类<p>
 * 
 * @author Meron
 *
 */
public interface ${modelClassName!}Dao {

	/* -----------------------------------------------
	 * 					App端相关方法
	 * -----------------------------------------------
	 */
    /**
     * 分页查询${modelCNName!}列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<${modelClassName!}> getList(@Param("offset") int offset, @Param("limit") int limit);
    
    

	/* -----------------------------------------------
	 * 					管理端相关方法
	 * -----------------------------------------------
	 */
    
    
}
