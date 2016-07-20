package workstop.debugdrawer_okhttp3_log.logging;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * @author Andre Perkins (Fuzz).
 *
 * This class is responsible for generating a curl request from a {@link Request}. This generated
 * curl call is executable and represents the same requests that was executed by the app for this
 * particular {@link Request}.
 */
public class CurlLoggingGenerator {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @NonNull
    public String generateCurlCommand(Request request) throws IOException {
        boolean compressed = false;
        String curlCmd = "curl";
        curlCmd += " -X " + request.method();

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            if ("Accept-Encoding".equalsIgnoreCase(name) && "gzip".equalsIgnoreCase(value)) {
                compressed = true;
            }
            curlCmd += " -H " + "\"" + name + ": " + value + "\"";
        }

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            // try to keep to a single line and use a subshell to preserve any line breaks
            curlCmd += " --data $'" + buffer.readString(charset).replace("\n", "\\n") + "'";
        }

        curlCmd += ((compressed) ? " --compressed " : " ") + "\"" + request.url() + "\"";
        return curlCmd;
    }
}