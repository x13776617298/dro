package com.babytree.apps.comm.bitmap.policy;

import java.text.DecimalFormat;

public enum SizeUnit {
	/**
	 * 大小单位 - 字节
	 */
	B {
		@Override
		public float getSize() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_B;
			f = formatUnit(f);
			return f;
		}

		@Override
		public String getSizeString() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_B;
			f = formatUnit(f);
			return f + this.name();
		}
	},
	/**
	 * 大小单位 - KB
	 */
	KB {
		@Override
		public float getSize() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_KB;
			f = formatUnit(f);
			return f;
		}

		@Override
		public String getSizeString() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_KB;
			f = formatUnit(f);
			return f + this.name();
		}
	},
	/**
	 * 大小单位 - MB
	 */
	MB {
		@Override
		public float getSize() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_MB;
			f = formatUnit(f);
			return f;
		}

		@Override
		public String getSizeString() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_MB;
			f = formatUnit(f);
			return f + this.name();
		}
	},
	/**
	 * 大小单位 - GB
	 */
	GB {
		@Override
		public float getSize() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_GB;
			f = formatUnit(f);
			return f;
		}

		@Override
		public String getSizeString() {
			initFormatter(dotLength);
			float f = totalSize / SIZE_GB;
			f = formatUnit(f);
			return f + this.name();
		}
	};

	/**
	 * 获取大小 - 数值格式
	 * 
	 * @return
	 */
	public abstract float getSize();

	/**
	 * 获取大小 - 文本串格式
	 * 
	 * @return
	 */
	public abstract String getSizeString();

	/**
	 * 格式化
	 * 
	 * @param f
	 * @return
	 */
	float formatUnit(float f) {
		return Float.parseFloat(format.format(f));
	}

	/**
	 * 初始化格式化器
	 */
	void initFormatter(int dotLength) {
		dotLength = (dotLength < 0) ? 2 : dotLength;
		StringBuilder ft;
		if (dotLength != 0) {
			ft = new StringBuilder("0.");
			for (int i = 0; i < dotLength; i++) {
				ft.append("0");
			}
		} else {
			ft = new StringBuilder("0");
		}
		format = new DecimalFormat(ft.toString());
	}

	/**
	 * 1B SIZE
	 */
	final float SIZE_B = 1L;

	/**
	 * 1KB SIZE
	 */
	final float SIZE_KB = SIZE_B * 1024;

	/**
	 * 1MB SIZE
	 */
	final float SIZE_MB = SIZE_KB * 1024;

	/**
	 * 1GB SIZE
	 */
	final float SIZE_GB = SIZE_MB * 1024;

	/**
	 * 格式化器
	 */
	DecimalFormat format;

	/**
	 * 字节总数
	 */
	public static long totalSize = 0L;

	/**
	 * 小数点后的位数
	 * <p>
	 * 默认两位小数
	 */
	public static int dotLength = 2;
}
