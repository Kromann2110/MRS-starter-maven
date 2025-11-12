package dk.easv.mrs.BLL.util;
import dk.easv.mrs.BE.Movie;
import java.util.ArrayList;
import java.util.List;

// Searches movies by title or year
public class MovieSearcher {
    public List<Movie> search(List<Movie> searchBase, String query) {
        List<Movie> searchResult = new ArrayList<>();
        // Check each movie against search query
        for (Movie movie : searchBase) {
            if(compareToMovieTitle(query, movie) || compareToMovieYear(query, movie)) {
                searchResult.add(movie); // Add matches to results
            }
        }
        return searchResult;
    }

    private boolean compareToMovieYear(String query, Movie movie) {
        // Convert year to string for text search
        return Integer.toString(movie.getYear()).contains(query);
    }

    private boolean compareToMovieTitle(String query, Movie movie) {
        // Case-insensitive title search
        return movie.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}