package nl.han.oose.dea.presentation.resources.tracks.dtos;

import nl.han.oose.dea.domain.entities.Track;

import java.util.Date;

public class GetTrackResponse {
    private String title;
    private String performer;
    private int duration;
    private Date publicationDate;
    private String description;
    private int playcount;

    public GetTrackResponse(String performer, String title, int duration, int playcount) {
        this.performer = performer;
        this.title = title;
        this.duration = duration;
        this.playcount = playcount;
    }

    public static GetTrackResponse fromEntity(Track track) {
        return new GetTrackResponse(track.getPerformer(), track.getTitle(), track.getDuration(), track.getPlaycount());
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
