package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.presentation.resources.tracks.dtos.GetTrackResponse;

import java.util.ArrayList;
import java.util.List;

public record GetPlaylistResponse(String id, String name, Boolean owner, List<GetPlaylistTrackResponse> tracks) {

    public static GetPlaylistResponse fromEntity(Playlist playlist, String currentUserId) {
        List<GetPlaylistTrackResponse> tracks = new ArrayList<>();

        if (playlist.getTracks() != null) {
            for (PlaylistTrack track : playlist.getTracks()) {
                tracks.add(GetPlaylistTrackResponse.fromEntity(track));
            }
        }

        return new GetPlaylistResponse(playlist.getId(), playlist.getName(), playlist.getOwner().getId().equals(currentUserId), tracks);
    }
}
