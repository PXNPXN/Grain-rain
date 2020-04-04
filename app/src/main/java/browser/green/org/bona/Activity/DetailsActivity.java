package browser.green.org.bona.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import browser.green.org.bona.Adapter.SimpleAdapter;
import browser.green.org.bona.R;
import fm.qingting.qtsdk.QTException;
import fm.qingting.qtsdk.QTSDK;
import fm.qingting.qtsdk.callbacks.QTCallback;
import fm.qingting.qtsdk.entity.Channel;
import fm.qingting.qtsdk.entity.ChannelProgram;
import fm.qingting.qtsdk.entity.QTListEntity;

/**
 * Created by lee on 2018/1/23.
 */

public class DetailsActivity extends AppCompatActivity {

    public final static String CHANNEL_ID = "channel_id";
    int channelId;
    String image;
    TextView title;
    TextView url;
    ImageView imageView;
    RecyclerView recyclerView;
    SimpleAdapter<ChannelProgram> listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        channelId = getIntent().getIntExtra(CHANNEL_ID, 0);
        image =getIntent().getStringExtra("image");
        if (channelId == 0) {
            return;
        }
        title = findViewById(R.id.title);
        url = findViewById(R.id.url);
        imageView = findViewById(R.id.cover);
        recyclerView = findViewById(R.id.list);
        listAdapter = new SimpleAdapter<ChannelProgram>() {
            @Override
            public void bindData(final SimpleHolder holder, final ChannelProgram object) {
                holder.mTextView.setText(object.getTitle());
                if (object.getThumbs() != null) {
                    Glide.with(holder.itemView.getContext()).load(object.getThumbs().getMediumThumb()).into(holder.mImageView);
                }
                holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ArrayList<Integer> programIds = new ArrayList();
                                                                for (ChannelProgram channelProgram : listAdapter.items) {
                                                                    programIds.add(channelProgram.getId());
                                                                }
                                                                // 根据holder在recyclerview的位置获取index，实际项目中不应该这么取，可能会受header和footer干扰
                                                                int currentIndex = holder.getAdapterPosition();
                                                                PlayerActivity.Companion.start(DetailsActivity.this, channelId, programIds, image, currentIndex);
                                                            }
                                                        }

                );
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(listAdapter);
        requestChannelDetails(channelId);
        requestChannelPrograms(channelId);
    }


    private void requestChannelDetails(int channelId) {
        QTSDK.requestChannelOnDemand(channelId, new QTCallback<Channel>() {
            @Override
            public void done(Channel result, QTException e) {
                if (e == null) {
                    title.setText(result.getTitle());
                    Glide.with(getBaseContext())
                            .load(result.getThumbs().getMediumThumb())
                            .into(imageView);
                }
            }
        });
    }

    private void requestChannelPrograms(int channelId) {
        QTSDK.requestChannelOnDemandProgramList(channelId, null,1,"",new QTCallback<QTListEntity<ChannelProgram>>() {
            @Override
            public void done(QTListEntity<ChannelProgram> result, QTException e) {
                if (e == null) {
                    listAdapter.items = result.getData();
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
