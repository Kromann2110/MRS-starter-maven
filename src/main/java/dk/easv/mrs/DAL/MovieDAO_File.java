package dk.easv.mrs.DAL;
import dk.easv.mrs.BE.Movie;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.StandardOpenOption.APPEND;

// Stores movies in text file (format: id,year,title)
public class MovieDAO_File implements IMovieDataAccess {
    private static final String MOVIES_FILE = "C:\\text\\My Movies.txt";

    // Makes sure file exists before operations
    private void ensureFileExists() throws Exception {
        Path path = Paths.get(MOVIES_FILE);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent()); // Create folders
            Files.createFile(path); // Create file
        }
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<Movie> movies = new ArrayList<>();

        for (String line: lines) {
            if (line.trim().isEmpty()) continue; // Skip empty lines

            String[] parts = line.split(",");
            if (parts.length < 3) continue; // Skip invalid lines

            // Parse CSV line: id,year,title
            int id = Integer.parseInt(parts[0].trim());
            int year = Integer.parseInt(parts[1].trim());
            String title = parts[2].trim();

            // Handle titles with commas
            if(parts.length > 3) {
                for(int i = 3; i < parts.length; i++) {
                    title += "," + parts[i].trim();
                }
            }

            movies.add(new Movie(id, year, title));
        }
        return movies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        ensureFileExists();
        List<String> movies = Files.readAllLines(Paths.get(MOVIES_FILE));

        // Generate next ID (last movie's ID + 1)
        int nextId = 1;
        if (!movies.isEmpty()) {
            String[] lastLine = movies.get(movies.size() - 1).split(",");
            nextId = Integer.parseInt(lastLine[0]) + 1;
        }

        // Append new movie to file
        String newMovieLine = nextId + "," + newMovie.getYear() + "," + newMovie.getTitle();
        Files.write(Paths.get(MOVIES_FILE), (newMovieLine + System.lineSeparator()).getBytes(), APPEND);

        return new Movie(nextId, newMovie.getYear(), newMovie.getTitle());
    }

    @Override
    public void updateMovie(Movie updatedMovie) throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<String> updatedLines = new ArrayList<>();
        boolean movieFound = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length >= 3) {
                int currentId = Integer.parseInt(parts[0].trim());

                // Replace line if ID matches
                if (currentId == updatedMovie.getId()) {
                    updatedLines.add(updatedMovie.getId() + "," + updatedMovie.getYear() + "," + updatedMovie.getTitle());
                    movieFound = true;
                } else {
                    updatedLines.add(line); // Keep original line
                }
            }
        }

        if (!movieFound) throw new Exception("Movie not found for update");

        Files.write(Paths.get(MOVIES_FILE), updatedLines); // Write all lines back
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<String> updatedLines = new ArrayList<>();
        boolean movieFound = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length >= 3) {
                int currentId = Integer.parseInt(parts[0].trim());

                // Skip line if ID matches (delete), otherwise keep it
                if (currentId != movie.getId()) {
                    updatedLines.add(line);
                } else {
                    movieFound = true;
                }
            }
        }

        if (!movieFound) throw new Exception("Movie not found for deletion");

        Files.write(Paths.get(MOVIES_FILE), updatedLines); // Write without deleted movie
    }

    // In MovieDAO_File class - add this method
    public void renumberMovies() throws Exception {
        ensureFileExists();
        List<String> lines = Files.readAllLines(Paths.get(MOVIES_FILE));
        List<String> updatedLines = new ArrayList<>();

        int newId = 1;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length >= 3) {
                // Keep the year and title, but assign new sequential ID
                int year = Integer.parseInt(parts[1].trim());
                String title = parts[2].trim();

                // Rebuild title in case it had commas
                if(parts.length > 3) {
                    for(int i = 3; i < parts.length; i++) {
                        title += "," + parts[i].trim();
                    }
                }

                // Create new line with sequential ID
                String newLine = newId + "," + year + "," + title;
                updatedLines.add(newLine);
                newId++;
            }
        }

        // Write all lines back with new IDs
        Files.write(Paths.get(MOVIES_FILE), updatedLines);
    }

}