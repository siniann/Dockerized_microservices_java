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
import java.util.Properties;


@Path("artists")
public class ArtistSearchResource {
    private static final Logger LOGGER = Logger.getLogger(ArtistSearchResource.class.getName());
    private static Properties properties = Configuration.loadProperties();


    @Path("search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Artist getArtist(@QueryParam("artist") String artist)
    {
        Artist myartist = new Artist();
        try{
            if(artist == null || artist.length() == 0)
            {
                LOGGER.log(Level.SEVERE, "Invalid Parameters");
                throw new ClientRequestException(new ErrorMessage("Client Side Error .Please provide parameter for artist"));
            }
        }catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, "Invalid Parameters");
            throw new ClientRequestException(new ErrorMessage("Client Side Error .Please provide parameter for artists"));
        }

        myartist = getArtists(artist);
        return myartist;
    }




    public Artist getArtists(String artist)
    {
        Artist simpleArtist = new Artist();
        String data = null;
        try {
            data = SpotifyApi.getInstance().searchArtists(artist).limit(1).build().getJson();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(data);
            simpleArtist.setId(String.valueOf(actualObj.get("artists").get("items").get(0).get("id")).replace("\"", ""));
            simpleArtist.setName(String.valueOf(actualObj.get("artists").get("items").get(0).get("name")).replace("\"", ""));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebApiException e) {
            e.printStackTrace();
        }

        return simpleArtist;
    }
}
