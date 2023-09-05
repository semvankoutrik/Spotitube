package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;

public class GetPlaylistResponse {
    private String id;
    private String name;
    private Boolean owner;
    // TODO: Add tracks

    public static GetPlaylistResponse fromEntity(Playlist playlist, String currentUserId) {
        GetPlaylistResponse response = new GetPlaylistResponse();

        response.id = playlist.getId();
        response.name = playlist.getName();
        response.owner = playlist.getOwner().getId().equals(currentUserId);

        return response;
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
}
