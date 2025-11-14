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
        List<Movie> allMoviesFromFile = movieManager.getAllMovies();
        moviesToShowOnScreen.clear();
        moviesToShowOnScreen.addAll(allMoviesFromFile);
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
        movieManager.createMovie(movie);
        loadAllMoviesFromFile();
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        loadAllMoviesFromFile();
    }

    public void updateMovie(Movie movieToBeUpdated) throws Exception {
        movieManager.updateMovie(movieToBeUpdated);
        loadAllMoviesFromFile();
    }
}