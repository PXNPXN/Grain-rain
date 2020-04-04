package browser.green.org.bona.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import browser.green.org.bona.R;
import browser.green.org.bona.ui.home.HomeFragment;

public class MyAdapter extends BaseAdapter {

    List<Map<String, Object>> list;
    LayoutInflater inflater;
    OnItemClickListener onItemClickListener;

    public MyAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener=listener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view=null;
        if(convertView == null) {
            view =inflater.inflate(R.layout.item,null);
            holder = new ViewHolder();

            holder.ib_first = view.findViewById(R.id.ib_first);
            holder.cb_first = view.findViewById(R.id.cb_first);
            holder.tv_title_first = view.findViewById(R.id.tv_title_first);

            holder.ib_second = view.findViewById(R.id.ib_second);
            holder.cb_second = view.findViewById(R.id.cb_second);
            holder.tv_title_second = view.findViewById(R.id.tv_title_second);

            holder.ib_third = view.findViewById(R.id.ib_third);
            holder.cb_third = view.findViewById(R.id.cb_third);
            holder.tv_title_third = view.findViewById(R.id.tv_title_third);
            view.setTag(holder);
        } else {
            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        Map map = list.get(position);

        holder.ib_first.setImageResource((Integer) map.get("iv_first"));
        holder.ib_first.setContentDescription((String) map.get("description_first"));
        holder.cb_first.setChecked((Boolean) map.get("state_first"));
        holder.tv_title_first.setText((String)map.get("tv_title_first"));

        holder.ib_second.setImageResource((Integer) map.get("iv_second"));
        holder.ib_second.setContentDescription((String)map.get("description_second"));
        holder.cb_second.setChecked((Boolean) map.get("state_second"));
        holder.tv_title_second.setText((String)map.get("tv_title_second"));

        holder.ib_third.setImageResource((Integer) map.get("iv_third"));
        holder.ib_third.setContentDescription((String)map.get("description_third"));
        holder.cb_third.setChecked((Boolean) map.get("state_third"));
        holder.tv_title_third.setText((String)map.get("tv_title_third"));


        final ViewHolder finalHolder = holder;
        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(v== finalHolder.ib_first){
                    if (finalHolder.cb_first.isChecked()){
                        onItemClickListener.onItemFirstDeleteClicked(position);
                        finalHolder.cb_first.setChecked(false);
                        finalHolder.cb_first.setVisibility(View.VISIBLE);
                    }else {
                        try {
                            onItemClickListener.onItemFirstDownloadClicked( position);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        finalHolder.cb_first.setVisibility(View.VISIBLE);
                        finalHolder.cb_first.setChecked(true);
                    }
                }
                if(v== finalHolder.ib_second){
                    if(finalHolder.cb_second.isChecked()){
                        onItemClickListener.onItemSecondDeleteClicked(position);
                        finalHolder.cb_second.setVisibility(View.VISIBLE);
                        finalHolder.cb_second.setChecked(false);
                    }else {
                        try {
                            onItemClickListener.onItemSecondDownloadClicked(position);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finalHolder.cb_second.setVisibility(View.VISIBLE);
                        finalHolder.cb_second.setChecked(true);
                    }
                }
                if(v== finalHolder.ib_third)
                    if(finalHolder.cb_third.isChecked()){
                        onItemClickListener.onItemThirdDeleteClicked(position);
                        finalHolder.cb_third.setVisibility(View.VISIBLE);
                        finalHolder.cb_third.setChecked(false);
                    }else {
                        try {
                            onItemClickListener.onItemThirdDownloadClicked(position);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finalHolder.cb_third.setVisibility(View.VISIBLE);
                        finalHolder.cb_third.setChecked(true);
                    }

            }
        };

        holder.ib_first.setOnClickListener(listener);
        holder.ib_second.setOnClickListener(listener);
        holder.ib_third.setOnClickListener(listener);



        return view;
    }

    public static class ViewHolder {
        ImageButton ib_first,ib_second,ib_third;
        CheckBox cb_first,cb_second,cb_third;
        TextView tv_title_first,tv_title_second,tv_title_third;
    }

    public interface OnItemClickListener{
        void onItemFirstDownloadClicked(int position) throws ExecutionException, InterruptedException;
        void onItemFirstDeleteClicked(int position);
        void onItemSecondDownloadClicked(int position) throws ExecutionException, InterruptedException;
        void onItemSecondDeleteClicked(int position);
        void onItemThirdDownloadClicked(int position) throws ExecutionException, InterruptedException;
        void onItemThirdDeleteClicked(int position);
    }
}
