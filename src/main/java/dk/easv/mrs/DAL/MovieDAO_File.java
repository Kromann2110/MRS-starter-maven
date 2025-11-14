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
    private static final String MOVIES_FILE = "data/My Movies.txt"; // Relative path

    // Add this constructor to debug file location
    public MovieDAO_File() {
        try {
            ensureFileExists();
            System.out.println("Movies file location: " + Paths.get(MOVIES_FILE).toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error accessing movies file: " + e.getMessage());
        }
    }

    // Makes sure file exists before operations
    private void ensureFileExists() throws Exception {
        Path path = Paths.get(MOVIES_FILE);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent()); // Create folders if needed
            Files.createFile(path); // Create file
            System.out.println("Created new movies file at: " + path.toAbsolutePath());
        }
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<Movie> movies = new ArrayList<>();

        System.out.println("DEBUG: Reading " + lines.size() + " lines from file");

        for (String line: lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",", 3); // Split into max 3 parts
            if (parts.length < 3) {
                System.out.println("DEBUG: Skipping invalid line: " + line);
                continue;
            }

            try {
                int id = Integer.parseInt(parts[0].trim());
                int year = Integer.parseInt(parts[1].trim());
                String title = parts[2].trim();

                movies.add(new Movie(id, year, title));
                System.out.println("DEBUG: Loaded movie - ID: " + id + ", Year: " + year + ", Title: " + title);
            } catch (NumberFormatException e) {
                System.err.println("DEBUG: Error parsing line: " + line + " - " + e.getMessage());
            }
        }

        System.out.println("DEBUG: Successfully loaded " + movies.size() + " movies");
        return movies;
    }

    // Create button - FIXED
    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        ensureFileExists();
        List<Movie> allMovies = getAllMovies(); // Use our own method to get current movies

        // Generate next ID (last movie's ID + 1)
        int nextId = 1;
        if (!allMovies.isEmpty()) {
            Movie lastMovie = allMovies.get(allMovies.size() - 1);
            nextId = lastMovie.getId() + 1;
        }

        // Append new movie to file
        String newMovieLine = nextId + "," + newMovie.getYear() + "," + newMovie.getTitle();
        Files.write(Paths.get(MOVIES_FILE), (System.lineSeparator() + newMovieLine).getBytes(), APPEND);

        System.out.println("DEBUG: Created new movie: " + nextId + "," + newMovie.getYear() + "," + newMovie.getTitle());

        return new Movie(nextId, newMovie.getYear(), newMovie.getTitle());
    }

    // Update button - FIXED
    @Override
    public void updateMovie(Movie movie) throws Exception {
        System.out.println("DEBUG: Updating movie: " + movie.getId() + ", " + movie.getTitle() + ", " + movie.getYear());
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
            if (parts.length >= 3) {
                try {
                    int currentId = Integer.parseInt(parts[0].trim());

                    if (currentId == movie.getId()) {
                        // Replace with updated movie
                        String updatedLine = movie.getId() + "," + movie.getYear() + "," + movie.getTitle();
                        updatedLines.add(updatedLine);
                        movieFound = true;
                        System.out.println("DEBUG: Updated line: " + updatedLine);
                    } else {
                        updatedLines.add(line);
                    }
                } catch (NumberFormatException e) {
                    updatedLines.add(line); // Keep invalid lines as-is
                }
            } else {
                updatedLines.add(line); // Keep invalid lines as-is
            }
        }

        if (!movieFound) {
            throw new Exception("Movie with ID " + movie.getId() + " not found for update");
        }

        // Write all lines back
        Files.write(Paths.get(MOVIES_FILE), updatedLines);
        System.out.println("DEBUG: Movie update completed successfully");
    }

    // Delete button - FIXED
    @Override
    public void deleteMovie(Movie movie) throws Exception {
        System.out.println("DEBUG: Deleting movie: " + movie.getId());
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
            if (parts.length >= 3) {
                try {
                    int currentId = Integer.parseInt(parts[0].trim());

                    if (currentId != movie.getId()) {
                        updatedLines.add(line);
                    } else {
                        movieFound = true;
                        System.out.println("DEBUG: Found and removed movie: " + line);
                    }
                } catch (NumberFormatException e) {
                    updatedLines.add(line); // Keep invalid lines
                }
            } else {
                updatedLines.add(line); // Keep invalid lines
            }
        }

        if (!movieFound) {
            throw new Exception("Movie with ID " + movie.getId() + " not found for deletion");
        }

        Files.write(Paths.get(MOVIES_FILE), updatedLines);
        System.out.println("DEBUG: Movie deletion completed. " + (lines.size() - updatedLines.size()) + " movie(s) removed");
    }
}