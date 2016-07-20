package workstop.debugdrawer_okhttp3_log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import workstop.debugdrawer_okhttp3_log.logging.NetworkLog;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsbile for providing the UI for the list of {@link NetworkLog}s.
 */
public class DebugLogActivity extends Activity {

    /**
     * This is called when you want to start the {@link DebugLogActivity}. This activity will present
     * a list of detailed information about {@link NetworkLog}s generated in your application.*/
    public static void newInstance(Activity startingActivity) {
        Intent start = new Intent(startingActivity, DebugLogActivity.class);
        startingActivity.startActivity(start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_activity_debug_log);
    }
}
