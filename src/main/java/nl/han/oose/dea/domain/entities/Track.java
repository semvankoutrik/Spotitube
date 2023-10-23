package nl.han.oose.dea.domain.entities;

import nl.han.oose.dea.domain.shared.EntityBase;

import java.util.Date;

public class Track extends EntityBase {
    public Track()
    {
    }

    public Track(String id, String title, String performer, int duration, int playcount, Date publicationDate, String description) {
        this.setId(id);
        this.title = title;
        this.performer = performer;
        this.playcount = playcount;
        this.publicationDate = publicationDate;
        this.description = description;
    }

    private String title;
    private String performer;
    private int duration;
    private int playcount;
    private Date publicationDate;
    private String description;

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

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }
}
