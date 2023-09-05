package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistsResponse {
    private List<GetPlaylistResponse> playlists = new ArrayList<>();
    private int length;

    public static GetPlaylistsResponse fromEntity(List<Playlist> playlists, String currentUserId) {
        GetPlaylistsResponse response = new GetPlaylistsResponse();

        for (Playlist playlist : playlists) {
            response.addPlaylist(playlist, currentUserId);
        }

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
