package com.kid.william.proyectorss.Volley;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Usuario on 2/04/2018.
 * Clase que representa al elemento <rss> del feed
 */

@Root(name = "rss", strict = false)
@Namespace(reference="http://search.yahoo.com/mrss/")
public class Rss {

    @Element
    private Channel channel;

    public Rss() {
    }

    public Rss(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
