package actions;

import entertainment.Season;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.simple.JSONArray;

import java.util.List;

public class Command {

    private UserInputData user;
    private ActionInputData action;

    public Command(List<UserInputData> users, ActionInputData action){
        this.action = action;
        for(UserInputData user : users)
            if(user.getUsername().equals(action.getUsername())){
                this.user = user;
                break;
            }
    }

    public int favorite(List<MovieInputData> movies){
        if(user.getHistory().containsKey(action.getTitle())){
            if(user.getFavoriteMovies().contains(action.getTitle()))
                return 1;
            else{
                user.getFavoriteMovies().add(action.getTitle());
                for (MovieInputData m : movies)
                    if (m.getTitle().equals(action.getTitle()))
                        m.incrementNo_favorite();
                return 2;
            }
        }
        return 0;
    }

    public int view(){
        String title = action.getTitle();
        if(user.getHistory().containsKey(title)) {
            int no_views = user.getHistory().get(title);
            user.getHistory().replace(title, ++no_views);
            return no_views;
        }else {
            user.getHistory().put(title, 1);
            return 1;
        }
    }

    public double rating_movie(List<MovieInputData> movies){
        double grade = action.getGrade();
        String title = action.getTitle();
        for (MovieInputData movie : movies)
            if (movie.getTitle().equals(title)){
                if (!user.getHistory().containsKey(action.getTitle())) return -1;
                if (user.getAlready_rated().contains(movie.getTitle())) return -2;
                movie.getRatings().add(grade);
                user.incrementNo_ratings();
                user.getAlready_rated().add(movie.getTitle());
                break;
            }
        return grade;
    }

    public double rating_serial(List<SerialInputData> serials){
        double grade = action.getGrade();
        String title = action.getTitle();
        int season_no = action.getSeasonNumber();
        for(SerialInputData serial : serials)
            if(serial.getTitle().equals(title)){
                if(!user.getHistory().containsKey(action.getTitle())) return -1;
                if (user.getAlready_rated().contains(serial.getTitle() + season_no)) return -2;
                serial.getSeasons().get(season_no - 1).getRatings().add(grade);
                user.incrementNo_ratings();
                user.getAlready_rated().add(serial.getTitle() + season_no);
                break;
            }
        return grade;
    }

    public UserInputData getUser() {
        return user;
    }

    public ActionInputData getAction() {
        return action;
    }
}
