package actions;

import actor.ActorsAwards;
import fileio.*;

import java.util.*;

public class Query {

    private UserInputData user;
    private ActionInputData action;

    public Query(List<UserInputData> users, ActionInputData action){
        this.action = action;
        for (UserInputData user : users)
            if (user.getUsername().equals(action.getUsername())){
                this.user = user;
                break;
            }
    }

    //Users
    public List<String> no_ratings(List<UserInputData> users, String sort, int N){
        HashMap<String,Integer> users_map = new HashMap<>();
        List<String> users_list = new ArrayList<>();
        for (UserInputData u : users){
            if(u.getNo_ratings() > 0) {
                users_map.put(u.getUsername(), u.getNo_ratings());
                users_list.add(u.getUsername());
            }
        }
        users_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = users_map.get(o1) - users_map.get(o2);
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = users_list.size();
        for (int i = size-1; i >= N; i--)
            users_list.remove(i);
        return users_list;
    }

    //Videos
    public boolean check_filters_movie(MovieInputData movie, List<List<String>> filters){
        int total = 0;
        boolean yes = false;
        int year = 0;
        int genre = 0;
        if (filters.get(0).get(0) != null){
            year = 1;
            if (Integer.parseInt(filters.get(0).get(0)) == movie.getYear()){
                yes = true;
            }
            if (yes)
                total++;
        }
        if(filters.get(1).get(0) != null){
            int c = 0;
            genre = 1;
            for (String s : filters.get(1))
                for(String g : movie.getGenres()){
                    if (s.equals(g))
                        c++;
                }
            if (c == filters.get(1).size())
                total++;
        }
        return total == (year + genre);
    }

    public List<String> rating_movie(List<MovieInputData> movies, String sort, int N, List<List<String>> filters){
        HashMap<String,Double> movies_map = new HashMap<>();
        List<String> movies_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (m.getRating() > 0){
                if (check_filters_movie(m, filters)) {
                    movies_map.put(m.getTitle(), m.getRating());
                    movies_list.add(m.getTitle());
                }
            }
        }
        movies_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (int)(movies_map.get(o1) - movies_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = movies_list.size();
        for (int i = size-1; i >= N; i--)
            movies_list.remove(i);
        return movies_list;
    }

    public boolean check_filters_serial(SerialInputData serial, List<List<String>> filters){
        int total = 0;
        boolean yes = false;
        int year = 0;
        int genre = 0;
        if (filters.get(0).get(0) != null){
            year = 1;
            if (Integer.parseInt(filters.get(0).get(0)) == serial.getYear()){
                yes = true;
            }
            if (yes)
                total++;
        }
        if(filters.get(1).get(0) != null){
            int c = 0;
            genre = 1;
            for (String s : filters.get(1))
                for(String g : serial.getGenres()){
                    if (s.equals(g))
                        c++;
                }
            if (c == filters.get(1).size())
                total++;
        }
        return total == (year + genre);
    }

