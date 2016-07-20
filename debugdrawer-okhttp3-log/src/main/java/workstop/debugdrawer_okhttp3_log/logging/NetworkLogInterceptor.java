package workstop.debugdrawer_okhttp3_log.logging;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsible for generating a {@link NetworkLog} that contains various pieces of
 * information about a {@link Request}.
 */
public class NetworkLogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String MESSAGE_ID_KEY = "messageID";

    final private CurlLoggingGenerator curlLoggingGenerator;
    final private NetworkLogDataStore networkLogDataStore;

    public NetworkLogInterceptor(CurlLoggingGenerator curlLoggingGenerator,
                                 NetworkLogDataStore networkLogDataStore) {
        this.curlLoggingGenerator = curlLoggingGenerator;
        this.networkLogDataStore = networkLogDataStore;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        String requestMethod = request.method();
        String requestedUrl = request.url().encodedPath();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String responseString = "";
        String messageID = "";
        if (responseBody.contentLength() != 0) {
            responseString = buffer.clone().readString(charset);
            messageID = response.header(MESSAGE_ID_KEY);
        }

        String curlCommand = curlLoggingGenerator.generateCurlCommand(request);
        networkLogDataStore.save(requestMethod, requestedUrl, messageID, responseString, tookMs,
                response.code(), curlCommand);
        return response;
    }
}
