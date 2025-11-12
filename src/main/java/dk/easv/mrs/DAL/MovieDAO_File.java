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

    // Create button
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


// Update button - This should update an existing movie, not renumber all
    @Override
    public void updateMovie(Movie movie) throws Exception {
        try
        {
            // Read all movie lines in list
            List<String> movies = Files.readAllLines(Paths.get(MOVIES_FILE));

            // Iterate through all lines and look for the right one (movie)
            for (int i = 0; i < movies.size(); i++)
            {
                // Split each line into atomic parts
                String[] separatedLine = movies.get(i).split(",");

                // Make sure we have a valid movie with all parts
                if(separatedLine.length >= 3) {
                    // individual movie items
                    int id = Integer.parseInt(separatedLine[0]);

                    // Check if the id is equal to movie.getId()
                    if (id == movie.getId()) {
                        String updatedMovieLine = movie.getId() + "," + movie.getYear() + "," + movie.getTitle();
                        movies.set(i, updatedMovieLine);
                        break;
                    }
                }
            }

            // Create new temp file
            Path filePath = Paths.get(MOVIES_FILE);  // Declare filePath here
            Path tempPathFile = Paths.get(MOVIES_FILE + "_TEMP");

            // Delete temp file if it exists from previous operations
            Files.deleteIfExists(tempPathFile);
            Files.createFile(tempPathFile);

            // For all lines...
            for (String line: movies) {
                Files.write(tempPathFile, (line + System.lineSeparator()).getBytes(), APPEND);
            }

            // Overwrite the old file with temp file
            Files.copy(tempPathFile, filePath, REPLACE_EXISTING);
            Files.deleteIfExists(tempPathFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("An error occurred while updating the movie");
        }
    }

    // Delete button
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

    // Renumber movies - This should be a separate functionality, not the main update
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