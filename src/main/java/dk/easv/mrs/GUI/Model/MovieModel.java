package dk.easv.mrs.GUI.Model;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.List;

public class MovieModel {
    private ObservableList<Movie> moviesToShowOnScreen;
    private FilteredList<Movie> filteredMovies;
    private MovieManager movieManager;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToShowOnScreen = FXCollections.observableArrayList();
        filteredMovies = new FilteredList<>(moviesToShowOnScreen);
        loadAllMoviesFromFile();
    }

    public ObservableList<Movie> getObservableMovies() {
        return filteredMovies;
    }

    private void loadAllMoviesFromFile() throws Exception {
        try {
            List<Movie> allMoviesFromFile = movieManager.getAllMovies();
            moviesToShowOnScreen.clear();
            moviesToShowOnScreen.addAll(allMoviesFromFile);
            System.out.println("DEBUG: Loaded " + allMoviesFromFile.size() + " movies into model");
        } catch (Exception e) {
            System.err.println("ERROR: Could not load movies: " + e.getMessage());
            throw new Exception("Could not load movies from file", e);
        }
    }

    public void searchMovie(String searchText) throws Exception {
        if (searchText == null || searchText.isEmpty()) {
            filteredMovies.setPredicate(movie -> true);
        } else {
            String query = searchText.toLowerCase();
            filteredMovies.setPredicate(movie ->
                    movie.getTitle().toLowerCase().contains(query) ||
                            String.valueOf(movie.getYear()).contains(query)
            );
        }
    }

    public void createMovie(Movie movie) throws Exception {
        Movie createdMovie = movieManager.createMovie(movie);
        System.out.println("DEBUG: Created movie in model: " + createdMovie);
        loadAllMoviesFromFile(); // Reload to reflect changes
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        System.out.println("DEBUG: Deleted movie in model: " + movie);
        loadAllMoviesFromFile(); // Reload to reflect changes
    }

    public void updateMovie(Movie movieToBeUpdated) throws Exception {
        movieManager.updateMovie(movieToBeUpdated);
        System.out.println("DEBUG: Updated movie in model: " + movieToBeUpdated);
        loadAllMoviesFromFile(); // Refresh the list
    }
}