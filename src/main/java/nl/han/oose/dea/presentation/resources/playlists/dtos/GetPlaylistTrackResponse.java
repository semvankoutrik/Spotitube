package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.Track;

import java.util.Date;

public class GetPlaylistTrackResponse {
    private String title;
    private String performer;
    private int duration;
    private int playcount;
    private Date publicationDate;
    private String description;
    private boolean offlineAvailable;

    public GetPlaylistTrackResponse(String performer, String title, int duration, int playcount, boolean offlineAvailable) {
        this.performer = performer;
        this.title = title;
        this.duration = duration;
        this.offlineAvailable = offlineAvailable;
    }

    public static GetPlaylistTrackResponse fromEntity(PlaylistTrack track) {
        return new GetPlaylistTrackResponse(track.getPerformer(), track.getTitle(), track.getDuration(), track.getPlaycount(), track.isOfflineAvailable());
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }
}
