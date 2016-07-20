package workstop.debugdrawer_okhttp3_log.logging;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsible for representing a single occurrence of a network
 * request/response.
 *
 */
public class NetworkLog {
    final private long epoch;
    final private String endpoint;
    final private String response;
    final private long duration;
    final private int responseCode;
    final private String curlCommand;

    public NetworkLog(long epoch, String endpoint, String response, long duration, int responseCode,
                      String curlCommand) {
        this.epoch = epoch;
        this.endpoint = endpoint;
        this.response = response;
        this.duration = duration;
        this.responseCode = responseCode;
        this.curlCommand = curlCommand;
    }

    public long getEpoch() {
        return epoch;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getResponse() {
        return response;
    }

    public long getDuration() {
        return duration;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getCurlCommand() {
        return curlCommand;
    }
}
