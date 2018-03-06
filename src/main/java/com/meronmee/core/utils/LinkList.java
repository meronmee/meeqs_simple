package com.meronmee.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 增强型ArrayList,支持链式操作
 * @author Meron
 *
 */
@SuppressWarnings("serial")
public class LinkList<E> extends ArrayList<E> {
	public LinkList(){}
	public LinkList(List<E> list){
		this.addAll(list);
	}
	public LinkList(Set<E> set){
		this.addAll(set);
	}
	public LinkList(E[] array){
		for(E item : array){
			this.add(item);
		}
	}
	
	public LinkList(E item){
		this.add(item);
	}	
	/**
	 * 支持链式操作的append
	 * @param item
	 * @return
	 */
	public LinkList<E> append(E item){
		this.add(item);		
		return this;
	}
	
	public LinkList<E> append (E[] array){
		for(E item : array){
			this.add(item);
		}
		return this;
	}
	
	
	public List<E> toList(){
		List<E> list = this;
		return list;
	}
}
