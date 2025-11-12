package dk.easv.mrs.DAL;
import dk.easv.mrs.BE.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_Mock implements IMovieDataAccess {

    private List<Movie> allMovies;

    public MovieDAO_Mock()
    {
        allMovies = new ArrayList<>();
        allMovies.add(new Movie(1, 1991,"Terminator 2"));
        allMovies.add(new Movie(2, 2001,"Harry Potter and the Sorcerer´s Stone"));
        allMovies.add(new Movie(3, 2010, "Inception"));
        allMovies.add(new Movie(4, 2010, "The Godfather"));
        allMovies.add(new Movie(5, 2010, "The Lord of the Rings"));
    }

    @Override
    public List<Movie> getAllMovies() {
        return allMovies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {

    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {

    }

    // Give the option to update the movie list
    @Override
    public void renumberMovies() throws Exception {
        // For mock data, just reassign sequential IDs to all movies
        for (int i = 0; i < allMovies.size(); i++) {
            Movie movie = allMovies.get(i);
            // Create new movie with sequential ID but same title/year
            allMovies.set(i, new Movie(i + 1, movie.getYear(), movie.getTitle()));
        }
    }
}