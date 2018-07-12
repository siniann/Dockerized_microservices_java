package de.uniba.dsg.jaxrs.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.exceptions.WebApiException;
import de.uniba.dsg.Configuration;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Tracks;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("charts")
public class ChartsService {
    private static final Logger LOGGER = Logger.getLogger(ChartsService.class.getName());
    private static Properties properties = Configuration.loadProperties();


    @Path("{artist-id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tracks> getCharts(@PathParam("artist-id") String artistId) {
        try {
            if (artistId == null) {
                LOGGER.log(Level.SEVERE, "Artist ID is not Provided");
                throw new ClientRequestException(new ErrorMessage("Please provide a valid Artist ID"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Artist ID is not Provided");
            throw new ClientRequestException(new ErrorMessage("Please provide a valid Artist ID"));
        }

        List<Tracks> mytracks = new ArrayList<>();
        String charts = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            charts = SpotifyApi.getInstance().getTopTracksForArtist(artistId, "DE").build().getJson();
            JsonNode actualObj = mapper.readTree(charts);

            for (int i = 0; i < actualObj.get("tracks").size(); i++) {
                Tracks track = new Tracks();
                String artist_name = "";

                for (int j = 0; j < actualObj.get("tracks").get(i).get("artists").size(); j++) {
                    artist_name += String.valueOf(actualObj.get("tracks").get(i).get("artists").get(j).get("name"));
                }

                track.setArtist(artist_name.replace("\"", ""));
                track.setTitle(String.valueOf(actualObj.get("tracks").get(i).get("name")).replace("\"", ""));
                track.setTrack_id(String.valueOf(actualObj.get("tracks").get(i).get("id")).replace("\"", ""));
                mytracks.add(track);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Some IOexception occurred!");
        } catch (WebApiException e) {
            LOGGER.log(Level.SEVERE, "Error 404!! Resource not Found");
            throw new ClientRequestException(new ErrorMessage("Error 404!! Resource not Found.Please check your Artist ID"));
        }
        return mytracks;
    }


}
