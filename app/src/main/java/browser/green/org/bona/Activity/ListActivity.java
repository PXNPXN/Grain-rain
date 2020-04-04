package browser.green.org.bona.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import browser.green.org.bona.Adapter.SimpleAdapter;
import browser.green.org.bona.R;
import fm.qingting.qtsdk.QTException;
import fm.qingting.qtsdk.QTSDK;
import fm.qingting.qtsdk.callbacks.QTCallback;
import fm.qingting.qtsdk.entity.Category;
import fm.qingting.qtsdk.entity.Channel;
import fm.qingting.qtsdk.entity.QTListEntity;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lee on 2018/1/23.
 */

public class ListActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    RecyclerView mRecyclerView;
    SimpleAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mTabLayout = findViewById(R.id.tab);
        mRecyclerView = findViewById(R.id.list);
        listAdapter  = new SimpleAdapter<Channel>() {
            @Override
            public void bindData(SimpleHolder holder, final Channel object) {
                holder.mTextView.setText(object.getTitle());
                Glide.with(holder.itemView.getContext()).load(object.getThumbs().getMediumThumb()).into(holder.mImageView);
                holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra(DetailsActivity.CHANNEL_ID, object.getId());
                        intent.putExtra("image",object.getThumbs().getMediumThumb());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(listAdapter);
        requestTags();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Category category = (Category) tab.getTag();
                if (category != null) {
                    requestList(category.getId());
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
        QTSDK.requestChannelOnDemandCategories(new QTCallback<List<Category>>() {
            @Override
            public void done(List<Category> result, QTException e) {
                if (e == null) {
                    if (result != null && result.size() > 0) {
                        for (Category category : result) {
                            TabLayout.Tab tab = mTabLayout.newTab();
                            tab.setText(category.getName());
                            tab.setTag(category);
                            mTabLayout.addTab(tab);
                        }
                        requestList(result.get(0).getId());
                    }
                }else{
                    Toast.makeText(getBaseContext(), e.getMessage(), LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestList(int tabId) {
        QTSDK.requestChannelOnDemandList(tabId,null,1,new QTCallback<QTListEntity<Channel>>() {
            @Override
            public void done(QTListEntity<Channel> result, QTException e) {
                if (e == null) {
                    if (result != null) {
                        listAdapter.items = result.getData();
                        listAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getBaseContext(), e.getMessage(), LENGTH_SHORT).show();
                }
            }

        });
    }
}
