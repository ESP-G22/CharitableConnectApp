package api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import layout.OutputPair;

public class HTTPConnection {
    public static final String PROBLEM_WITH_SENDING_REQUEST_MSG = "Problem with sending the request.\n" +
            "Are you connected to the Internet?";
    public static final String PROBLEM_WITH_SENDING_JSON_MSG = "Problem with sending parameters.";
    public static final String PARAMETERS_SENT_MSG = "Parameters passed";
    public static final String ERROR_404_MSG = "Could not contact server.";
    public static final String ERROR_403_MSG = "You do not have the permission to do this. Are you logged in as the correct user?";
    public static final String NO_RESPONSE_CODE_MSG = "Problem with getting response";
    public static final String EXTRACTING_RESPONSE_ERROR_MSG = "Problem with parsing JSON";

    public HttpURLConnection conn;

    public HTTPConnection() {
        conn = null;
    }

    // REQUESTS

    public OutputPair post(String urlStr, JSONObject input) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            OutputPair out = passParams(input);
            if (!out.isSuccess()) {
                return out;
            }
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }

    public OutputPair post(String urlStr, JSONObject input, String authHeaderValue) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
            OutputPair out = passParams(input);
            if (!out.isSuccess()) {
                return out;
            }
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }

    public OutputPair get(String urlStr, String authHeaderValue) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }

    public OutputPair delete(String urlStr, JSONObject input, String authHeaderValue) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
            passParams(input);
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }

    public OutputPair delete(String urlStr, String authHeaderValue) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }
    public OutputPair put(String urlStr, JSONObject input, String authHeaderValue) {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
            passParams(input);
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }
        return getResponse();
    }

    public OutputPair postImage(String urlStr, Bitmap image, String authHeaderValue) {
        // convert image to byte array for upload.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageContents = stream.toByteArray();
        image.recycle();

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            DataOutputStream imageStream = new DataOutputStream(out);
            imageStream.write(imageContents, 0, imageContents.length);
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        return getResponse();
    }

    public Bitmap getImage(String urlStr, String authHeaderValue) throws IOException {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            //Bitmap bitmap = BitmapFactory.decodeStream((InputStream) conn.getEntity().getContent());
        } catch (IOException err) {
            throw new IOException(PROBLEM_WITH_SENDING_REQUEST_MSG);
        }

        /*
        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            throw new Exception(status.getMessage());
        }

        //BitmapFactory.decodeByteArray(null, 0, 1);
         */
        return null;
    }


    // OTHER

    /**
     * After sending a request, this determines what response to output to the screen or
     * how to proceed to get proper output.
     *
     * @return OutputPair containing several error messages if it did not work.
     */
    private OutputPair getResponse() {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 404) {
                return new OutputPair(false, ERROR_404_MSG);
            }
            if (responseCode == 403) {
                return new OutputPair(false, ERROR_403_MSG);
            }
            if ((responseCode > 299) || (responseCode < 200)) {
                return new OutputPair(false, HTTPConnection.getJSONResponse(conn.getErrorStream()).toString());
            } else {
                return new OutputPair(true, HTTPConnection.getJSONResponse(conn.getInputStream()).toString());
            }

        } catch (IOException err) {
            return new OutputPair(false, NO_RESPONSE_CODE_MSG);
        } catch (JSONException err) {
            return new OutputPair(false, EXTRACTING_RESPONSE_ERROR_MSG);
        }
    }

    /**
     * Pass JSON into output stream. Used in requests with parameters.
     *
     * @param params JSON body.
     */
    private OutputPair passParams(JSONObject params) {
        try {
            OutputStream wr = conn.getOutputStream();
            wr.write(params.toString().getBytes("UTF-8"));
            wr.flush();
            wr.close();
        } catch (IOException err) {
            return new OutputPair(false, PROBLEM_WITH_SENDING_JSON_MSG);
        }
        return new OutputPair(true, PARAMETERS_SENT_MSG);
    }

    /**
     * Disconnect the current HttpURLConnection after a request.
     *
     * Sets connection to null.
     */
    public void disconnect() {
        conn.disconnect();
        conn = null;
    }

    /**
     * Convert response from HTTP request to JSON.
     *
     * @param stream Response from HTTP request. Correct or error stream.
     * @return Response stream in JSON.
     * @throws IOException If the stream cannot be read, or no lines can be read.
     * @throws JSONException If the response cannot be converted to JSON.
     */
    public static JSONArray getJSONResponse(InputStream stream) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String lines;
        while ((lines = reader.readLine()) != null) {
            builder.append(lines);
            builder.append(System.getProperty("line.separator"));
        }

        String retStr = builder.toString().trim();
        reader.close();

        // Converting response to format that looks like an array so it can be converted to a
        // JSON array.
        if ((retStr.charAt(0) != '[') || (retStr.charAt(retStr.length() - 1) != ']')) {
            return new JSONArray("[" + retStr + "]");
        }
        return new JSONArray(retStr);
    }
}
