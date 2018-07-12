package de.uniba.dsg.jaxrs.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Track;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.models.Artist;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Tracks;
import de.uniba.dsg.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Path("tracks")
public class TrackSearchResource {
    private static final Logger LOGGER = Logger.getLogger(TrackSearchResource.class.getName());

    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tracks getArtistTrack(@QueryParam("title") String title, @QueryParam("artist") String artist) {
        System.out.println("title =" + title + " Artist = " + artist);
        Tracks mytrack = new Tracks();

        try {
            if (title == null || title.length() == 0) {
                LOGGER.log(Level.SEVERE, "Invalid Parameters");
                throw new ClientRequestException(new ErrorMessage("Client Side Error .Please provide a title"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid Parameters");
            throw new ClientRequestException(new ErrorMessage("Client Side Error .Please provide a title"));
        }
        if (artist == null || artist.length() == 0) {
            mytrack = getTitle(title);
        } else {
            mytrack = getArtistWithTrack(title, artist);
        }
        return mytrack;

    }

    public Tracks getArtistWithTrack(String title, String artist) {
        String song = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            song = SpotifyApi.getInstance().searchTracks(title).build().getJson();
            JsonNode actualObj = mapper.readTree(song);

            for (int i = 0; i < actualObj.get("tracks").get("items").size(); i++) {
                Tracks mytrack = new Tracks();

                for (int j = 0; j < actualObj.get("tracks").get("items").get(i).get("artists").size(); j++) {
                    String art = (String.valueOf(actualObj.get("tracks").get("items").get(i).get("artists").get(j).get("name")).replace("\"", ""));

                    if (art.toLowerCase().contains(artist.toLowerCase())) {
                        mytrack.setTitle(String.valueOf(actualObj.get("tracks").get("items").get(i).get("name")).replace("\"", ""));
                        mytrack.setId(String.valueOf(actualObj.get("tracks").get("items").get(i).get("id")).replace("\"", ""));
                        mytrack.setArtist(String.valueOf(actualObj.get("tracks").get("items").get(i).get("artists").get(j).get("name")).replace("\"", ""));
                        return mytrack;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in IOexception");
        } catch (WebApiException e) {
            LOGGER.log(Level.SEVERE, "Error 404!! Resource not Found");
            throw new ClientRequestException(new ErrorMessage("Error 404!! Resource not Found.Please check your Artist ID"));
        }

        throw new ClientRequestException(new ErrorMessage("Error 404!! Resource not Found."));

    }

    //Search only with title
    public Tracks getTitle(String title) {
        System.out.println("here in get title");
        String song = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            song = SpotifyApi.getInstance().searchTracks(title).build().getJson();
            JsonNode actualObj = mapper.readTree(song);
            for (int i = 0; i < actualObj.get("tracks").get("items").size(); i++) {
                Tracks mytrack = new Tracks();
                mytrack.setTitle(String.valueOf(actualObj.get("tracks").get("items").get(i).get("name")).replace("\"", ""));
                mytrack.setId(String.valueOf(actualObj.get("tracks").get("items").get(i).get("id")).replace("\"", ""));
                mytrack.setArtist(String.valueOf(actualObj.get("tracks").get("items").get(i).get("artists").get(0).get("name")).replace("\"", ""));
                return (mytrack);

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in IOexception");
        } catch (WebApiException e) {
            LOGGER.log(Level.SEVERE, "Error 404!! Resource not Found");
            throw new ClientRequestException(new ErrorMessage("Error 404!! Resource not Found.Please check your Artist ID"));
        }

        throw new ClientRequestException(new ErrorMessage("Error 404!! Resource not Found."));
    }


}
