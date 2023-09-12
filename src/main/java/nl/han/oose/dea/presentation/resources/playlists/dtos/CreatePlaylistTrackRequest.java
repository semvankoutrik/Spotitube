package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.PlaylistTrack;

import java.util.Date;

public class CreatePlaylistTrackRequest {
    private String id;
    private String title;
    private String performer;
    private int duration;
    private int playcount;
    private Date publicationDate;
    private String description;
    private boolean offlineAvailable;

    public PlaylistTrack mapToEntity() {
        PlaylistTrack track = new PlaylistTrack();

        track.setId(id);
        track.setTitle(title);
        track.setPerformer(performer);
        track.setDuration(duration);
        track.setPlaycount(playcount);
        track.setPublicationDate(publicationDate);
        track.setDescription(description);
        track.setOfflineAvailable(offlineAvailable);

        return track;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
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

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
