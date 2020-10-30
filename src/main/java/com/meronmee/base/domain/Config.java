package com.meronmee.base.domain;

import com.meronmee.core.api.annotation.Table;
import com.meronmee.core.api.domain.Model;

/**
 * 配置信息
 */
@Table("config")
public class Config extends Model {

    /** 配置项名称 */
    private String configKey;

    /** 配置项值 */
    private String configValue;

    /** 配置项说明 */
    private String remark;

    /*------ Getter/Setter ------*/

    /** 配置项名称 */
    public String getConfigKey() {
        return this.configKey;
    }

    /** 配置项名称 */
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    /** 配置项值 */
    public String getConfigValue() {
        return this.configValue;
    }

    /** 配置项值 */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    /** 配置项说明 */
    public String getRemark() {
        return this.remark;
    }

    /** 配置项说明 */
    public void setRemark(String remark) {
        this.remark = remark;
    }


}
