package com.kid.william.proyectorss;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kid.william.proyectorss.database.LectorDataBase;
import com.kid.william.proyectorss.database.ScriptDatabase;
import com.kid.william.proyectorss.Volley.Rss;
import com.kid.william.proyectorss.Volley.VolleySingleton;
import com.kid.william.proyectorss.Volley.XmlRequest;
import com.kid.william.proyectorss.utils.UtilConexion;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static final String TAG = MainActivity.class.getSimpleName();
    //public static final String URL_FEED = "http://estaticos.marca.com/rss/portada.xml";
    public static final String URL_FEED = "https://depor.com/arcio/rss";
    //public static final String URL_FEED = "https://www.forbes.com/most-popular/feed";
    private LectorAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener la lista
        listView = findViewById(R.id.listaLector);
        swipeRefreshLayout = findViewById(R.id.refreshlayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadRSS();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) adapter.getItem(position);
                // Obtene url de la entrada seleccionada
                String url = c.getString(c.getColumnIndex(ScriptDatabase.ColumnEntradas.URL));
                // Nuevo intent explícito
                Intent i = new Intent(MainActivity.this, DetalleLectorActivity.class);
                // Setear url
                i.putExtra("url-extra", url);
                // Iniciar actividad
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRSS();
    }

    private void showLoading(final boolean valor){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(valor);
            }
        });
    }

    private void loadRSS(){
        showLoading(true);
        if(UtilConexion.isConnected(this)){
            VolleySingleton.getInstance(this).addToRequestQueue(
                    new XmlRequest<>(
                            URL_FEED,
                            Rss.class,
                            null,
                            new Response.Listener<Rss>() {
                                @Override
                                public void onResponse(Rss response) {
                                    // Caching
                                    LectorDataBase.getInstance(MainActivity.this).
                                            sincronizarEntradas(response.getChannel().getItems());
                                    // Carga inicial de datos...
                                    new LoadData().execute();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    showLoading(false);
                                    //Log.d(TAG, "Error Volley: " + error.getMessage());
                                    Toast.makeText(MainActivity.this,
                                            getString(R.string.main_Error1)+error,Toast.LENGTH_LONG).show();
                                }
                            }
                    )
            );
        }else {
            showLoading(false);
            //Log.i(TAG, "La conexión a internet no está disponible");
            Toast.makeText(this,getString(R.string.main_Error2),Toast.LENGTH_LONG).show();
            adapter= new LectorAdapter(
                    this,
                    LectorDataBase.getInstance(this).obtenerEntradas(),
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listView.setAdapter(adapter);
        }
    }

    public class LoadData extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            // Carga inicial de registros
            return LectorDataBase.getInstance(MainActivity.this).obtenerEntradas();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            showLoading(false);
            //Log.d(TAG,"TAMAÑO "+cursor.getCount());
            // Crear el adaptador
            adapter = new LectorAdapter(
                    MainActivity.this,
                    cursor,
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            // Relacionar la lista con el adaptador
            listView.setAdapter(adapter);
        }
    }
}
