package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistResponse {
    private final String id;
    private final String name;
    private final Boolean owner;
    private final List<GetTrackResponse> tracks;

    public GetPlaylistResponse(String id, String name, Boolean owner, List<GetTrackResponse> tracks) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.tracks = tracks;
    }

    public static GetPlaylistResponse fromEntity(Playlist playlist, String currentUserId) {
        List<GetTrackResponse> tracks = new ArrayList<>();

        for(Track track : playlist.getTracks()) {
            tracks.add(GetTrackResponse.fromEntity(track));
        }

        return new GetPlaylistResponse(playlist.getId(), playlist.getName(), playlist.getOwner().getId().equals(currentUserId), tracks);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getOwner() {
        return owner;
    }

    public List<GetTrackResponse> getTracks() {
        return tracks;
    }
}
