package com.kid.william.proyectorss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetalleLectorActivity extends AppCompatActivity {

    /*
    Etiqueta de depuración
     */
    private static final String TAG = DetalleLectorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lector);

        // Dehabilitar titulo de la actividad
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Recuperar url
        String urlExtra = getIntent().getStringExtra("url-extra");

        // Obtener WebView
        WebView webview = (WebView)findViewById(R.id.webview);

        // Habilitar Javascript en el renderizado
        webview.getSettings().setJavaScriptEnabled(true);

        // Transmitir localmente
        webview.setWebViewClient(new WebViewClient());

        // Cargar el contenido de la url
        webview.loadUrl(urlExtra);
    }
}
