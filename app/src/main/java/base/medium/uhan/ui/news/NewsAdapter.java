package base.medium.uhan.ui.news;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import base.medium.uhan.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mData = new ArrayList<News>();
    private FragmentActivity activity;
    private View.OnClickListener mListener;
    private View.OnClickListener shareListener;
    private View.OnClickListener scrapListener;

    public void setOnClick(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    public void setOnShare(View.OnClickListener shareListener) {
        this.shareListener = shareListener;
    }

    public void setOnScrap(View.OnClickListener scrapListener) {
        this.scrapListener = scrapListener;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
        TextView content;
        TextView link;
        TextView date;
        ImageView thumnail;
        Button share;
        Button scrap;


        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_content);
            link = itemView.findViewById(R.id.news_link);
            date = itemView.findViewById(R.id.news_date);
            thumnail = itemView.findViewById(R.id.news_icon);
            share = itemView.findViewById(R.id.button_share);

            Log.d("ddd", "ViewHolder");
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public NewsAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setList(List<News> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item, parent, false);
        NewsAdapter.ViewHolder vh = new NewsAdapter.ViewHolder(view);

        Log.d("ddd", "onCreateViewHolder");

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        String content = mData.get(position).getContent();
        String date = mData.get(position).getDate();
        String link = mData.get(position).getLink();
        String imgUrl = mData.get(position).getImgUrl();

        holder.title.setText(title);
        holder.content.setText(content);
        holder.link.setText(link);

        if (imgUrl == "") {
            holder.thumnail.setVisibility(View.GONE);
        } else {
            Glide.with(activity).load(imgUrl).into(holder.thumnail);
        }


        holder.link.setTag(imgUrl);
        holder.date.setText(date);
        holder.itemView.setOnClickListener(mListener);
        holder.share.setTag(link);
        holder.share.setOnClickListener(shareListener);

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }



}