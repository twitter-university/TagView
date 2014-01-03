/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.twitter.university.android.tagview.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.LevelListDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;

import com.twitter.university.android.tagview.R;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TagView extends View {
    private static final String TAG = "TAGVIEW";

    private static final int MARGIN = 200;
    private static final int PAD_H = 100;
    private static final int PAD_V = 30;
    private static final int TEXT_SIZE = 64;
    private static final int TEXT_COLOR = Color.BLUE;
    private static final int TAG_BG = R.drawable.tag;

    private static class Tag {
        private final String tag;
        private final int level;
        String shortTag;
        Tag(String tag, int level) {
            this.tag = tag;
            this.level = level;
        }
    }


    private final TextPaint textPaint;
    private final float textHeight;
    private final float textBaseline;

    private final Rect tagRect = new Rect();
    private final RectF tagRectF = new RectF();
    private final PointF tagBorderTL = new PointF();
    private final PointF tagTL = new PointF();
    private final LevelListDrawable background;

    private Tag tag;

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        background = (LevelListDrawable) getResources().getDrawable(TAG_BG);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(TEXT_COLOR);

        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        textBaseline = metrics.leading - metrics.ascent;
        textHeight = metrics.descent + textBaseline;
    }

    /**
     * @param context
     * @param attrs
     */
    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     */
    public TagView(Context context) {
        this(context, null);
    }

    /**
     * @param tag
     * @param level
     */
    public void setTag(String tag, int level) {
        this.tag = new Tag(tag, level);
        invalidate();
        requestLayout();
    }

    /**
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        String tagText = (null == tag) ? "" : tag.tag;

        int h = View.getDefaultSize(
            (int) (getPaddingLeft() + getPaddingRight()
                + (2 * (MARGIN + PAD_H)) + textPaint.measureText(tagText)),
            wSpec);

        int v = View.getDefaultSize(
            (int) (getPaddingTop() + getPaddingBottom()
                + (2 * (MARGIN + PAD_V)) + textHeight),
            wSpec);

        setMeasuredDimension((int) h, (int) v);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if ((!changed) || (null == tag)) { return; }

        float wMax = right - (left + getPaddingLeft() + getPaddingRight() + (2 * (PAD_H + MARGIN)));
        tag.shortTag = TextUtils.ellipsize(tag.tag, textPaint, wMax, TruncateAt.END).toString();
    }

    /**
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null == tag) { return; }

        tagBorderTL.set(getPaddingLeft() + MARGIN, getPaddingTop() + MARGIN);

        float h = Math.min(
                (2 * PAD_V) + textHeight,
                getHeight() - (tagBorderTL.y + (getPaddingBottom() + MARGIN)));

        float w = (2 * PAD_H) + textPaint.measureText(tag.shortTag);

        tagRectF.set(tagBorderTL.x, tagBorderTL.y, tagBorderTL.x + w, tagBorderTL.y + h);

        tagRectF.round(tagRect);
        background.setBounds(tagRect);
        background.setLevel(tag.level);
        background.draw(canvas);

        tagRectF.inset(PAD_H, PAD_V);
        canvas.clipRect(tagRectF);

        canvas.save();
        canvas.rotate(
                180.0F,
                tagRectF.left + ((tagRectF.right - tagRectF.left) / 2),
                tagRectF.top + ((tagRectF.bottom - tagRectF.top) / 2));

        textPaint.setShadowLayer(6.0F, 15.0F, 17.0F, Color.GREEN);

        canvas.drawText(
                tag.shortTag,
                (int) tagBorderTL.x + PAD_H,
                (int) tagBorderTL.y + PAD_V + textBaseline,
                textPaint);

        canvas.restore();
    }
}
