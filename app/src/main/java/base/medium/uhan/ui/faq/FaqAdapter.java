package base.medium.uhan.ui.faq;

import android.content.Context;
import android.text.Html;
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

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    private List<HashMap<String, String>> mData;
    private FragmentActivity activity;
    private View.OnClickListener mListener;

    public void setOnClick(View.OnClickListener mListener) {
        this.mListener = mListener;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
        TextView content;



        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.faq_content);

            itemView.findViewById(R.id.pub_date).setVisibility(View.GONE);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {

                    if (content.getVisibility() == View.GONE) {
                        content.setVisibility(View.VISIBLE);
                        //notifyItemChanged(pos);
                    } else if(content.getVisibility() == View.VISIBLE) {
                        content.setVisibility(View.GONE);
                        //notifyItemChanged(pos);
                    }


                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FaqAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setList(List<HashMap<String, String>> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_pub, parent, false);
        FaqAdapter.ViewHolder vh = new FaqAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FaqAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).get("title");
        String content = mData.get(position).get("content");

        holder.title.setText(title);
        holder.content.setText(Html.fromHtml(content));



    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }



}