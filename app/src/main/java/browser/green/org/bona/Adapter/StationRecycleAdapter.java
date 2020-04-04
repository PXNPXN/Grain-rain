package browser.green.org.bona.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import browser.green.org.bona.Notes;
import browser.green.org.bona.R;
import browser.green.org.bona.Station;

public class StationRecycleAdapter extends RecyclerView.Adapter<StationRecycleAdapter.myViewHodler> {
    private Context mContext;
    private ArrayList<Station> mStationArrayList;


    public StationRecycleAdapter(Context context,ArrayList<Station> mStationArrayList){
        this.mContext=context;
        this.mStationArrayList=mStationArrayList;
    }

    public  StationRecycleAdapter.myViewHodler onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView=View.inflate(mContext, R.layout.recyler_station_item,null);
        return new myViewHodler(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHodler holder, final int position) {
        Station data=mStationArrayList.get(position);
       holder.mTextView.setText(data.getmText());
       holder.itemView.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
               Toast.makeText(mContext,"hello-"+position,Toast.LENGTH_LONG).show();
           }
       });
    }

    public int getposition(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        return mStationArrayList.size();
    }

    class myViewHodler extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public myViewHodler(View itemView) {
            super(itemView);
            mTextView=itemView.findViewById(R.id.tv_station_name);
        }


    }

    public  interface  OnItemClickListener{
        public  void OnItemClick(View view, Station data);
    }

    private NoteRecycleAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(NoteRecycleAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

}
