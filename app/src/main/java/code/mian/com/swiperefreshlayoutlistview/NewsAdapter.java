package code.mian.com.swiperefreshlayoutlistview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by hhmsw on 2017/6/28.
 */

public class NewsAdapter extends BaseAdapter {

    private SparseArray<String> sparses ;
    private Context context;

    public NewsAdapter(Context c, SparseArray<String> news){
        this.context = c;
        this.sparses = news;
    }
    @Override
    public int getCount() {
        return sparses.size();
    }

    @Override
    public Object getItem(int i) {
        return sparses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        NewsViewHolder mNewsViewHolder = null;
        if(view == null){
            view  = LayoutInflater.from(context).inflate(R.layout.news_item_typeone,parent,false);
            mNewsViewHolder = new NewsViewHolder(view);
            view.setTag(mNewsViewHolder);
        }else{
            mNewsViewHolder =(NewsViewHolder) view.getTag();
        }
        mNewsViewHolder.TitleTv.setText(sparses.get(position));
        return view;
    }

    static class NewsViewHolder{
        TextView TitleTv;
        public NewsViewHolder(View itemView) {
            TitleTv = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
