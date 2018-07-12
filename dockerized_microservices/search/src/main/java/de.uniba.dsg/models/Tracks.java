package de.uniba.dsg.models;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "Tracks")
@XmlType(propOrder = {"artist", "track_id", "title"}, namespace = "http://jaxws.dsg.uniba.de/")
public class Tracks {
    private String title;
    private String id;
    private String artist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id;}

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}