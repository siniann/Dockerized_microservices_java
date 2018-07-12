package de.uniba.dsg.models;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Cover")
@XmlType(propOrder = {"url"} ,namespace = "http://jaxws.dsg.uniba.de/")
public class Cover {
    private String url;

    public Cover() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}