package browser.green.org.bona.ui.home;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import browser.green.org.bona.MainActivity;
import browser.green.org.bona.MyPagerAdapter;
import browser.green.org.bona.R;

public class HomeFragment extends Fragment {
    private Button btnSea;
    private MediaPlayer mMediaPlayer;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager=root.findViewById(R.id.vp_main);
        View view1= inflater.inflate(R.layout.sea,null);
        View view2= inflater.inflate(R.layout.rain,null);
        View view3= inflater.inflate(R.layout.meditation,null);
        View view4= inflater.inflate(R.layout.thunder,null);
        btnSea=(Button)root.findViewById(R.id.btnPause);

        ArrayList<View> viewContainter  = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewContainter .add(view1);
        viewContainter .add(view2);
        viewContainter .add(view3);
        viewContainter .add(view4);
        MyPagerAdapter adapter = new MyPagerAdapter();
        adapter.setViews(viewContainter);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,new GallyPageTransformet());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            //页面滑动后调用
            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMediaPlayer=MediaPlayer.create(getContext(),R.raw.rain);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();

        btnSea.setOnClickListener(new View.OnClickListener() {
            int flag=1;
            public void onClick(View v) {
                if(flag%2==0){
                    btnSea.setBackgroundResource(R.drawable.pause1);
                    mMediaPlayer.start();
                    flag++;

                }
                else{
                    btnSea.setBackgroundResource(R.drawable.pause);
                    mMediaPlayer.pause();
                    flag++;
                }
            }
        });

    }

    public class GallyPageTransformet implements ViewPager.PageTransformer {

        private static final  float min_scale=0.85f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            float scaleFactor=Math.max(min_scale,1-Math.abs(position));
            float rotate=20*Math.abs(position);
            if (position<-1){

            }else if (position<0){
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(rotate);
            }else if (position>=0&&position<1){
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(-rotate);
            }else if (position>=1){
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(-rotate);
            }

        }
    }
}
