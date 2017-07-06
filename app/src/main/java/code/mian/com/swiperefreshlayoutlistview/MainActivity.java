package code.mian.com.swiperefreshlayoutlistview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mListView;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private MessageHandler mMessageHandler = null;
    private SparseArray news;
    private NewsAdapter mNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageHandler = new MessageHandler(this);
        news = new SparseArray();
        for (int i = 0; i < 10; i++) {
            news.put(i, "唐僧爱喝酒 " + i);
        }

        mNewsAdapter = new NewsAdapter(this, news);
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.mSwiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        //设置向下拉多少出现刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMessageHandler.sendEmptyMessage(100);
            }
        });

        mSwipeRefreshLayout.setonLoadMoreRefreshListener(new MySwipeRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onloadMore() {

                mMessageHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int oldSize = news.size();
                        for (int i = oldSize - 1; i < oldSize + 5; i++) {
                            news.put(i, "姜子牙最爱，雕牌洗衣粉，随便拿随便选" + i);
                        }
                        mNewsAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setLoading(false);
                    }
                }, 2000);

            }
        });
    }

    private static class MessageHandler extends Handler {
        private static WeakReference<Activity> weakReference = null;

        public MessageHandler(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = (MainActivity) weakReference.get();
            if (activity == null) {
                return;
            }

            if (activity instanceof MainActivity) {
                if (activity.news.size() > 0) {
                    activity.news.clear();
                }
                //下拉只更新前5条
                for (int i = 0; i < 5; i++) {
                    activity.news.put(i, "姜子牙最爱" + i);
                }
                activity.mNewsAdapter.notifyDataSetChanged();
                activity.mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
