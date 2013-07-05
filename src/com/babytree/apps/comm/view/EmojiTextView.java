package com.babytree.apps.comm.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.AttributeSet;
import android.widget.TextView;

public class EmojiTextView extends TextView {
	private Context context;

	public EmojiTextView(Context context) {
		super(context);
		this.context = context;
	}

	public EmojiTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setEmojiText(String text) {
		// text = EmojiUtils.convertTag(text);
		CharSequence spanned = Html.fromHtml(text, emojiGetter, null);
		setText(spanned);

	}

	public void appendHtml(String text) {
		CharSequence spanned = Html.fromHtml(text, emojiGetter, null);
		append(spanned);
	}

	private ImageGetter emojiGetter = new ImageGetter() {
		public Drawable getDrawable(String source) {
			int id = getResources().getIdentifier(source, "drawable", context.getPackageName());

			Drawable emoji = getResources().getDrawable(id);
			emoji.setBounds(0, 0, emoji.getIntrinsicWidth(), emoji.getIntrinsicHeight());
			return emoji;
		}
	};
}