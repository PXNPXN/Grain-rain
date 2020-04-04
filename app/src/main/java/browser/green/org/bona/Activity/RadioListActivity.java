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

import browser.green.org.bona.Adapter.SimpleAdapter;
import browser.green.org.bona.R;
import fm.qingting.qtsdk.QTException;
import fm.qingting.qtsdk.QTSDK;
import fm.qingting.qtsdk.callbacks.QTCallback;
import fm.qingting.qtsdk.entity.QTListEntity;
import fm.qingting.qtsdk.entity.Radio;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lee on 2018/1/23.
 */

public class RadioListActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    RecyclerView mRecyclerView;
    SimpleAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mTabLayout = findViewById(R.id.tab);
        mRecyclerView = findViewById(R.id.list);
        listAdapter = new SimpleAdapter<Radio>() {
            @Override
            public void bindData(SimpleHolder holder, final Radio object) {
                holder.mTextView.setText(object.getTitle());
                Glide.with(holder.itemView.getContext()).load(object.getThumbs().getMediumThumb()).into(holder.mImageView);
                holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RadioDetailsListActivity.class);
                        intent.putExtra(DetailsActivity.CHANNEL_ID, object.getId());
                        intent.putExtra("image",object.getThumbs().getMediumThumb());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(listAdapter);
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("电台");
        mTabLayout.addTab(tab);
        requestList();
    }

    private void requestList() {
        QTSDK.requestRadioList(1, new QTCallback<QTListEntity<Radio>>() {
            @Override
            public void done(QTListEntity<Radio> result, QTException e) {
                if (e == null) {
                    if (result != null) {
                        listAdapter.items = result.getData();
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage(), LENGTH_SHORT).show();
                }
            }

        });
    }
}
