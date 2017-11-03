package com.meronmee.pattern.struct.facade;

import com.meronmee.app.Log;

public class TreatmentFacade {
	public void goutong(){
		Log.info("和病人沟通相关事宜");		
	}
	public void zouliucheng(){
		Log.info("走具体流程...");		

		HospitalGuahao guahao = new HospitalGuahao();
		HospitalHuajia huajia = new HospitalHuajia();
		HospitalJiuzheng jiuzheng = new HospitalJiuzheng();
		HospitalQuyao quyao = new HospitalQuyao();
		guahao.chooseKeshi();
		guahao.chooseYishengc();
		guahao.jiaofei();
		jiuzheng.jiancha();
		huajia.jiaofei();
		jiuzheng.chouxie();
		jiuzheng.chankanchouxiejieguo();
		jiuzheng.kaiyaofang();
		huajia.jiaofei();
		quyao.quhao();
		quyao.lingyao();
	}
}
