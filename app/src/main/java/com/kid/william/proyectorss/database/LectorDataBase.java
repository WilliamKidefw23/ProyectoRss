package com.kid.william.proyectorss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kid.william.proyectorss.Volley.Item;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Usuario on 1/04/2018.
 */

public class LectorDataBase extends SQLiteOpenHelper {

    // Mapeado rápido de indices
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TITULO = 1;
    private static final int COLUMN_DESC = 2;
    private static final int COLUMN_CATE = 3;
    private static final int COLUMN_URL = 4;

    //Instancia singleton
    private static LectorDataBase singleton;

    //Etiqueta de depuración
    private static final String TAG = LectorDataBase.class.getSimpleName();

    //Nombre de la base de datos
    public static final String DATABASE_NAME = "Feed.db";

    //Versión actual de la base de datos
    public static final int DATABASE_VERSION = 1;

    private LectorDataBase(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
    public static synchronized LectorDataBase getInstance(Context context) {
        if (singleton == null) {
            singleton = new LectorDataBase(context.getApplicationContext());
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla 'entrada'
        db.execSQL(ScriptDatabase.CREAR_ENTRADA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Añade los cambios que se realizarán en el esquema
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabase.ENTRADA_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Obtiene todos los registros de la tabla entrada
     *
     * @return cursor con los registros
     */
    public Cursor obtenerEntradas() {
        // Seleccionamos todas las filas de la tabla 'entrada'
        //Descentralizado
        /*return getWritableDatabase().rawQuery(
                "SELECT * FROM " + ScriptDatabase.ENTRADA_TABLE_NAME +
                        //" WHERE "+ScriptDatabase.ColumnEntradas.CATEGORIA+"=?"+
                        " ORDER BY " +ScriptDatabase.ColumnEntradas.ID+" DESC"
                , null);*/

        String valor = "Contacto";
        return getWritableDatabase().query(ScriptDatabase.ENTRADA_TABLE_NAME,
                ScriptDatabase.ColumnEntradas.COLUMNS_ENTRADAS,
                //"categoria=?",new String[]{valor},
                null,null,
                null,
                null,
                ScriptDatabase.ColumnEntradas.ID+" DESC"
        //null
        );
    }

    /**
     * Inserta un registro en la tabla entrada
     *
     * @param titulo      titulo de la entrada
     * @param descripcion desripcion de la entrada
     * @param url         url del articulo
     * @param thumb_url   url de la miniatura
     */
    /*public void insertarEntrada(
            String titulo,
            String descripcion,
            String url,
            String thumb_url) {
        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnEntradas.TITULO, titulo);
        values.put(ScriptDatabase.ColumnEntradas.DESCRIPCION, descripcion);
        values.put(ScriptDatabase.ColumnEntradas.URL, url);
        values.put(ScriptDatabase.ColumnEntradas.URL_MINIATURA, thumb_url);
        // Insertando el registro en la base de datos
        getWritableDatabase().insert(
                ScriptDatabase.ENTRADA_TABLE_NAME,
                null,
                values
        );
    }*/

    public void insertarEntrada(
            String titulo,
            String descripcion,
            String categoria,
            String url) {
        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnEntradas.TITULO, titulo);
        values.put(ScriptDatabase.ColumnEntradas.DESCRIPCION, descripcion);
        values.put(ScriptDatabase.ColumnEntradas.CATEGORIA, categoria);
        values.put(ScriptDatabase.ColumnEntradas.URL, url);
        // Insertando el registro en la base de datos
        getWritableDatabase().insert(
                ScriptDatabase.ENTRADA_TABLE_NAME,
                null,
                values);
    }

    /**
     * Modifica los valores de las columnas de una entrada
     *
     * @param id          identificador de la entrada
     * @param titulo      titulo nuevo de la entrada
     * @param descripcion descripcion nueva para la entrada
     * @param url         url nueva para la entrada
     * @param thumb_url   url nueva para la miniatura de la entrada
     */
    /*public void actualizarEntrada(int id,
                                  String titulo,
                                  String descripcion,
                                  String url,
                                  String thumb_url) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnEntradas.TITULO, titulo);
        values.put(ScriptDatabase.ColumnEntradas.DESCRIPCION, descripcion);
        values.put(ScriptDatabase.ColumnEntradas.URL, url);
        values.put(ScriptDatabase.ColumnEntradas.URL_MINIATURA, thumb_url);

        // Modificar entrada
        getWritableDatabase().update(
                ScriptDatabase.ENTRADA_TABLE_NAME,
                values,
                ScriptDatabase.ColumnEntradas.ID + "=?",
                new String[]{String.valueOf(id)});

    }*/

    public void actualizarEntrada(int id,
                                  String titulo,
                                  String descripcion,
                                  String categoria,
                                  String url) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnEntradas.TITULO, titulo);
        values.put(ScriptDatabase.ColumnEntradas.DESCRIPCION, descripcion);
        values.put(ScriptDatabase.ColumnEntradas.CATEGORIA, categoria);
        values.put(ScriptDatabase.ColumnEntradas.URL, url);

        // Modificar entrada
        getWritableDatabase().update(
                ScriptDatabase.ENTRADA_TABLE_NAME,
                values,
                ScriptDatabase.ColumnEntradas.ID + "=?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Procesa una lista de items para su almacenamiento local
     * y sincronización.
     *
     * @param entries lista de items
     */
    public void sincronizarEntradas(List<Item> entries) {

        //#1 Mapear temporalemente las entradas nuevas para realizar una comparación con las locales

        HashMap<String, Item> entryMap = new HashMap<String, Item>();
        for (Item e : entries) {
            entryMap.put(e.getTitle(), e);
        }

        //#2 Obtener las entradas locales
        //Log.i(TAG, "Consultar items actualmente almacenados");
        Cursor c = obtenerEntradas();
        assert c != null;
        //Log.i(TAG, "Se encontraron " + c.getCount() + " entradas, computando...");

        //#3  Comenzar a comparar las entradas
        int id;
        String titulo;
        String descripcion;
        String categoria;
        String url;

        while (c.moveToNext()) {
            id = c.getInt(COLUMN_ID);
            titulo = c.getString(COLUMN_TITULO);
            descripcion = c.getString(COLUMN_DESC);
            categoria = c.getString(COLUMN_CATE);
            url = c.getString(COLUMN_URL);
            Item match = entryMap.get(titulo);
            if (match != null) {
                // Filtrar entradas existentes. Remover para prevenir futura inserción
                entryMap.remove(titulo);

                //#3.1 Comprobar si la entrada necesita ser actualizada
                if ((match.getTitle() != null && !match.getTitle().equals(titulo)) ||
                        (match.getDescripcion() != null && !match.getDescripcion().equals(descripcion)) ||
                        (match.getCategoria() != null && !match.getCategoria().equals(categoria)) ||
                        (match.getLink() != null && !match.getLink().equals(url))) {
                    // Actualizar entradas
                    actualizarEntrada(
                            id,
                            match.getTitle(),
                            match.getDescripcion(),
                            match.getCategoria(),
                            match.getLink()
                    );
                }
            }
        }
        c.close();

        //#4 Añadir entradas nuevas
        for (Item e : entryMap.values()) {
            //Log.i(TAG, "Insertado: titulo=" + e.getTitle());
            insertarEntrada(
                    e.getTitle(),
                    e.getDescripcion(),
                    e.getCategoria(),
                    e.getLink()
            );
        }
        //Log.i(TAG, "Se actualizaron los registros");
    }
}
