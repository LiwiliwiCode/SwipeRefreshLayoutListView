package code.mian.com.swiperefreshlayoutlistview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by hhmsw on 2017/6/28.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener{
    //加载更多接口
    private OnLoadMoreListener mLoadMoreListener;
    //ListView
    private ListView listView;
    //加载更多FooterView
    private View mFooterView;
    //滑动最下面时上拉操作,系统提供的一个当手指在屏幕滑动时，按下抬起之间的差值，各个手机之间不同
    private int mTouchSlop ;
    //按下时Y轴坐标
    private int mDownY;
    //抬起时Y轴坐标
    private int mUpY;
    //上拉加载标记，是否在加载中
    private boolean isLoading = false;

    public MySwipeRefreshLayout(Context context) {
        super(context);

    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooterView = LayoutInflater.from(context).inflate(R.layout.news_loadmore,null,false);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public interface OnLoadMoreListener{
        void onloadMore();
    }

    public void setonLoadMoreRefreshListener(OnLoadMoreListener onLoadMoreListener){
        this.mLoadMoreListener = onLoadMoreListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(listView == null){
            getListView();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            if(canLoad()){
                LoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    private void getListView() {
        int childes = getChildCount();
        if(childes > 0){
            View view = getChildAt(0);
            if(view instanceof ListView){
                listView = (ListView) view;
                listView.setOnScrollListener(this);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mUpY = (int)ev.getRawY();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    /**
     * 加载更多
     */
    private void LoadMore(){
        if(mLoadMoreListener != null){
            setLoading(true);
            mLoadMoreListener.onloadMore();
        }
    }

    /**
     * 设置刷新状态
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if(isLoading){
            listView.addFooterView(mFooterView);
        }else{
            listView.removeFooterView(mFooterView);
            mUpY = 0;
            mDownY = 0;
        }
    }

    private boolean canLoad(){
        return isBottom() && !isLoading && isPullUp();
    }

    /**
     * 判断ListView是否已滑动到底部
     */
    private boolean isBottom(){
        if(listView != null && listView.getAdapter() != null){
            return listView.getLastVisiblePosition() == (listView.getAdapter().getCount() -1);
        }
        return false;
    }

    /**
     * 根据触摸ACTION_DOWN ACTION_UP 插值判断是否满足滑动条件
     */
    private boolean isPullUp(){
        return (mDownY - mUpY) >= mTouchSlop;
    }

}
