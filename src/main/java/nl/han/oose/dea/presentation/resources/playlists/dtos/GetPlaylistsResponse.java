package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistsResponse {
    private final List<GetPlaylistResponse> playlists = new ArrayList<>();
    private int length;

    public static GetPlaylistsResponse fromEntity(List<Playlist> playlists, String currentUserId) {
        GetPlaylistsResponse response = new GetPlaylistsResponse();

        int length = 0;

        for (Playlist playlist : playlists) {
            response.addPlaylist(playlist, currentUserId);

            for (Track t : playlist.getTracks()) {
                length += t.getDuration();
            }
        }

        response.length = length;

        return response;
    }

    public void addPlaylist(Playlist playlist, String currentUserId) {
        this.playlists.add(GetPlaylistResponse.fromEntity(playlist, currentUserId));
    }

    public List<GetPlaylistResponse> getPlaylists() {
        return playlists;
    }

    public int getLength() {
        return length;
    }
}
