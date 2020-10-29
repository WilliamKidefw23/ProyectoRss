package com.kid.william.proyectorss.Volley;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Usuario on 2/04/2018.
 * Clase que representa la etiqueta <item> del feed
 */
@Root(name = "item", strict = false)
public class Item {

    //@Element(name="title")
    @Path("title")
    @Text(required=false)
    private String title;

    //@Element(name = "description",data=true,required = true)
    @Path("description")
    @Text(required=false)
    private String descripcion;

    @Element(name="link")
    private String link;

    @Path("category")
    @Text(required=false)
    private String categoria;

    /*@Element(name="description")
    @Namespace(reference="http://search.yahoo.com/mrss/", prefix="media")
    private Content content;*/

    public Item() {
    }

    public Item(String title, String descripcion, String link, Content content) {
        this.title = title;
        this.descripcion = descripcion;
        this.link = link;
        //this.content = content;
    }

    public Item(String title, String descripcion,String categoria, String link) {
        this.title = title;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLink() {
        return link;
    }

    public String getCategoria() {
        return categoria;
    }

    /*public Content getContent() {
        return content;
    }*/
}
