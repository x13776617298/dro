package com.handmark.pulltorefresh.library.internal;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 这个类是为了规范Adapter，定义了数据域及获取数据域的这个方法。
 * @author gaierlin
 * @param <T> 任务数据类型
 */
public class BabyTreeBaseAdapter<T> extends BaseAdapter {
	
	private LinkedList<T> mListItems;
	
	private static int DIGITAL_ZERO = 0;// 表示无数据。
	private static int DEFAULT_INIT_VALUE = -1;
	private int mCount = DEFAULT_INIT_VALUE;// 为-1，防止取余时被0除，而报错误。
	
	public Context mContext;
	public LayoutInflater mInflater;
	public BabyTreeBaseAdapter(Context context){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mListItems = new LinkedList<T>();
	}
	
	/**
	 * 清理数据域
	 */
	public void clear(){
		if(mListItems!=null){
			mListItems.clear();
		}
	}
	/**
	 * 为数据域添加多个数据
	 * @param data
	 */
	public void setData(List<T> data){
		if(data!=null){
			mListItems.addAll(data);
		}
	}
	
	/**
	 * 设置多个数据到数据域的头部
	 * @param datas
	 */
	public void setMultitermDataToHader(LinkedList<T> datas){
		if(datas!=null){
			mListItems.addAll(0, datas);
		}
	}
	
	/**
	 * 设置多个数据到数据域的头部
	 * @param datas
	 */
	public void setMultitermDataToHader(List<T> datas){
		if(datas!=null){
			mListItems.addAll(0, datas);
		}
	}
	
	/**
	 * 设置多个数据到数据域的末尾
	 * @param datas
	 */
	public void setMultitermDataToFooter(List<T> datas){
		if(datas!=null){
			mListItems.addAll(getCount(), datas);
		}
	}
	/**
	 * 设置多个数据到数据域的末尾
	 * @param datas
	 */
	public void setMultitermDataToFooter(LinkedList<T> datas){
		if(datas!=null){
			mListItems.addAll(getCount(), datas);
		}
	}
	
	/**
	 * 将数据添加到数据域头部
	 * @param data
	 */
	public void setDataFirst(T data){
		if(data!=null){
			mListItems.addFirst(data);
		}
	}
	
	/**
	 * 将数据添加到数据域的末尾
	 * @param data
	 */
	public void setDataLast(T data){
		if(data!=null){
			mListItems.addLast(data);
		}
	}
	
	/**
	 * 数据将按顺序添加到数据域中
	 * @param data
	 */
	public void setData(T data){
		if(data!=null){
			mListItems.add(data);
		}
	}
	
	/**
	 * 移除指定position的数据，返回被移除的对像。
	 * @param position
	 * @return
	 */
	public T removeData(int position){
		T result = null;
		if(mListItems!=null){
			result = mListItems.remove(position);
		}
		return result;
	}
	
	/**
	 * 移除指定T的数据,返回boolean。
	 * @return
	 */
	public boolean removeData(T t){
		boolean result = false;
		if(mListItems!=null){
			result = mListItems.remove(t);
		}
		return result;
	}
	
	/**
	 * 移除数据域的第一条数据
	 * @return
	 */
	public T removeFirst(){
		T result = null;
		if(mListItems!=null){
			result = mListItems.removeFirst();
		}
		return result;
	}
	
	/**
	 * 移除数据域的最后一条数据
	 * @return
	 */
	public T removeLast(){
		T result = null;
		if(mListItems!=null){
			result = mListItems.removeLast();
		}
		return result;
	}
	
	@Override
	public int getCount() {
		int result = DIGITAL_ZERO;
		if (mListItems != null && mListItems.size() != DIGITAL_ZERO)
			result = mListItems.size();
		mCount = (result == DIGITAL_ZERO ? DEFAULT_INIT_VALUE : result);
		return result;
	}

	/**
	 * 得到指定Position的数据类型
	 */
	@Override
	public T getItem(int position) {
		if (isEmpty()) {// 防止取余时被0除
			return null;
		}
		return mListItems.get(position % mCount);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
