package com.meronmee.core.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数
 * @author Meron
 *
 * @param <T>
 */
public class Pager<T> implements Serializable{
	private static final long serialVersionUID = -2953303694134528749L;

	/** 每页最大记录条数 */
	private int pageSize = 20;

	/** 当前页号(从1开始)*/
	private int pageNo = 1;

	/** 总记录条数 */
	private long totalCount = 0;

	
	/** 当前页记录列表 */
	private List<T> records = new ArrayList<T>();

	
	public Pager() {
		this.pageNo = 0;
		this.pageSize = 0;
		this.records = new ArrayList<T>();;
		this.totalCount = 0;
	}
	/**
	 * 根据当前页号、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例。
	 * @param pageNo 当前页号(从1开始)
	 * @param pageSize 页大小（每页数据个数）
	 * @param records 当前页数据列表
	 * @param totalCount 数据总个数
	 */
	public Pager(int pageNo, int pageSize, List<T> records, long totalCount) {
		this.pageNo = pageNo >= 1 ? pageNo : 1;
		this.pageSize = pageSize >= 1 ? pageSize : 1;
		this.records = records;
		this.totalCount = totalCount;
	}
	/**
	 * 根据当前页号、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例。
	 * @param pageNo 当前页号(从1开始)
	 * @param pageSize 页大小（每页数据个数）
	 * @param records 当前页数据列表
	 */
	public Pager(int pageNo, int pageSize, List<T> records) {
		this.pageNo = pageNo >= 1 ? pageNo : 1;
		this.pageSize = pageSize >= 1 ? pageSize : 1;
		this.records = records;
	}
	

	public long getThisPageFirstRecordNumber() {
		return (pageNo - 1) * pageSize + 1;
	}

	public long getThisPageLastRecordNumber() {
		long fullCount = getThisPageFirstRecordNumber() + getPageSize() - 1;
		return getTotalCount() < fullCount ? getTotalCount() : fullCount;
	}

	/**
	 * 下一页
	 * @return
	 */
	public int getNextPageNo() {
		return pageNo + 1;
	}

	/**
	 * 上一页
	 * @return
	 */
	public int getPreviousPageNo() {
		return pageNo-1 >=1 ? (pageNo-1) : 1;
	}
	
	/**
	 * 总页数
	 * @return
	 */
	public int getTotalPage(){
		int toatalPage = 0;
		if(!getRecords().isEmpty()){
	    	if((this.getTotalCount() % this.getPageSize()) == 0){
	    		toatalPage = (int) (this.getTotalCount() / this.getPageSize());
	    	} else {
	    		toatalPage = (int) (this.getTotalCount() / this.getPageSize() + 1);
	    	}
		}
		
		return toatalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public int getPageSize() {
		if(pageSize<=0){
			pageSize = 1;
		}
		return pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}
	
	
    public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public void setPageSize(int pageSize) {
		if(pageSize <= 0){
			pageSize = 20;
		}
		this.pageSize = pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	

	/**
     * 根据页大小（每页数据个数）获取给定页号的第一条数据在总数据中的位置（从1开始）
     * @param pageNo 给定的页号
     * @param pageSize 页大小（每页数据个数）
     * @return 给定页号的第一条数据在总数据中的位置（从1开始）
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) startIndex = 1;
        return startIndex;
    }
    
	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		return pageNo <= 1;
	}
	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}
	/**
	 * 下一页页码
	 */
	public int getNextPage() {
		return this.getNextPageNo();
	}
	/**
	 * 上一页页码
	 */
	public int getPrePage() {
		return this.getPreviousPageNo();
	}
}
