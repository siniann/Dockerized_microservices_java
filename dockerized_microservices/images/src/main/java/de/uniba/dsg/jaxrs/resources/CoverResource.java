package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.models.Image;
import com.wrapper.spotify.models.Track;
import de.uniba.dsg.Configuration;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Cover;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

@Path("covers")
public class CoverResource {
    private static final Logger LOGGER = Logger.getLogger(Cover.class.getName());
    private static Properties properties = Configuration.loadProperties();

    @GET
    @Path("{track-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cover getImages(@PathParam("track-id") String trackId) {
        if (trackId == null)
            throw new ClientRequestException(new ErrorMessage("Track with id required."));
        TrackRequest request = SpotifyApi
                .getInstance()
                .getTrack(trackId)
                .build();
        try {
            Track track = request.get();

            if (track == null)
                throw new ResourceNotFoundException(new ErrorMessage(("No track available.Please retry!")));

            Image image = track.getAlbum().getImages().get(0);
            String url = image.getUrl();
            Cover cover = new Cover();
            cover.setUrl(url);
            return cover;

        } catch (WebApiException | IOException e) {
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}