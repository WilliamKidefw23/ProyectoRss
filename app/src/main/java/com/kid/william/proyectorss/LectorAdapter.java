package com.kid.william.proyectorss;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kid.william.proyectorss.database.ScriptDatabase;
import com.kid.william.proyectorss.Volley.VolleySingleton;


public class LectorAdapter extends CursorAdapter {

    private static final String TAG = LectorAdapter.class.getSimpleName();

    /**
     * View holder para evitar multiples llamadas de findViewById()
     */
    public static class ViewHolder {
        TextView titulo;
        TextView descripcion;
        TextView categoria;
        NetworkImageView imagen;
        int tituloI;
        int descripcionI;
        int categoriaI;
        int imagenI;
    }

    public LectorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lector_layout, null, false);
        ViewHolder vh = new ViewHolder();
        // Almacenar referencias
        //vh.titulo = (TextView) view.findViewById(R.id.titulo);
        vh.descripcion =  view.findViewById(R.id.descripcion);
        vh.categoria = view.findViewById(R.id.categoria);
        //vh.imagen = (NetworkImageView) view.findViewById(R.id.imagen);
        // Setear indices
        vh.tituloI = cursor.getColumnIndex(ScriptDatabase.ColumnEntradas.TITULO);
        vh.descripcionI = cursor.getColumnIndex(ScriptDatabase.ColumnEntradas.DESCRIPCION);
        vh.categoriaI = cursor.getColumnIndex(ScriptDatabase.ColumnEntradas.CATEGORIA);
        vh.imagenI = cursor.getColumnIndex(ScriptDatabase.ColumnEntradas.URL_MINIATURA);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();
        // Setear el texto al titulo
        //vh.titulo.setText(cursor.getString(vh.tituloI));
        // Setear el texto a la categoria
        vh.categoria.setText(cursor.getString(vh.categoriaI));
        // Obtener acceso a la descripción y su longitud
        //int ln = cursor.getString(vh.descripcionI).length();
        String descripcion = cursor.getString(vh.descripcionI);
        // Acortar descripción a 200 caracteres
        //if (ln >= 200)
        //    vh.descripcion.setText(descripcion.substring(0, 200)+"...");
        //else vh.descripcion.setText(descripcion);
        vh.descripcion.setText(descripcion);
        // Obtener URL de la imagen
        String thumbnailUrl = cursor.getString(vh.imagenI);
        // Obtener instancia del ImageLoader
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        // Volcar datos en el image view
        //vh.imagen.setImageUrl(thumbnailUrl, imageLoader);
    }
}
