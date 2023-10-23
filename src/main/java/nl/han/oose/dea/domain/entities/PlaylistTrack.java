package nl.han.oose.dea.domain.entities;

import java.util.Date;

public class PlaylistTrack extends Track {
    public PlaylistTrack()
    {
    }

    public PlaylistTrack(String id, String title, String performer, int duration, int playcount, Date publicationDate, String description, boolean offlineAvailable) {
        this.setId(id);
        this.setTitle(title);
        this.setPerformer(performer);
        this.setPlaycount(playcount);
        this.setPublicationDate(publicationDate);
        this.setDescription(description);
        this.offlineAvailable = offlineAvailable;
    }

    private boolean offlineAvailable;

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
