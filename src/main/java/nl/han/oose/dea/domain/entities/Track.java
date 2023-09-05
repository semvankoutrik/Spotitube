package nl.han.oose.dea.domain.entities;

import nl.han.oose.dea.domain.shared.EntityBase;

public class Track extends EntityBase {
    private String performer;
    private String title;
    private int duration;

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
