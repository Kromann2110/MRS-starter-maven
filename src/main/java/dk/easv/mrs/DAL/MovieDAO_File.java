package dk.easv.mrs.DAL;

import dk.easv.mrs.BE.Movie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.APPEND;

public class MovieDAO_File implements IMovieDataAccess {
    private static final String MOVIES_FILE = "data/My Movies.txt";

    public MovieDAO_File() {
        try {
            ensureFileExists();
            System.out.println("Movies file location: " + Paths.get(MOVIES_FILE).toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error accessing movies file: " + e.getMessage());
        }
    }

    private void ensureFileExists() throws Exception {
        Path path = Paths.get(MOVIES_FILE);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            System.out.println("Created new movies file at: " + path.toAbsolutePath());
        }
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<Movie> movies = new ArrayList<>();

        for (String line: lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",", 3);
            // REMOVED: No more checking for insufficient parts - will crash if less than 3

            // REMOVED: No more try-catch - will crash on NumberFormatException
            int id = Integer.parseInt(parts[0].trim());
            int year = Integer.parseInt(parts[1].trim());
            String title = parts[2].trim();

            movies.add(new Movie(id, year, title));
        }

        return movies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        ensureFileExists();
        List<Movie> allMovies = getAllMovies();

        int nextId = 1;
        if (!allMovies.isEmpty()) {
            Movie lastMovie = allMovies.get(allMovies.size() - 1);
            nextId = lastMovie.getId() + 1;
        }

        String newMovieLine = nextId + "," + newMovie.getYear() + "," + newMovie.getTitle();
        Files.write(Paths.get(MOVIES_FILE), (System.lineSeparator() + newMovieLine).getBytes(), APPEND);

        return new Movie(nextId, newMovie.getYear(), newMovie.getTitle());
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        ensureFileExists();

        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<String> updatedLines = new ArrayList<>();
        boolean movieFound = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split(",", 3);
            // REMOVED: No more length checking

            int currentId = Integer.parseInt(parts[0].trim());

            if (currentId == movie.getId()) {
                String updatedLine = movie.getId() + "," + movie.getYear() + "," + movie.getTitle();
                updatedLines.add(updatedLine);
                movieFound = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (!movieFound) {
            throw new Exception("Movie with ID " + movie.getId() + " not found for update");
        }

        Files.write(Paths.get(MOVIES_FILE), updatedLines);
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        ensureFileExists();

        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<String> updatedLines = new ArrayList<>();
        boolean movieFound = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split(",", 3);
            // REMOVED: No more length checking

            int currentId = Integer.parseInt(parts[0].trim());

            if (currentId != movie.getId()) {
                updatedLines.add(line);
            } else {
                movieFound = true;
            }
        }

        if (!movieFound) {
            throw new Exception("Movie with ID " + movie.getId() + " not found for deletion");
        }

        Files.write(Paths.get(MOVIES_FILE), updatedLines);
    }
}