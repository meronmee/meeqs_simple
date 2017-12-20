package com.meronmee.core.shiro;

import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.meronmee.core.commons.Const;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.EncryptUtil;

/**
 * 自定义密码验证器
 * @author Meron
 *
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
	
	/**
	 * 验证密码
	 * 
	 * @param tokenCredentials 用户输入的凭证（密码），本系统中是char[]类型的
	 * @param accountCredentials 从数据库取出的凭证（密码），之前在CustomRealm#doGetAuthenticationInfo中设置的，本系统中是String类型的
	 * 
	 * @return true:验证通过，false:验证不通过，将抛出一个org.apache.shiro.authc.IncorrectCredentialsException
	 */
	@Override
	protected boolean equals(Object tokenCredentials, Object accountCredentials){        
		//用户输入的密码
        String inputPassword = null;
        if(tokenCredentials instanceof char[]){
        	inputPassword = String.valueOf((char[])tokenCredentials);//直接String.valueOf会得到对象的HashCode
        } else {
        	inputPassword = BaseUtils.toString(inputPassword);
        }
        
        //从数据库取出的密码
        String userPassword = BaseUtils.toString(accountCredentials);//String类型的
        
        //可以在此进行MD5等加密处理
        //String cryptedPassword = EncryptUtil.md5(inputPassword, Const.MESSAGE_DIGEST_SALT);
        //return cryptedPassword.equalsIgnoreCase(userPassword);
        
		return inputPassword.equalsIgnoreCase(userPassword);
	}
	
}
