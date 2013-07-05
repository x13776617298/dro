package com.babytree.apps.comm.tools;

import java.math.BigDecimal;

/**
 * 数学类
 * 
 * @author wangbingqi
 * 
 */
public class BabytreeMath {

	/**
	 * 随机数
	 * 
	 * @param f
	 *            小数点后2位 0.01
	 * @return boolean true随中了 false未中
	 */
	public static boolean random(double f) {
		try {
			BigDecimal bg = new BigDecimal(f);
			f = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (f == 0) {
				return false;
			}
			if (Math.round(Math.random() * 10000 * 100) <= f * 10000) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 取最新随机数
	 * 
	 * @param f
	 *            当前随机数
	 * @param oldF
	 *            上一次的随机数
	 * @return
	 */
	public static double getProportion(double f, double oldF) {
		if (oldF == 0) {
			return f;
		} else if (oldF == 100) {
			return 100;
		} else if (oldF >= f) {
			return oldF;
		} else {
			return (f - oldF) / (100 - oldF) * 100;
		}
	}

}
