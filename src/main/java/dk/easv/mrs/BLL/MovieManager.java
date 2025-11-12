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

    //list of the movies
    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }
    // Search button
    public List<Movie> searchMovies(String query) throws Exception {
        // Get all movies first, then filter
        List<Movie> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    //Create button
    public Movie createMovie(Movie newMovie) throws Exception {
        return movieDAO.createMovie(newMovie); // Data layer handles ID generation
    }

    // delete button
    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
    }

    // Update button
    public void updateMovie(Movie movie) throws Exception {
        movieDAO.updateMovie(movie);
    }

    // when you press update it will sequence the id's
    public void renumberMovies() throws Exception {
        movieDAO.renumberMovies();
    }
}