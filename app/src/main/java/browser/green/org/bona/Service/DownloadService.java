package browser.green.org.bona.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new DownloadBinder();
    }

    public class DownloadBinder extends Binder{
        public void get_Request(String url) throws IOException {getRequest(url);}
    }

    public void getRequest(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url).build();
        Response response=client.newCall(request).execute();
        response.body().contentType();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
