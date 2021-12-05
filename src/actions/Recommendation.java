package actions;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Recommendation {

    private UserInputData user;
    private ActionInputData action;

    public Recommendation(List<UserInputData> users, ActionInputData action){
        this.action = action;
        for (UserInputData user : users)
            if (user.getUsername().equals(action.getUsername())){
                this.user = user;
                break;
            }
    }

    public boolean check_premium(){
        return user.getSubscriptionType().equals("PREMIUM");
    }

    //All users
    public String standard(List<MovieInputData> movies, List<SerialInputData> shows){
        for (MovieInputData m : movies){
            if (!user.getHistory().containsKey(m.getTitle()))
                return m.getTitle();
        }
        for (SerialInputData s : shows){
            if (!user.getHistory().containsKey(s.getTitle()))
                return s.getTitle();
        }
        return "";
    }

    public String best_unseen(List<MovieInputData> movies, List<SerialInputData> shows){
        HashMap<String,Double> videos_map = new HashMap<>();
        List<String> videos_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (!user.getHistory().containsKey(m.getTitle())) {
                videos_map.put(m.getTitle(), m.getRating());
                videos_list.add(m.getTitle());
            }
        }
        for (SerialInputData s : shows){
            if (!user.getHistory().containsKey(s.getTitle())) {
                videos_map.put(s.getTitle(), s.getRating());
                videos_list.add(s.getTitle());
            }
        }
        videos_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (int) (videos_map.get(o2) - videos_map.get(o1));
            }
        });
        if (!videos_list.isEmpty())
            return videos_list.get(0);
        else return "";
    }

    //Premium users
    public String popular(List<MovieInputData> movies, List<SerialInputData> shows){
        HashMap<String,Integer> genres_map = new HashMap<>();
        List<String> genres_list = new ArrayList<>();
        for (MovieInputData m : movies)
            for (String genre : m.getGenres()){
                if (genres_map.containsKey(genre))
                    genres_map.replace(genre, genres_map.get(genre) + m.getNo_views());
                else {
                    genres_map.put(genre, 0);
                    genres_list.add(genre);
                }
            }

        for (SerialInputData s : shows)
            for (String genre : s.getGenres()){
                if (genres_map.containsKey(genre))
                    genres_map.replace(genre, genres_map.get(genre) + s.getNo_views());
                else {
                    genres_map.put(genre, 0);
                    genres_list.add(genre);
                }
            }

        genres_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return genres_map.get(o2) - genres_map.get(o1);
            }
        });

        for (String str : genres_list){
            for (MovieInputData m : movies){
                if (m.getGenres().contains(str))
                    if (!user.getHistory().containsKey(m.getTitle()))
                        return m.getTitle();
            }
            for (SerialInputData s : shows){
                if (s.getGenres().contains(str))
                    if (!user.getHistory().containsKey(s.getTitle()))
                        return s.getTitle();
            }
        }
        return "";
    }

    public String favorite(List<MovieInputData> movies, List<SerialInputData> shows){
        HashMap<String,Integer> videos_map = new HashMap<>();
        List<String> videos_list = new ArrayList<>();
        for (MovieInputData m : movies){
            videos_map.put(m.getTitle(), m.getNo_favorite());
            videos_list.add(m.getTitle());
        }
        for (SerialInputData s : shows){
            videos_map.put(s.getTitle(), s.getNo_favorite());
            videos_list.add(s.getTitle());
        }
        videos_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return videos_map.get(o2) - videos_map.get(o1);
            }
        });
        for (String str : videos_list){
            if (!user.getHistory().containsKey(str))
                return str;
        }
        return "";
    }

    public List<String> search(List<MovieInputData> movies, List<SerialInputData> shows, String genre){
        HashMap<String,Double> videos_map = new HashMap<>();
        List<String> videos_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (m.getGenres().contains(genre) && !user.getHistory().containsKey(m.getTitle())){
                videos_map.put(m.getTitle(), m.getRating());
                videos_list.add(m.getTitle());
            }
        }
        for (SerialInputData s : shows){
            if (s.getGenres().contains(genre) && !user.getHistory().containsKey(s.getTitle())){
                videos_map.put(s.getTitle(), s.getRating());
                videos_list.add(s.getTitle());
            }
        }
        videos_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (int)(videos_map.get(o1) - videos_map.get(o2));
                if (res == 0)
                    return o1.compareTo(o2);
                else return res;
            }
        });
        return videos_list;
    }
}
