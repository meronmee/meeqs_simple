package ${basePackage!}.${modelVarName!}.api;

import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};
import com.meronmee.core.api.domain.RequestInfo;

/**
 * ${modelCNName!}相关服务接口
 * @author Meron
 * @date 2019-12-30 16:37
 */
public interface ${modelClassName!}Api {
    /**
     * 根据Id获取${modelCNName!}
     */
    public ${modelClassName!} retrieve(Long id);

}
