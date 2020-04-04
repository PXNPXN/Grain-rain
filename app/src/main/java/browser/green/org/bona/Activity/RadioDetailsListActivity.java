package browser.green.org.bona.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import browser.green.org.bona.Adapter.SimpleAdapter;
import browser.green.org.bona.R;
import fm.qingting.qtsdk.QTException;
import fm.qingting.qtsdk.QTSDK;
import fm.qingting.qtsdk.callbacks.QTCallback;
import fm.qingting.qtsdk.entity.RadioProgram;
import fm.qingting.qtsdk.entity.RadioProgramList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lee on 2018/1/23.
 */

public class RadioDetailsListActivity extends AppCompatActivity {
    public final static String CHANNEL_ID = "channel_id";
    int channelId;
    String image;
    RadioProgramList list;
    TabLayout mTabLayout;
    RecyclerView mRecyclerView;
    SimpleAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        channelId = getIntent().getIntExtra(CHANNEL_ID, 0);
        image =getIntent().getStringExtra("image");
        if (channelId == 0) {
            return;
        }
        mTabLayout = findViewById(R.id.tab);
        mRecyclerView = findViewById(R.id.list);
        listAdapter = new SimpleAdapter<RadioProgram>() {
            @Override
            public void bindData(SimpleHolder holder, final RadioProgram object) {
                holder.mTextView.setText(object.getTitle());
                holder.mLinearLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PlayerActivity.Companion.start(RadioDetailsListActivity.this, channelId, null,image);
                            }
                        }
                );
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(listAdapter);
        requestList();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = (Integer) tab.getTag();
                if (index != 0) {
                    switch (index) {
                        case 1:
                            listAdapter.items = list.getMondayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 2:
                            listAdapter.items = list.getTuesdayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 3:
                            listAdapter.items = list.getWednesdayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 4:
                            listAdapter.items = list.getThursdayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 5:
                            listAdapter.items = list.getFridayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 6:
                            listAdapter.items = list.getSaturdayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                        case 7:
                            listAdapter.items = list.getSundayList();
                            listAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void requestTags() {

        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("周一");
        tab.setTag(1);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周二");
        tab.setTag(2);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周三");
        tab.setTag(3);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周四");
        tab.setTag(4);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周五");
        tab.setTag(5);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周六");
        tab.setTag(6);
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("周日");
        tab.setTag(7);
        mTabLayout.addTab(tab);
    }

    private void requestList() {
        QTSDK.requestRadioProgramList(channelId, new QTCallback<RadioProgramList>() {
            @Override
            public void done(RadioProgramList result, QTException e) {
                if (e == null) {
                    if (result != null) {
                        list = result;
                        requestTags();
                    }
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage(), LENGTH_SHORT).show();
                }
            }

        });
    }
}
