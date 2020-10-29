package com.kid.william.proyectorss.database;

import android.provider.BaseColumns;

/**
 * Created by Usuario on 1/04/2018.
 */

public class ScriptDatabase {
    /*
    Etiqueta para Depuración
     */
    private static final String TAG = ScriptDatabase.class.getSimpleName();

    // Metainformación de la base de datos
    public static final String ENTRADA_TABLE_NAME = "entrada";
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";

    // Campos de la tabla entrada
    public static class ColumnEntradas {
        public static final String ID = BaseColumns._ID;
        public static final String TITULO = "titulo";
        public static final String DESCRIPCION = "descripcion";
        public static final String CATEGORIA = "categoria";
        public static final String URL = "url";
        public static final String URL_MINIATURA = "thumb_url";

        public static final String[] COLUMNS_ENTRADAS ={ ID,TITULO,DESCRIPCION,CATEGORIA,URL,URL_MINIATURA };
    }

    // Comando CREATE para la tabla ENTRADA
    public static final String CREAR_ENTRADA =
            "CREATE TABLE " + ENTRADA_TABLE_NAME + "(" +
                    ColumnEntradas.ID + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnEntradas.TITULO + " " + STRING_TYPE + " not null," +
                    ColumnEntradas.DESCRIPCION + " " + STRING_TYPE + "," +
                    ColumnEntradas.CATEGORIA + " " + STRING_TYPE + "," +
                    ColumnEntradas.URL + " " + STRING_TYPE + "," +
                    ColumnEntradas.URL_MINIATURA + " " + STRING_TYPE +")";
}
