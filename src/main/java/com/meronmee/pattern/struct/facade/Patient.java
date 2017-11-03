package com.meronmee.pattern.struct.facade;

public class Patient {
	public static void main(String[] args){
		//之前流程
		/*
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
		*/
		
		TreatmentFacade facade = new TreatmentFacade();
		facade.goutong();
		facade.zouliucheng();
		
		
	}
}
