package HugoVanDerWel.models;

import java.util.Date;

public class TrackModel {

    public TrackModel(){
    }
    public int id;
    public String title;
    public String performer;
    public int duration;
    public String album;
    public int playcount;
    public Date publicationDate;
    public String description;
    public boolean offlineAvailable;
}
