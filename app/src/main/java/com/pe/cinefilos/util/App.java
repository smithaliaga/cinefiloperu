package proguide.walleton.util;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App instance;
    private static Context context;

    public static App getInstance() {
        if (null == instance){
            instance = new App();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

}