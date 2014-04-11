package com.foboy.gogo_android.controls;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.graphics.Color;

import java.util.List;

import com.foboy.gogo_android.GlobalConfig;
import com.foboy.gogo_android.R;
import com.foboy.gogo_android.common.StringUtils;

/**
 * Based on PredicateLayout by Henrik Gustafsson
 * 
 * @see http
 *      ://stackoverflow.com/questions/549451/line-breaking-widget-layout-for
 *      -android
 * @license http://creativecommons.org/licenses/by-sa/2.5/
 */
public class TagListView extends ViewGroup implements OnHierarchyChangeListener {

	private int mLineHeight;
	private final int mHorizontalSpacing;
	private final int mVerticalSpacing;
	private final ArrayList<TagListener> mListeners;

	private final ArrayList<String> mTags = new ArrayList<String>();
	private final ArrayList<TagView> tTags = new ArrayList<TagView>();
	private final LayoutInflater mInflater;
	
	private boolean binded;
	

	public static class LayoutParams extends ViewGroup.LayoutParams {
		public final int mHorizontalSpacing;
		public final int mVerticalSpacing;

		/**
		 * @param horizontalSpacing
		 *            Pixels between items, horizontally
		 * @param verticalSpacing
		 *            Pixels between items, vertically
		 */
		public LayoutParams(int horizontalSpacing, int verticalSpacing) {
			super(0, 0);
			this.mHorizontalSpacing = horizontalSpacing;
			this.mVerticalSpacing = verticalSpacing;
		}
	}

	public static interface TagListener {
		void onAddedTag(String tag);

		void onRemovedTag(String tag);
	}

	public TagListView(Context context) {
		this(context, null, 0);
	}

	public TagListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TagListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagListView);
		mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.TagListView_horizontal_spacing, 1);
		mVerticalSpacing = a.getDimensionPixelSize(R.styleable.TagListView_vertical_spacing, 1);
		a.recycle();

		mListeners = new ArrayList<TagListener>();
		setOnHierarchyChangeListener(this);
	}

	public void addTagListener(TagListener listener) {
		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
		}
	}

	public void removeTagListener(TagListener listener) {
		mListeners.remove(listener);
	}

	public void addTag(String tag,int id) {
		mTags.add(tag);
		inflateTagView(tag, id);
	}

	private void inflateTagView(String tag,int id) {
		TagView tagView = (TagView) mInflater.inflate(R.layout.tag, null);
		tagView.setText(tag);
		tagView.setId(id);
		tagView.setName(tag);
		addView(tagView);
		tTags.add(tagView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);

		final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		final int count = getChildCount();
		int lineHeight = 0;

		int xpos = getPaddingLeft();
		int ypos = getPaddingTop();

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
						MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

				final int childWidth = child.getMeasuredWidth();
				lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.mVerticalSpacing);

				if (xpos + childWidth > width) {
					xpos = getPaddingLeft();
					ypos += lineHeight;
				}

				xpos += childWidth + lp.mHorizontalSpacing;
			}
		}
		this.mLineHeight = lineHeight;

		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			height = ypos + lineHeight;

		}
		else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			if (ypos + lineHeight < height) {
				height = ypos + lineHeight;
			}
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(mHorizontalSpacing, mVerticalSpacing);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		if (p instanceof LayoutParams) return true;
		return false;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		final int width = r - l;
		int xpos = getPaddingLeft();
		int ypos = getPaddingTop();

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final int childWidth = child.getMeasuredWidth();
				final int childHeight = child.getMeasuredHeight();
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (xpos + childWidth > width) {
					xpos = getPaddingLeft();
					ypos += mLineHeight;
				}
				child.layout(xpos, ypos, xpos + childWidth, ypos + childHeight);
				xpos += childWidth + lp.mHorizontalSpacing;
			}
		}
	}



	@Override
	public void onChildViewAdded(View parent, View child) {
		if (child instanceof TagView) {
			TagView tagView = (TagView) child;

			for (TagListener listener : mListeners) {
				listener.onAddedTag(tagView.getText().toString());
			}
		}
	}

	@Override
	public void onChildViewRemoved(View parent, View child) {
		if (child instanceof TagView) {
			TagView tagView = (TagView) child;

			for (TagListener listener : mListeners) {
				listener.onRemovedTag(tagView.getText().toString());
			}
		}
	}
	
	public List<Integer> selectedTagIds()
	{
		List<Integer> selectedIds = new ArrayList<Integer>();
		List<TagView> selectedTags = new ArrayList<TagView>();
		for(TagView tag : tTags)
		{
			if(tag.isSelected())
			{
				selectedTags.add(tag);
				selectedIds.add(tag.getId());
			}
		}
		if(selectedTags.size() > 0 )
		{
			GlobalConfig.getInstance().setFirstCatalogName(selectedTags.get(0).getName());
		}
		return selectedIds;
	}

	public boolean isBinded() {
		return binded;
	}

	public void setBinded(boolean binded) {
		this.binded = binded;
	}


}