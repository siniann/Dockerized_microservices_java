This is a mock-up of a web music player. We leverage the Spotify API and the Java wrapper to get access to music data. Here, we will break down the necessary functionality into a set of small microservices and orchestrate them to achieve the desired use case. All services should be implemented as RESTful Web Services and hosted inside separate Docker containers. 
----------------------------------
Search 
The Search service is the main service of the orchestration. It allows to query for artists and song titles and returns the appropriate Spotify IDs on success. These IDs are the global iden- tifiers that can be passed to the other services to request an artist’s top songs and cover images for a particular track. The service should expose two different resources: one for tracks and one for ar- tists. The necessary parameters should be passed via query params. For instance, the search resour- ce for tracks should be available at /tracks/search?title={song title}&artist={artist}. The track search should return an object that contains the track ID, artist name, and song title. For the artist object it is sufficient to return the artist ID and the artist name.

Charts 
The Charts service should return the current top songs for a particular artist. For the implementation of the service, you can reuse the Java code querying the Spotify API from the first assignment. To get a list of tracks (ID, title, artist) the endpoint needs a valid artist ID. The resource should be exposed at /charts/{artistID}.

Images 
This service should return the cover art for a particular track ID. 

Web 
The web project mimics a mock-up of a web music player and integrates all of the previously mentioned services. It will display the currently played song title and artist name together with the cover art. Moreover, it will also show the current top songs of the artist. 

The user interface is available at http://{docker-machine- ip}:8888. 

To alter the currently played song, change the URL query params inside the browser window, e.g., http://{docker-machine-ip}:8888/?title={songtitle}&artist={artist na- me}.


Service Orchestration
 All of the described services should be packaged inside their own Docker container and orchestrated via Docker Compose

 ------------------------------------------
 Perform gradle build in each individual microservices.The generated artifacts are used to build images for running in the docker container.

 Perform docker-compose build,followed by docker-compose up to make the services up and running
