package ${basePackage!}.base.model;

import ${basePackage!}.core.annotation.Table;
import ${basePackage!}.core.model.Model;

/**
 * 
 * @author Meron
 *
 */
@Table("${modelVarName!}")
public class ${modelClassName!} extends Model {
	private static final long serialVersionUID = 1;

	/**
	 * 字段1
	 */
	private String field1;

	/**
	 * 字段2 - 1:字典1, 2:字典2, 3:字典3
	 */
	private Integer field2;
	
	/**
	 * 字段3
	 */
	private Boolean field3;

	//-------------------------
	
	//Getter/Setter
	//...
	
	
	
	
}
