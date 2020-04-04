package browser.green.org.bona.Manager;

import android.icu.math.BigDecimal;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<String, Object, Long> {
    private static int songCount=0;

    @Override
    protected void onPreExecute() {
        Log.i("iSpring", "DownloadTask -> onPreExecute, Thread name: " + Thread.currentThread().getName());
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Long doInBackground(String... params) {

        int count;
        long total = 0;
        try {
            URL url = new URL(params[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conexion.getContentLength();

            // downlod the file
            File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            System.out.println("songCountsongCountsongCountsongCount:"+music);
            //
            File song =new File(music,"song"+"-"+songCount+".mp3");
            songCount++;
            FileOutputStream fout=new FileOutputStream(song);
            InputStream input = new BufferedInputStream(url.openStream());

            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int)(total*100/lenghtOfFile));
                fout.write(data, 0, count);
            }
            fout.flush();
            fout.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    //下载文件后返回一个Object数组：下载文件的字节数以及下载的博客的名字
    @RequiresApi(api = Build.VERSION_CODES.N)
    private float downloadSingleFile(String str){
        int byteCount = 0;
        HttpURLConnection conn = null;
        try{
            URL url = new URL(str);
            conn = (HttpURLConnection)url.openConnection();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length = -1;
            while ((length = is.read(buf)) != -1) {
                baos.write(buf, 0, length);
                byteCount += length;
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        float result=byteCount/(1024*1024);
        BigDecimal bg = new BigDecimal(result);
        result = (float) bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        Log.i("iSpring", "DownloadTask -> onProgressUpdate, Thread name: " + Thread.currentThread().getName());
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Long aLong) {
        Log.i("iSpring", "DownloadTask -> onPostExecute, Thread name: " + Thread.currentThread().getName());
        super.onPostExecute(aLong);
    }

    @Override
    protected void onCancelled() {
        Log.i("iSpring", "DownloadTask -> onCancelled, Thread name: " + Thread.currentThread().getName());
        super.onCancelled();
    }


}
