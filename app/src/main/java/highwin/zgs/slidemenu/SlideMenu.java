package highwin.zgs.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * User: zgsHighwin
 * Email: 799174081@qq.com  or 799174081@gmail.com
 * Description: A Thought Of SlideMenu
 * Create-Time: 2016/8/22 11:23
 */
public class SlideMenu extends RelativeLayout {


    private View mLeftView;
    private View mMainView;
    private int mLeftViewWidth;
    private int mMainViewWidth;
    private Scroller mScroller;

    public SlideMenu(Context context) {
        super(context);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalStateException("你必须要有两个以上的孩子｜SlideMenu must has two child view at least");
        }

        mLeftView = getChildAt(0);
        mMainView = getChildAt(1);
        mLeftViewWidth = mLeftView.getLayoutParams().width;
        mMainViewWidth = mMainView.getLayoutParams().width;
        mScroller = new Scroller(getContext());
    }

    //继承自FrameLayout就可以省下onMeasure的工作
    //inherited from FraeLayout can save onMeasure by yourself
/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLeftView.measure(mLeftViewWidth, heightMeasureSpec);
        mMainView.measure(widthMeasureSpec, heightMeasureSpec);
    }*/

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftView.layout(-mLeftViewWidth, 0, 0, b);
        mMainView.layout(0, 0, r, b);
    }
    private float intDownX;
    private float mDonwX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDonwX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDonwX) > mLeftViewWidth / 4) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("SlideMenu", "onTouchEvent...ACTION_DOWN:");
                mDonwX = event.getX();
            case MotionEvent.ACTION_MOVE:
                Log.d("SlideMenu", "onTouchEvent...ACTION_MOVE:");
                float moveX = event.getX();
                float deltaX = moveX - mDonwX;
                float realX = getScrollX() - deltaX;
                realX = realX > 0 ? 0 : realX < -mLeftViewWidth ? -mLeftViewWidth : realX;
                scrollTo((int) (realX), 0);

                Log.d("SlideMenu", "deltaX:" + deltaX + "...getScrollX" + getScrollX() + "---moveX" + moveX + " donwX" + mDonwX + "     realX" + realX);
                mDonwX = moveX;
            case MotionEvent.ACTION_UP:
                Log.d("SlideMenu", "onTouchEvent...ACTION_UP:");

                //user ScrollAnimation
               /*  ScrollAnimation scrollAnimation;
                if (getScrollX() > -mLeftViewWidth / 2) {
                    //打开slideMenu||open menu
                    scrollAnimation = new ScrollAnimation(this, 0, 300, TimeUnit.MILLISECONDS);
                } else {
                    //关闭slideMenu||close menu
                    scrollAnimation = new ScrollAnimation(this, -mLeftViewWidth, 300, TimeUnit.MILLISECONDS);
                }
                startAnimation(scrollAnimation);*/
                if (getScrollX() > -mLeftViewWidth / 2) {
                    //打开slideMenu||open menu
                    mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 300);
                } else {
                    //关闭slideMenu||close menu
                    mScroller.startScroll(getScrollX(), 0, -mLeftViewWidth - getScrollX(), 0, 300);
                }
                invalidate();
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {  //true表示动画没有结，还要继续刷新||mScroller.computeScrollOffset()->true, the animationis not yet finished
                int currX = mScroller.getCurrX();
                scrollTo(currX, 0);
                invalidate();
            }
        }
    }

}
