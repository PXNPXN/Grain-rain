package browser.green.org.bona.Application;

import android.app.Application;
import android.content.Context;

import fm.qingting.qtsdk.QTSDK;

public class BoNaApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        QTSDK.setHost("https://open.staging.qingting.fm");
        QTSDK.init(getApplicationContext(), "MmYxYThlY2EtYWMxMi0xMWU4LTkyM2YtMDAxNjNlMDAyMGFk"
        );
        QTSDK.setAuthRedirectUrl("http://qttest.qingting.fm");
    }

    public static Context getContext() {
        return mContext;
    }
}


