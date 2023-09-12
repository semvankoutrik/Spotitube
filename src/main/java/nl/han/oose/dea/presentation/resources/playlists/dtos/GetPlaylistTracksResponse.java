package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.PlaylistTrack;

import java.util.List;

public class GetPlaylistTracksResponse {
    private List<GetPlaylistTrackResponse> tracks;

    public static GetPlaylistTracksResponse fromEntity(List<PlaylistTrack> tracks) {
        GetPlaylistTracksResponse response = new GetPlaylistTracksResponse();

        response.tracks = tracks.stream().map(GetPlaylistTrackResponse::fromEntity).toList();

        return response;
    }

    public List<GetPlaylistTrackResponse> getTracks() {
        return tracks;
    }

    public void setTracks(List<GetPlaylistTrackResponse> tracks) {
        this.tracks = tracks;
    }
}
