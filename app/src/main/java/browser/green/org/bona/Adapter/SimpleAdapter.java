package browser.green.org.bona.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import browser.green.org.bona.R;


/**
 * Created by lee on 2018/1/30.
 */

public abstract class SimpleAdapter<T> extends RecyclerView.Adapter {

   public List<T> items;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        SimpleHolder holder = new SimpleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SimpleHolder) holder).bind(items.get(position));
    }

    public abstract void bindData(SimpleHolder holder, T object);

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class SimpleHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public LinearLayout mLinearLayout;
        public TextView mTextView;

        public SimpleHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cover);
            mTextView = itemView.findViewById(R.id.title);
            mLinearLayout = itemView.findViewById(R.id.container);
        }

        public void bind(T object) {
            bindData(this, object);
        }
    }
}