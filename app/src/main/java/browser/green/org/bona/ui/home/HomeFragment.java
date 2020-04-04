package browser.green.org.bona.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import browser.green.org.bona.Adapter.MyAdapter;
import browser.green.org.bona.Adapter.MyPagerAdapter;
import browser.green.org.bona.Constants;
import browser.green.org.bona.Manager.DownloadTask;
import browser.green.org.bona.Service.MyService;
import browser.green.org.bona.R;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;


public class HomeFragment extends Fragment {
    public ImageButton ib_add,ib_pause;
    public TextView textView;
    private MyService.MyBinder myBinder;
    public ArrayList<View> viewContainer;
    public ViewPager viewPager;
    public View view;
    public MyPagerAdapter adapter;
    public static int i=0;
    private static final int REQUEST_PERMISSION = 0;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder= (MyService.MyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView=root.findViewById(R.id.tv_greet);
        setGreet();
        adapter = new MyPagerAdapter();
        viewContainer  = new ArrayList<View>();// 将要分页显示的View装入数组中


        ib_pause=root.findViewById(R.id.ib_pauseAndReplay);
        ib_add=root.findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dialogueBox();
            }
        });
        ib_pause.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(myBinder.is_playing()){
                    ib_pause.setBackgroundResource(R.drawable.pause);
                    myBinder.pause_music();
                }
                else{
                    if(i==0){
                        myBinder.play_music(R.raw.sea);
                        ib_pause.setBackgroundResource(R.drawable.replay);
                        myBinder.set_loop(true);
                    }
                    else{
                        ib_pause.setBackgroundResource(R.drawable.replay);
                        myBinder.start();
                    }
                    i++;
                }
            }
        });
        //绑定服务
        Intent intent = new Intent(getContext(),MyService.class);
        Objects.requireNonNull(getActivity()).getApplicationContext().bindService(intent,conn, Context.BIND_AUTO_CREATE);

        viewPager=root.findViewById(R.id.vp_main);
        View view1= inflater.inflate(R.layout.sea,null);
        View view2= inflater.inflate(R.layout.rain,null);
        View view3= inflater.inflate(R.layout.meditation,null);
        View view4= inflater.inflate(R.layout.thunder,null);

        viewContainer .add(view1);
        viewContainer .add(view2);
        viewContainer .add(view3);
        viewContainer .add(view4);

        adapter.setViews(viewContainer);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(true, new GallyPageTransformer());
        //设置viewPager页面滑动的事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面状态改变时调用
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
            //页面滑动过程中调用
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            //页面滑动后调用
            @Override
            public void onPageSelected(int arg0) {
                ib_pause.setBackgroundResource(R.drawable.replay);
                switch (arg0){
                    case 0:
                        myBinder.reset_music();
                        myBinder.play_music(R.raw.sea);
                        myBinder.set_loop(true);
                        break;
                    case 1:
                        myBinder.reset_music();
                        myBinder.play_music(R.raw.rain);
                        myBinder.set_loop(true);
                        break;
                    case 2:
                        myBinder.reset_music();
                        myBinder.play_music(R.raw.meditation);
                        myBinder.set_loop(true);
                        break;
                    case 3:
                        myBinder.reset_music();
                        myBinder.play_music(R.raw.thunder);
                        myBinder.set_loop(true);
                        System.out.println("aaaaaaaa page is :"+arg0);
                        break;
                    case 4:
                        myBinder.reset_music();
                        myBinder.playmusic("/storage/emulated/0/Music/song-0.mp3");
                        myBinder.set_loop(true);
                        break;
                    case 5:
                        myBinder.reset_music();
                        myBinder.playmusic("/storage/emulated/0/Music/song-1.mp3");
                        myBinder.set_loop(true);
                        break;
                    case 6:
                        myBinder.reset_music();
                        myBinder.playmusic("/storage/emulated/0/Music/song-2.mp3");
                        myBinder.set_loop(true);
                        break;

                }
            }

        });
        return root;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public List getMusic(){
        String [] e={".mp3"};
        File file=new File(Environment.getExternalStorageDirectory()+"/Music");
        List list=searchMp3Infos(file,e);
        return list;
    }

    //扫描实现
    public static List<String> searchMp3Infos(File file, String[] ext) {
        List<String> list = new ArrayList<>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        searchMp3Infos(listFile[i], ext);
                    }
                }
            } else {
                String filename = file.getAbsolutePath();
                for (int i = 0; i < ext.length; i++) {
                    if (filename.endsWith(ext[i])) {
                        list.add(filename);
                        break;
                    }
                }
            }
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dialogueBox(){

        LayoutInflater inflater=getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog,null);
        ListView listView=view.findViewById(R.id.listView);
        final MyAdapter adapter = new MyAdapter(getContext());

        final List<Map<String, Object>> list = new ArrayList<>();
        List<List> listInfo =getState();
        Map<String, Object> map = new HashMap<>();
        Boolean state_first=Boolean.parseBoolean(listInfo.get(0).get(0).toString());
        Boolean state_second=Boolean.parseBoolean(listInfo.get(1).get(0).toString());
        Boolean state_third=Boolean.parseBoolean(listInfo.get(2).get(0).toString());
        map.put("iv_first",R.drawable.test);
        map.put("description_first",listInfo.get(0).get(1));
        map.put("state_first",state_first);
        map.put("tv_title_first", Constants.voiceType[0]);

        map.put("iv_second",R.drawable.night);
        map.put("description_second",listInfo.get(1).get(1));
        map.put("state_second",state_second);
        map.put("tv_title_second",Constants.voiceType[1]);

        map.put("iv_third",R.drawable.lake);
        map.put("description_third",listInfo.get(2).get(1));
        map.put("state_third",state_third);
        map.put("tv_title_third",Constants.voiceType[2]);

        list.add(map);

        adapter.setList(list);
        final List<Map<String, Object>> mapList=list;
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {

            /*
             * onItemFirstDeleteClicked 删除
             * onItemSecondDownloadClicked 下载
             */
            @Override
            public void onItemFirstDownloadClicked(int position) throws ExecutionException, InterruptedException {
                final String s=mapList.get(position).get("description_first").toString();
                String [] slist={s};
                DownloadTask downloadTask=new DownloadTask();
                downloadTask.execute(slist);
                System.out.println("item 1 downloaded");
                list.get(position).put("description_first","/storage/emulated/0/Music/song-"+position+".mp3");
                addPage(R.layout.new_layout,R.drawable.test);

            }

            @Override
            public void onItemFirstDeleteClicked(int position) {
                String s=mapList.get(position).get("description_first").toString();
                System.out.println("deletedeletedeletedelete111:"+s);
                delete(s);

            }

            @Override
            public void onItemSecondDownloadClicked(int position) throws ExecutionException, InterruptedException {
                final String s=mapList.get(position).get("description_second").toString();
                String [] slist={s};
                DownloadTask downloadTask=new DownloadTask();
                downloadTask.execute(slist);
                list.get(position).put("description_second","/storage/emulated/0/Music/song-"+position+".mp3");
                System.out.println("item 2 downloaded");
                addPage(R.layout.new1_layout,R.drawable.night);
            }

            @Override
            public void onItemSecondDeleteClicked(int position) {
                String s=mapList.get(position).get("description_second").toString();
                System.out.println("deletedeletedeletedelete222:"+s);
                delete(s);
            }

            @Override
            public void onItemThirdDownloadClicked(int position) throws ExecutionException, InterruptedException {
                final String s=mapList.get(position).get("description_third").toString();
                String [] slist={s};
                DownloadTask downloadTask=new DownloadTask();
                downloadTask.execute(slist);
                long t=downloadTask.get();
                list.get(position).put("description_third","/storage/emulated/0/Music/song-"+position+".mp3");
                System.out.println("item 3 downloaded");
                addPage(R.layout.new2_layout,R.drawable.lake);
            }

            @Override
            public void onItemThirdDeleteClicked(int position) {
                String s=mapList.get(position).get("description_third").toString();
                System.out.println("deletedeletedeletedelete333:"+s);
                delete(s);
            }
        });
        listView.setAdapter(adapter);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(view);
        dialog.show();
    }

    public void addPage(int layout,int id){
        view =getLayoutInflater().inflate(layout,null);
        view.setBackgroundResource(id);
        viewContainer.add(view);
        adapter.setViews(viewContainer);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);

    }

    public void delPage(){
        int position=viewPager.getCurrentItem();
        viewContainer.remove(position);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<List> getState(){
        List<List> listInfo = new ArrayList<>();
        List list=getMusic(); //只有filename
        for(i=0;i<3;i++){
            List<String> stringList = new ArrayList<>();
            stringList.add(Constants.state[i]);
            stringList.add(Constants.url[i]);
            listInfo.add(stringList);
        }
        if (list.size()==0)
            return  listInfo;
        for(int i=0;i<list.size();i++){
            String temp=list.get(i).toString();
            temp=temp.substring(temp.length()-3);
            String [] name=temp.split("-");
            String path="/storage/emulated/0/Music/"+temp;
            int id=Integer.parseInt(name[1]);
            listInfo.get(id).set(0,"true");
            listInfo.get(id).set(1,path);
        }

        return listInfo;

    }

    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            Log.e("fail","file:" + delFile + "not exit！");
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return false;
        }
    }

    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "delete " + filePath$Name + "succeeded!");
                return true;
            } else {
                Log.e("fail","delete " + filePath$Name + "failed");
                return false;
            }
        } else {
            Log.i("fail","delete failed:"+filePath$Name+" not exits");
            return false;
        }
    }

    public void setGreet(){

        Calendar calendar =Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(6<=hour&&hour<=11)
            textView.setText("早上好");
        else if(11<hour&&hour<=13)
            textView.setText("中午好");
        else if(13<hour&&hour<=17)
            textView.setText("下午好");
        else
            textView.setText("晚上好");
    }

    public static class GallyPageTransformer implements ViewPager.PageTransformer {

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
