package nl.han.oose.dea.presentation.resources.tracks.dtos;

import nl.han.oose.dea.domain.entities.Track;

public class GetTrackResponse {
    private String performer;
    private String title;
    private int duration;

    public GetTrackResponse(String performer, String title, int duration) {
        this.performer = performer;
        this.title = title;
        this.duration = duration;
    }

    public static GetTrackResponse fromEntity(Track track) {
        return new GetTrackResponse(track.getPerformer(), track.getTitle(), track.getDuration());
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
}
