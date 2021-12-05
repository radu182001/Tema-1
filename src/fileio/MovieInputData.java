package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }

    //Extra
    private List<Double> ratings = new ArrayList<Double>();
    private int no_favorite = 0;
    private int no_views = 0;

    public List<Double> getRatings() {
        return ratings;
    }

    public double getRating(){
        int res = 0;
        for(double rating : ratings)
            res += rating;
        return res / (double)ratings.size();
    }

    public void incrementNo_favorite(){ no_favorite++; }

    public int getNo_favorite() { return no_favorite; }

    public void resetNo_favorite(){ no_favorite = 0; }

    public void incrementNo_views(int x){ no_views += x; }

    public int getNo_views(){ return no_views; }

    public void resetNo_views(){ no_views = 0; }
}
