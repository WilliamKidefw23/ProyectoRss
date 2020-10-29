package com.kid.william.proyectorss.Volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by Usuario on 2/04/2018.
 */

public class XmlRequest<T> extends Request<T> {

    private static final String TAG = XmlRequest.class.getSimpleName();

    // Atributos
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final Serializer serializer = new Persister();

    /**
     * Se predefine para el uso de peticiones GET
     */
    public XmlRequest(String url, Class<T> clazz, Map<String, String> headers,
                      Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            // Convirtiendo el flujo en cadena con formato UTF-8
            String xml = new String(response.data, "UTF-8");
            // Depurando...
            //Log.d(TAG,"Depurando:"+ xml);
            // Enviando la respuesta parseada
            return Response.success(
                    serializer.read(clazz, xml),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            //Log.e(TAG,"UnsupportedEncodingException:"+e.getMessage());
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.e(TAG,"Exception:"+e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    /*@Override
    public void deliverError(VolleyError error) {
        Log.d(TAG, "deliverError");

        final int status = error.networkResponse.statusCode;
        // Handle 30x
        if(HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
            final String location = error.networkResponse.headers.get("Location");
            Log.d(TAG, "Location: " + location);
            final XmlRequest<T> request = new XmlRequest<T>(
                    method, location, jsonRequest, this.requestContentType, this.clazz, this.listener, this.errorListener);
            // Construct a request clone and change the url to redirect location.
            RequestManager.getRequestQueue().add(request);
        }
    }*/

    /*@Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

        Log.e(TAG, error.getMessage(), error);

        final int status = error.networkResponse.statusCode;
        // Handle 30x
        if (status == HttpURLConnection.HTTP_MOVED_PERM ||
                status == HttpURLConnection.HTTP_MOVED_TEMP ||
                status == HttpURLConnection.HTTP_SEE_OTHER) {
            final String location = error.networkResponse.headers.get("Location");
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Location: " + location);
            }
        }
    }

/*
    @Override
    public String getUrl() {
        Log.d(TAG,"getUrl");
        String url = super.getUrl();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url; // use http by default
        }

        return url;
    }
*/
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
