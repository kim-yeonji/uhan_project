package base.medium.uhan.ui.pub;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import base.medium.uhan.R;
import java.util.HashMap;
import java.util.List;

public class PubAdapter extends RecyclerView.Adapter<PubAdapter.ViewHolder> {

    private List<HashMap<String, String>> mData;
    private FragmentActivity activity;
    private View.OnClickListener mListener;
    private View.OnClickListener sListener;

    public void setOnClick(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    public void setShareClick(View.OnClickListener sListener) {
        this.sListener = sListener;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
        TextView date;


        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.news_title);
            date = itemView.findViewById(R.id.pub_date);
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public PubAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setList(List<HashMap<String, String>> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_pub, parent, false);
        PubAdapter.ViewHolder vh = new PubAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PubAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).get("title");
        String date = mData.get(position).get("date");
        String key = mData.get(position).get("key");

        holder.title.setText(title);
        holder.itemView.setOnClickListener(mListener);
        holder.itemView.setTag(key);
        holder.date.setText(date);


    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }



}