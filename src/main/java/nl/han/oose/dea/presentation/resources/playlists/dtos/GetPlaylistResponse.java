package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;

import java.util.ArrayList;
import java.util.List;

public record GetPlaylistResponse(String id, String name, Boolean owner, List<GetTrackResponse> tracks) {

    public static GetPlaylistResponse fromEntity(Playlist playlist, String currentUserId) {
        List<GetTrackResponse> tracks = new ArrayList<>();

        for (Track track : playlist.getTracks()) {
            tracks.add(GetTrackResponse.fromEntity(track));
        }

        return new GetPlaylistResponse(playlist.getId(), playlist.getName(), playlist.getOwner().getId().equals(currentUserId), tracks);
    }
}
