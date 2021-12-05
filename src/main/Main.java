package main;

import actions.Query;
import actions.Recommendation;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import actions.Command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        for (int i = 0; i < input.getCommands().size(); i++) {
            ActionInputData action = input.getCommands().get(i);
            String message = "";
            if (action.getActionType().equals("command")) {
                Command command = new Command(input.getUsers(), action);
                String title = command.getAction().getTitle();
                if (command.getAction().getType().equals("favorite")) {
                    int result_fav = command.favorite(input.getMovies());
                    if (result_fav == 0)
                        message = "error -> " + title + " is not seen";
                    else if (result_fav == 1)
                        message = "error -> " + title + " is already in favourite list";
                    else if (result_fav == 2)
                        message = "success -> " + title + " was added as favourite";
                } else if (command.getAction().getType().equals("view"))
                    message = "success -> " + title + " was viewed with total views of " + command.view();
                else if (command.getAction().getType().equals("rating")) {
                    double rating;
                    if (input.getCommands().get(i).getSeasonNumber() > 0)
                        rating = command.rating_serial(input.getSerials());
                    else rating = command.rating_movie(input.getMovies());
                    if (rating != -1 && rating != -2)
                        message = "success -> " + title + " was rated with " + rating + " by " + command.getUser().getUsername();
                    else if (rating == -1) message = "error -> " + title + " is not seen";
                    else message = "error -> " + title + " has been already rated";
                }
            }
            else if (action.getActionType().equals("query")){
                Query query = new Query(input.getUsers(), action);
                if (action.getCriteria().equals("num_ratings"))
                    message = "Query result: " + query.no_ratings(input.getUsers(), action.getSortType(), action.getNumber()).toString();
                else if (action.getObjectType().equals("movies")){
                    if (action.getCriteria().equals("ratings"))
                        message = "Query result: " + query.rating_movie(input.getMovies(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    else if (action.getCriteria().equals("favorite")) {
                        query.set_favorites(input.getUsers(), input.getMovies(), input.getSerials());
                        message = "Query result: " + query.favorite_movie(input.getMovies(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    }
                    else if (action.getCriteria().equals("longest"))
                        message = "Query result: " + query.longest_movie(input.getMovies(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    else if (action.getCriteria().equals("most_viewed")) {
                        query.set_views(input.getUsers(), input.getMovies(), input.getSerials());
                        message = "Query result: " + query.most_viewed_movie(input.getMovies(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    }
                }
                else if (action.getObjectType().equals("shows")){
                    if (action.getCriteria().equals("ratings"))
                        message = "Query result: " + query.rating_serial(input.getSerials(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    else if (action.getCriteria().equals("favorite")) {
                        query.set_favorites(input.getUsers(), input.getMovies(), input.getSerials());
                        message = "Query result: " + query.favorite_serial(input.getSerials(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    }
                    else if (action.getCriteria().equals("longest"))
                        message = "Query result: " + query.longest_serial(input.getSerials(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    else if (action.getCriteria().equals("most_viewed")) {
                        query.set_views(input.getUsers(), input.getMovies(), input.getSerials());
                        message = "Query result: " + query.most_viewed_serial(input.getSerials(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    }
                }
                else if (action.getObjectType().equals("actors")){
                    if (action.getCriteria().equals("average")){
                        query.set_average_rating(input.getActors(), input.getMovies(), input.getSerials());
                        message = "Query result: " + query.average(input.getActors(), action.getSortType(), action.getNumber()).toString();
                    }
                    else if (action.getCriteria().equals("awards")){
                        message = "Query result: " + query.awards(input.getActors(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                    }
                    else if (action.getCriteria().equals("filter_description"))
                        message = "Query result: " + query.filter_description(input.getActors(), action.getSortType(), action.getNumber(), action.getFilters()).toString();
                }
            }
            else if (action.getActionType().equals("recommendation")){
                Recommendation recommendation = new Recommendation(input.getUsers(), action);
                if (action.getType().equals("standard")) {
                    String result = recommendation.standard(input.getMovies(), input.getSerials());
                    if (!result.equals(""))
                        message = "StandardRecommendation result: " + result;
                    else message = "StandardRecommendation cannot be applied!";
                }
                else if (action.getType().equals("best_unseen")) {
                    String result = recommendation.best_unseen(input.getMovies(), input.getSerials());
                    if (!result.equals(""))
                        message = "BestRatedUnseenRecommendation result: " + result;
                    else message = "BestRatedUnseenRecommendation cannot be applied!";
                }
                else if (action.getType().equals("popular")){
                    String result = recommendation.popular(input.getMovies(), input.getSerials());
                    if (recommendation.check_premium() && !result.equals(""))
                        message = "PopularRecommendation result: " + result;
                    else message = "PopularRecommendation cannot be applied!";
                }
                else if (action.getType().equals("favorite")){
                    String result = recommendation.favorite(input.getMovies(), input.getSerials());
                    if (recommendation.check_premium() && !result.equals(""))
                        message = "FavoriteRecommendation result: " + result;
                    else message = "FavoriteRecommendation cannot be applied!";
                }
                else if (action.getType().equals("search")){
                    List<String> result = recommendation.search(input.getMovies(), input.getSerials(), action.getGenre());
                    if (recommendation.check_premium() && !result.isEmpty())
                        message = "SearchRecommendation result: " + result;
                    else message = "SearchRecommendation cannot be applied!";
                }
            }
            arrayResult.add(fileWriter.writeFile(input.getCommands().get(i).getActionId(), "", message));
        }


        fileWriter.closeJSON(arrayResult);
    }
}
