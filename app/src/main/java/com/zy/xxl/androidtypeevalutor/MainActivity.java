package com.zy.xxl.androidtypeevalutor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_close_btn)
    Button searchCloseBtn;
    @BindView(R.id.icon)
    Button mIcon;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.parent)
    RelativeLayout parent;
    @BindView(R.id.image)
    ImageView image;

    private float sacle = 0.2F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        web.loadUrl("https://www.baidu.com/");

        int originalLeft = web.getMeasuredWidth();
        int originalTop = web.getMeasuredHeight();
//        Log.e("tag", "originalWidth =" + originalLeft);
//        Log.e("tag", "originalHeight =" + originalTop);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    //添加图片
    @OnClick(R.id.icon)
    public void onAddViewClicked() {

    }

    //动画开始
    @OnClick(R.id.search_close_btn)
    public void onViewClicked() {
        ValueAnimator animator = getTransAnimator(web);
        ValueAnimator animator1 = getScaleAnimator(web);

        getAnimationSet(animator, animator1, web);

    }

    private void getAnimationSet(ValueAnimator trans, ValueAnimator scale, final View view) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(trans).after(scale);
//        set.play(scale);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                mIcon.setVisibility(View.VISIBLE);
//                int width = view.getMeasuredWidth();//图片宽度
//                int height = view.getMeasuredHeight();//图片高度
//                Log.e("tag", "ScaleWidth ===" + width);
//                Log.e("tag", "ScaleHeight ===" + height);

            }
        });
    }

    //缩小
    private ValueAnimator getScaleAnimator(final View view) {
        int width = view.getMeasuredWidth();//图片宽度
        int height = view.getMeasuredHeight();//图片高度
//        Log.e("tag", "ScaleWidth =" + width);
//        Log.e("tag", "ScaleHeight =" + height);

        float left = view.getLeft();
        float top = view.getTop();

//        Log.e("tag", "ScaleLeft =" + left);
//        Log.e("tag", "ScaleTop =" + top);
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zy", 1.0F, sacle)//
                .setDuration(500);//
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
        return anim;
    }


    /**
     * 上移
     *
     * @param view
     */

    public ValueAnimator getTransAnimator(final View view) {

        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();//屏幕宽度
        int height1 = wm1.getDefaultDisplay().getHeight();//屏幕高度

//        Log.e("tag", "windWidth =" + width1);
//        Log.e("tag", "winHeight =" + height1);


        int originalHeight = view.getMeasuredHeight();
//        Log.e("tag", "originalHeight =" + originalHeight);
        float width = view.getMeasuredWidth() ;//图片宽度 乘以缩放比例
        float height = view.getMeasuredHeight() ;//图片高度 乘以缩放比例
//        Log.e("tag", "TransWidth =" + width);
//        Log.e("tag", "TransHeight =" + height);

        //开始点 left = width1 - width / 2
        float left =view.getLeft();
        //开始点 top = height1 - originalHeight + (originalHeight - height) / 2
        float top = view.getTop();
//        Log.e("tag", "TransLeft =" + left);
//        Log.e("tag", "TransTop =" + top);
//


        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(500);
        valueAnimator.setObjectValues(new PointF(0, 0));
//        final PointF mStartValue = new PointF(left, top);
//        final PointF mEndValue = new PointF(width1 - width, 0);
        float dx = (width1 - sacle * width) / 2 - 30;
        float endX = left + dx;
        float dy = top + (height - sacle * height) / 2 ;
        float endY = 0 - dy ;
        final PointF mStartValue = new PointF(left, top);
        final PointF mEndValue = new PointF(endX, endY);
        Log.e("tag","开始点X == " + left);
        Log.e("tag","开始点Y == " + top);
        Log.e("tag","结束点X == " + endX);
        Log.e("tag","结束点Y == " + endY);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                startValue = mStartValue;
                endValue = mEndValue;
                //result = x0 + t * (v1 - v0)
                float x = startValue.x + (fraction * (endValue.x - startValue.x));
                float y = startValue.y + (fraction * (endValue.y - startValue.y));


                PointF point = new PointF();
                point.set(x, y);
                return point;
            }
        });


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);

//                Log.e("tag", "point.x ===" + point.x);
//                Log.e("tag", "point.y ===" + point.y);

            }

        });

        return valueAnimator;
    }

}
