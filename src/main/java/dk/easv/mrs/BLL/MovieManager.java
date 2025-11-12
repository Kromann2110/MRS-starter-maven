package dk.easv.mrs.BLL;
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.util.MovieSearcher;
import dk.easv.mrs.DAL.IMovieDataAccess;
import dk.easv.mrs.DAL.MovieDAO_File;
import java.util.List;

// Handles movie operations - bridge between GUI and data layer
public class MovieManager {
    private MovieSearcher movieSearcher = new MovieSearcher();
    private IMovieDataAccess movieDAO;

    public MovieManager() {
        movieDAO = new MovieDAO_File(); // Uses file storage
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies(); // Delegate to data layer
    }

    public List<Movie> searchMovies(String query) throws Exception {
        // Get all movies first, then filter
        List<Movie> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        return movieDAO.createMovie(newMovie); // Data layer handles ID generation
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie); // Delegate to data layer
    }

    public void updateMovie(Movie movie) throws Exception {
        movieDAO.updateMovie(movie); // Delegate to data layer
    }

    // In MovieManager class - add this method
    public void renumberMovies() throws Exception {
        movieDAO.renumberMovies();
    }
}