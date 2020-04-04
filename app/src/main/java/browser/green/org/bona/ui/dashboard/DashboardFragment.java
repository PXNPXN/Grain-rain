package browser.green.org.bona.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import browser.green.org.bona.Activity.ListActivity;
import browser.green.org.bona.Activity.RadioListActivity;
import browser.green.org.bona.Adapter.NoteRecycleAdapter;
import browser.green.org.bona.Adapter.StationRecycleAdapter;
import browser.green.org.bona.Notes;
import browser.green.org.bona.R;
import browser.green.org.bona.Station;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private ArrayList<Station> StationList= new ArrayList<>();
    private StationRecycleAdapter mStationRecycleAdapter;
    public RecyclerView mRecyclerView;
    private View view;
    private ImageView WebStation,Station;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();
        return view;
    }

    private void init() {
        WebStation=view.findViewById(R.id.iv_webStation);
        Station=view.findViewById(R.id.iv_station);
        WebStation.setOnClickListener(this);
        Station.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_webStation:
                startActivity(new Intent(context,ListActivity.class));
                break;
            case R.id.iv_station:
                startActivity(new Intent(context,RadioListActivity.class));
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

//    private void init(){
//      ArrayList <Station> List=new ArrayList<Station>();
//      for(int i=0;i<10;i++){
//          Station station=new Station();
//          station.setText("hello"+i);
//          List.add(station);
//      }
//      StationList=List;
//      initRecyclerView();
//    }
//
//    private void initRecyclerView() {
//        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_station);
//        //create adapter
//        mStationRecycleAdapter=new StationRecycleAdapter(getActivity(),StationList);
//
//        mRecyclerView.setAdapter(mStationRecycleAdapter);
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//
//
//    }

}