    public List<String> rating_serial(List<SerialInputData> serials, String sort, int N, List<List<String>> filters){
        HashMap<String,Double> serials_map = new HashMap<>();
        List<String> serials_list = new ArrayList<>();
        for (SerialInputData s : serials){
            if (s.getRating() > 0){
                if (check_filters_serial(s, filters)){
                    serials_map.put(s.getTitle(), s.getRating());
                    serials_list.add(s.getTitle());
                }
            }
        }
        serials_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (int)(serials_map.get(o1) - serials_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = serials_list.size();
        for (int i = size-1; i >= N; i--)
            serials_list.remove(i);
        return serials_list;
    }

    public void set_favorites(List<UserInputData> users, List<MovieInputData> movies, List<SerialInputData> shows){
        for (MovieInputData m : movies)
            m.resetNo_favorite();
        for (SerialInputData s : shows)
            s.resetNo_favorite();

        for (UserInputData user : users)
            for (String fav : user.getFavoriteMovies()) {
                for (MovieInputData m : movies)
                    if (m.getTitle().equals(fav)) {
                        m.incrementNo_favorite();
                        break;
                    }
                for (SerialInputData s : shows)
                    if (s.getTitle().equals(fav)){
                        s.incrementNo_favorite();
                        break;
                    }
            }
    }

    public List<String> favorite_movie(List<MovieInputData> movies, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> movies_map = new HashMap<>();
        List<String> movies_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (m.getNo_favorite() > 0){
                if (check_filters_movie(m, filters)) {
                    movies_map.put(m.getTitle(), m.getNo_favorite());
                    movies_list.add(m.getTitle());
                }
            }
        }
        movies_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (movies_map.get(o1) - movies_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = movies_list.size();
        for (int i = size-1; i >= N; i--)
            movies_list.remove(i);
        return movies_list;
    }

    public List<String> favorite_serial(List<SerialInputData> serials, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> serials_map = new HashMap<>();
        List<String> serials_list = new ArrayList<>();
        for (SerialInputData s : serials){
            if (s.getNo_favorite() > 0){
                if (check_filters_serial(s, filters)) {
                    serials_map.put(s.getTitle(), s.getNo_favorite());
                    serials_list.add(s.getTitle());
                }
            }
        }
        serials_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (serials_map.get(o1) - serials_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = serials_list.size();
        for (int i = size-1; i >= N; i--)
            serials_list.remove(i);
        return serials_list;
    }

    public List<String> longest_movie(List<MovieInputData> movies, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> movies_map = new HashMap<>();
        List<String> movies_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (m.getDuration() > 0){
                if (check_filters_movie(m, filters)) {
                    movies_map.put(m.getTitle(), m.getDuration());
                    movies_list.add(m.getTitle());
                }
            }
        }
        movies_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (movies_map.get(o1) - movies_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = movies_list.size();
        for (int i = size-1; i >= N; i--)
            movies_list.remove(i);
        return movies_list;
    }

    public List<String> longest_serial(List<SerialInputData> serials, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> serials_map = new HashMap<>();
        List<String> serials_list = new ArrayList<>();
        for (SerialInputData s : serials){
            if (s.getDuration() > 0){
                if (check_filters_serial(s, filters)) {
                    serials_map.put(s.getTitle(), s.getDuration());
                    serials_list.add(s.getTitle());
                }
            }
        }
        serials_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (serials_map.get(o1) - serials_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = serials_list.size();
        for (int i = size-1; i >= N; i--)
            serials_list.remove(i);
        return serials_list;
    }

    public void set_views(List<UserInputData> users, List<MovieInputData> movies, List<SerialInputData> shows){
        for (MovieInputData m : movies) {
            m.resetNo_views();
            for (UserInputData u : users)
                if (u.getHistory().containsKey(m.getTitle()))
                    m.incrementNo_views(u.getHistory().get(m.getTitle()));
        }

        for (SerialInputData s : shows) {
            s.resetNo_views();
            for (UserInputData u : users)
                if (u.getHistory().containsKey(s.getTitle()))
                    s.incrementNo_views(u.getHistory().get(s.getTitle()));
        }
    }

    public List<String> most_viewed_movie(List<MovieInputData> movies, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> movies_map = new HashMap<>();
        List<String> movies_list = new ArrayList<>();
        for (MovieInputData m : movies){
            if (m.getNo_views() > 0){
                if (check_filters_movie(m, filters)) {
                    movies_map.put(m.getTitle(), m.getNo_views());
                    movies_list.add(m.getTitle());
                }
            }
        }
        movies_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (movies_map.get(o1) - movies_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = movies_list.size();
        for (int i = size-1; i >= N; i--)
            movies_list.remove(i);
        return movies_list;
    }

    public List<String> most_viewed_serial(List<SerialInputData> serials, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> serials_map = new HashMap<>();
        List<String> serials_list = new ArrayList<>();
        for (SerialInputData s : serials){
            if (s.getNo_views() > 0){
                if (check_filters_serial(s, filters)) {
                    serials_map.put(s.getTitle(), s.getNo_views());
                    serials_list.add(s.getTitle());
                }
            }
        }
        serials_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (serials_map.get(o1) - serials_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = serials_list.size();
        for (int i = size-1; i >= N; i--)
            serials_list.remove(i);
        return serials_list;
    }

    //Actors
    public void set_average_rating(List<ActorInputData> actors, List<MovieInputData> movies, List<SerialInputData> shows){
        for (ActorInputData a : actors){
            double size = 0;
            a.resetSum_rating();
            for (String video : a.getFilmography()) {
                for (MovieInputData m : movies)
                    if (m.getTitle().equals(video))
                        if (m.getRating() > 0) {
                            a.incrementSum_rating(m.getRating());
                            size++;
                        }
                for (SerialInputData s : shows)
                    if (s.getTitle().equals(video))
                        if (s.getRating() > 0) {
                            a.incrementSum_rating(s.getRating());
                            size++;
                        }
            }
            a.setSize(size);
        }
    }

    public List<String> average(List<ActorInputData> actors, String sort, int N){
        HashMap<String,Double> actors_map = new HashMap<>();
        List<String> actors_list = new ArrayList<>();
        for (ActorInputData a : actors){
            if (a.getAverage_rating() > 0){
                actors_map.put(a.getName(), a.getAverage_rating());
                actors_list.add(a.getName());
            }
        }
        actors_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = (int)(actors_map.get(o1) - actors_map.get(o2));
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        int size = actors_list.size();
        for (int i = size-1; i >= N; i--)
            actors_list.remove(i);
        return actors_list;
    }

    public int check_filters_awards(ActorInputData actor, List<List<String>> filters){
        int awards = 0;
        int check = 0;
        for (String award : filters.get(3)){
            if (actor.getAwards().containsKey(ActorsAwards.valueOf(award))){
                check++;
            }
        }
        if (check != filters.get(3).size())
            return -1;
        else {
            for (Map.Entry<ActorsAwards,Integer> e : actor.getAwards().entrySet())
                awards += e.getValue();
        }
        return awards;
    }

    public List<String> awards(List<ActorInputData> actors, String sort, int N, List<List<String>> filters){
        HashMap<String,Integer> actors_map = new HashMap<>();
        List<String> actors_list = new ArrayList<>();
        for (ActorInputData a : actors){
            int awards = check_filters_awards(a, filters);
            if (awards != -1){
                actors_map.put(a.getName(), awards);
                actors_list.add(a.getName());
            }
        }
        actors_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = actors_map.get(o1) - actors_map.get(o2);
                if (sort.equals("asc")) {
                    if (res == 0)
                        return o1.compareTo(o2);
                    else return res;
                }
                else {
                    if (sort.equals("desc")){
                        if (res == 0)
                            return -o1.compareTo(o2);
                        else return -res;
                    }
                }
                return res;
            }
        });
        return actors_list;
    }

    public boolean check_filters_words(ActorInputData actor, List<List<String>> filters){
        int check = 0;
        String desrciption = actor.getCareerDescription().toLowerCase(Locale.ROOT);
        for (String word : filters.get(2)){
            if (desrciption.contains(word + " "))
                check++;
        }
        return check == filters.get(2).size();
    }

    public List<String> filter_description(List<ActorInputData> actors, String sort, int N, List<List<String>> filters){
        List<String> actors_list = new ArrayList<>();
        for (ActorInputData a : actors){
            if (check_filters_words(a, filters))
                actors_list.add(a.getName());
        }
        actors_list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (sort.equals("asc")) {
                    return o1.compareTo(o2);
                }
                else if (sort.equals("desc")){
                    return -o1.compareTo(o2);
                }
                return o1.compareTo(o2);
            }
        });
        int size = actors_list.size();
        for (int i = size-1; i >= N; i--)
            actors_list.remove(i);
        return actors_list;
    }
}
