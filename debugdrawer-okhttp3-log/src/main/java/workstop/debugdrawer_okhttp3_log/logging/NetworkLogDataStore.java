package workstop.debugdrawer_okhttp3_log.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsible for acting as a simple
 *
 * //TODO at least save this to an in-memory, sorted list
 */
public class NetworkLogDataStore {
    List<NetworkLog> networkLogs = new ArrayList<>();

    public void save(String requestMethod, String requestedUrl, String messageID, String responseString,
                     long tookMs, int code, String curlCommand) {
        networkLogs.add(new NetworkLog(System.currentTimeMillis(),
                requestedUrl, responseString, tookMs, code, curlCommand));
    }
}