package dk.easv.mrs.DAL;

import dk.easv.mrs.BE.Movie;
import java.util.List;

//the interface
public interface IMovieDataAccess {

    List<Movie> getAllMovies() throws Exception;

    Movie createMovie(Movie newMovie) throws Exception;

    void updateMovie(Movie movie) throws Exception;

    void deleteMovie(Movie movie) throws Exception;

    // In IMovieDataAccess interface - add this line
    void renumberMovies() throws Exception;

}