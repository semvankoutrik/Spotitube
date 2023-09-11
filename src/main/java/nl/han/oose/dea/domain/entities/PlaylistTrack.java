package nl.han.oose.dea.domain.entities;

public class PlaylistTrack extends Track {
    private boolean offlineAvailable;

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
