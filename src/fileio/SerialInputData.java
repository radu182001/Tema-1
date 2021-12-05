package fileio;

import entertainment.Season;

import java.util.ArrayList;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }

    //Extra
    private int no_favorite = 0;

    private int duration = 0;

    private int no_views = 0;

    public double getRating(){
        int res = 0;
        for(Season s : seasons)
            res += s.getRating();
        return res / (double)seasons.size();
    }

    public void incrementNo_favorite(){ no_favorite++; }

    public int getNo_favorite() { return no_favorite; }

    public void resetNo_favorite(){ no_favorite = 0; }

    public int getDuration(){
        for (Season s : seasons)
            duration += s.getDuration();
        return duration;
    }

    public void incrementNo_views(int x){ no_views += x; }

    public int getNo_views(){ return no_views; }

    public void resetNo_views(){ no_views = 0; }
}
