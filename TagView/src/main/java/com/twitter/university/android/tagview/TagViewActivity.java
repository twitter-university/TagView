
package com.twitter.university.android.tagview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.twitter.university.android.tagview.widget.TagView;

import org.xmlpull.v1.XmlPullParser;


public class TagViewActivity extends Activity {

    private static class TagAnimation implements View.OnClickListener, Animation.AnimationListener {
        private final Context context;
        private final Animation animIn;
        private final Animation animOut;
        private volatile View animatedView;

        TagAnimation(Context context) {
            this.context = context;
            this.animOut = AnimationUtils.loadAnimation(context, R.anim.tag_animate_out);
            this.animIn = AnimationUtils.loadAnimation(context, R.anim.tag_animate_in);
        }

        @Override
        public void onClick(View view) {
            animatedView = view;
            animOut.setAnimationListener(this);
            animatedView.startAnimation(animOut);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (null == animatedView) { return; }
            animatedView.startAnimation(animIn);
            animatedView = null;
        }

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view);

        TagView tags = ((TagView) findViewById(R.id.tags));

        tags.setOnClickListener(new TagAnimation(this));

        tags.setTag("One", 0);
        tags.setTag("Thirteen Twenty-three Thirty-three Forty-three Fifty-three Sixty-three Seventy-three Eighty-three Ninty-three One oh three", 1);
//        tags.addTag("Two", 0);
//        tags.addTag("Three", 0);
//        tags.addTag("Four", 0);
//        tags.addTag("Five", 0);
//        tags.addTag("Six", 1);
//        tags.addTag("Seven", 0);
//        tags.addTag("Eight", 0);
//        tags.addTag("Nine", 1);
//        tags.addTag("Ten", 0);
//        tags.addTag("Eleven", 0);
//        tags.addTag("Twelve", 0);
//        tags.addTag("Fourteen", 0);
//        tags.addTag("Fifteen", 0);
//        tags.addTag("Sixteen", 0);
    }
}
