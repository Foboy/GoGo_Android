package com.foboy.gogo_android.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.widget.EditText;
import com.foboy.gogo_android.R;

public class TagView extends EditText implements OnClickListener, AnimationListener {
	public final static String T = "tagview";

    private final int mTagBackground;
    private final int mTagBorder;
    private final Paint mBackgroundPaint;
	private final Paint mBorderPaint;
	private TagView _this;
	private int id;
	private String name;
	private boolean selected = false;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if(this.selected)
		{
			this.setTextColor(getResources().getColor( R.color.tagSelectedColor));
		}
		else
		{
			this.setTextColor(getResources().getColor( R.color.tagTextColor));
		}
	}

	private boolean mSelectedForDelete = false;

    public TagView(Context context) {
		this(context, null, 0);
    }

	public TagView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = this;
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagView);
		mTagBackground = parseColor(context, a, R.styleable.TagView_background, R.color.tagBackgroundDefault);
		mTagBorder = parseColor(context, a, R.styleable.TagView_border, R.color.tagBorderDefault);
		a.recycle();

		mBackgroundPaint = backgroundPaint();
		mBorderPaint = borderPaint();

		setCursorVisible(false);
		setOnClickListener(this);
    }

    private int parseColor(final Context context, final TypedArray a, final int index, final int defaultColorRes){
        //try to get as a resource
        final int resId = a.getResourceId(index,-1);
        if(resId > -1) return context.getResources().getColor(resId);
        //try to get as a hex string
        final String str = a.getString(index);
        if(null != str) return Color.parseColor(str);
        //return default
        return context.getResources().getColor(defaultColorRes);
    }

    private Paint backgroundPaint() {
        Paint p = new Paint();
        p.setColor(mTagBackground);
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        return p;
    }

    private Paint borderPaint(){
        Paint p = new Paint();
        p.setColor(mTagBorder);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        return p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int push = 2;
        final int width = getWidth()-push;
        final int height = getHeight();
        final int roundness = 0;

        canvas.drawRoundRect(new RectF(push, 0, width, height), roundness, roundness, mBackgroundPaint);
        canvas.drawRoundRect(new RectF(push, 0, width, height), roundness, roundness, mBorderPaint);
        super.onDraw(canvas);
    }

	@Override
	public void onClick(View v) {
		//TagListView parent = (TagListView) getParent();
		//parent.selectTag(_this);
		this.setSelected(!this.selected);
	}

	@Override
	public void onAnimationEnd(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {}
}
