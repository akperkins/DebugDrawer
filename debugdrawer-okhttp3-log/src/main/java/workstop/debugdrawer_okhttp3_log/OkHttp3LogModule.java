package workstop.debugdrawer_okhttp3_log;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.palaima.debugdrawer.base.DebugModule;
import okhttp3.OkHttpClient;
import workstop.debugdrawer_okhttp3_log.logging.CurlLoggingGenerator;
import workstop.debugdrawer_okhttp3_log.logging.NetworkLogDataStore;
import workstop.debugdrawer_okhttp3_log.logging.NetworkLogInterceptor;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsible for adding the ability to view your {@link OkHttpClient}
 * requests/responses through the DebugDrawer with the goal of giving any developer that uses this
 * particular {@link DebugModule} increased visibility into how their app is interacting
 * with an API.
 */
public class OkHttp3LogModule implements DebugModule {
    /** Used to assert that any project trying to use this dependency is dependent on {@link OkHttpClient}*/
    private static final boolean HAS_OKHTTP3;
    /** Used to create the {@link DebugLogActivity}*/
    private final Activity activity;

    static {
        boolean hasDependency;
        try {
            Class.forName("okhttp3.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }
        HAS_OKHTTP3 = hasDependency;
    }

    /**
     * This is called when you are configuring your {@link OkHttpClient} instance with the
     * {@link okhttp3.OkHttpClient.Builder} as that is the only time we have access to add our own
     * {@link okhttp3.Interceptor}s.
     *
     */
    public static void configureNetworkInterceptor(OkHttpClient.Builder builder) {
        CurlLoggingGenerator curlLoggingGenerator = new CurlLoggingGenerator();
        NetworkLogDataStore networkLogDataStore = new NetworkLogDataStore();
        builder.addNetworkInterceptor(new NetworkLogInterceptor(curlLoggingGenerator, networkLogDataStore));
    }

    public OkHttp3LogModule(@NonNull Activity activity) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        this.activity = activity;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_okhttp3_log, parent, false);

        Button okHttpViewLog = (Button) view.findViewById(R.id.showNetworkLog);
        okHttpViewLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DebugLogActivity.newInstance(activity);
            }
        });

        return view;
    }

    @Override
    public void onOpened() {
        //no-op
    }

    @Override
    public void onClosed() {
        //no-op
    }

    @Override
    public void onResume() {
        //no-op
    }

    @Override
    public void onPause() {
        //no-op
    }

    @Override
    public void onStart() {
        //no-op
    }

    @Override
    public void onStop() {
        //no-op
    }
}

