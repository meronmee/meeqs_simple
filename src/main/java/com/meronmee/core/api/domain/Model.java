package com.meronmee.core.api.domain;

import com.meronmee.core.api.annotation.AttrIgnore;
import com.meronmee.core.api.util.ApiUtils;
import com.meronmee.core.api.util.ToMapUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * 模型对象的基类
 *
 * @author Meron
 */
public abstract class Model implements Serializable{
    private static final long serialVersionUID = 1L;

    protected Long id;
    
    protected Date createTime;
    
    protected Date updateTime;    
    
    protected Boolean deleteStatus = false;
    
    
    public Model(){
    	this.createTime = new Date();    	
    	this.updateTime = new Date();
    	this.deleteStatus = false;
    }
    
    

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setId(BigInteger id) {
		this.id = ApiUtils.toLong(id);
	}
	public void setId(Integer id) {
		this.id = ApiUtils.toLong(id);
	}
	
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean getDeleteStatus() {
		if(deleteStatus==null){
			deleteStatus = false;
		}
		return deleteStatus;
	}
	public void setDeleteStatus(Boolean deleteStatus) {
		if(deleteStatus==null){
			deleteStatus = false;
		}
		this.deleteStatus = deleteStatus;
	}



	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Model)) {
            return false;
        }
        Model model = (Model) obj;
        
        return model.getId()!=null && this.getId()!=null && model.getId().longValue() == this.getId().longValue();
    }

    @Override
    public int hashCode() {
        if (id == null) {
            id = -1L;
        }
        return Long.valueOf(id + 1000L).hashCode();
    }

    @Override
    public String toString(){
        List<Field> fields=new ArrayList<>();
        for (Class<?> superClass = this.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] f=superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(f));
        }

        StringBuilder sb = new StringBuilder("{");
        for (Field field : fields) {
            String fieldName = field.getName();

            //@ToMap(ignore=true), 忽略
            if(ToMapUtils.ignore(field)){
                continue;
            }
            //@AttrIgnore
            if(field.isAnnotationPresent(AttrIgnore.class)){
                continue;
            }

            //继承自Serializable接口的字段
            if("serialVersionUID".equals(fieldName)){
                continue;
            }

            //处理字段值
            //------------------------------------
            Object value = ApiUtils.getFieldValue(this, field);//调用Getter方法获取字段的值
            if(value == null){
                sb.append(fieldName).append("=null").append(", ");
                continue;
            }
            sb.append(fieldName).append("=").append(value).append(", ");
        }//for
        String str = sb.toString();

        return str.replaceAll(", $", "}");
    }

}