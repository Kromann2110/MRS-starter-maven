package dk.easv.mrs.GUI.Model;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.List;

// Manages movie data for the UI
public class MovieModel {
    private ObservableList<Movie> moviesToShowOnScreen;
    private FilteredList<Movie> filteredMovies;
    private MovieManager movieManager;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToShowOnScreen = FXCollections.observableArrayList();
        filteredMovies = new FilteredList<>(moviesToShowOnScreen);
        loadAllMoviesFromFile(); // Load initial data
    }

    // Provides data for ListView
    public ObservableList<Movie> getObservableMovies() {
        return filteredMovies;
    }

    // Reloads all movies from file
    private void loadAllMoviesFromFile() {
        try {
            List<Movie> allMoviesFromFile = movieManager.getAllMovies();
            moviesToShowOnScreen.clear();
            moviesToShowOnScreen.addAll(allMoviesFromFile); // Refresh UI list
        } catch (Exception e) {
            throw new RuntimeException("Could not load movies", e);
        }
    }

    // Filters movies based on search text
    public void searchMovie(String searchText) throws Exception {
        if (searchText == null || searchText.isEmpty()) {
            filteredMovies.setPredicate(movie -> true); // Show all movies
        } else {
            // Show only movies containing search text in title
            String query = searchText.toLowerCase();
            filteredMovies.setPredicate(movie ->
                    movie.getTitle().toLowerCase().contains(query)
            );
        }
    }

    //gives you the option to add/create a new movie for your list
    public void createMovie(Movie movie) throws Exception {
        movieManager.createMovie(movie); // Save to file
        loadAllMoviesFromFile(); // Refresh list
    }

    //gives you the option to update the list, when you add or delete a movie
    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie); // Update in file
        loadAllMoviesFromFile(); // Refresh list
    }

    //gives you the option to delete one or more movies
    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie); // Delete from file
        loadAllMoviesFromFile(); // Refresh list
    }

    // When you press the update button it changes the line-up to it matches the sequence of the id-numbers
    public void renumberMovies() throws Exception {
        movieManager.renumberMovies();
        loadAllMoviesFromFile(); // Refresh the list
    }
}