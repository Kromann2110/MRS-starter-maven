package dk.easv.mrs.GUI.Controller;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {
    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;

    @FXML
    private TextField txtTitle, txtYear;

    private MovieModel movieModel;

    public MovieViewController() {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstMovies.setItems(movieModel.getObservableMovies());

        lstMovies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedMovie) -> {
            if (selectedMovie != null) {
                txtTitle.setText(selectedMovie.getTitle());
                txtYear.setText(String.valueOf(selectedMovie.getYear()));
            }
        });

        txtMovieSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
            }
        });
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    // CREATE button - creates new movie
    @FXML
    private void handleCreate(ActionEvent event) {
        String title = txtTitle.getText().trim();
        String yearText = txtYear.getText().trim();

        if (title.isEmpty() || yearText.isEmpty()) {
            showAlert("Missing Information", "Please enter both title and year");
            return;
        }

        try {
            int year = Integer.parseInt(yearText);
            Movie newMovie = new Movie(-1, year, title);
            movieModel.createMovie(newMovie);

            // Renumber after creating to maintain sequence
            movieModel.renumberMovies();

            clearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Movie Created");
            alert.setContentText("New movie has been added to the list.");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Invalid Year", "Please enter a valid year number");
        } catch (Exception e) {
            displayError(e);
        }
    }

    // UPDATE button - updates selected movie's title and year
    @FXML
    private void handleUpdate(ActionEvent event) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("No Movie Selected", "Please select a movie to update");
            return;
        }

        String newTitle = txtTitle.getText().trim();
        String yearText = txtYear.getText().trim();

        if (newTitle.isEmpty() || yearText.isEmpty()) {
            showAlert("Missing Information", "Please enter both title and year");
            return;
        }

        try {
            int newYear = Integer.parseInt(yearText);

            // Update the selected movie
            Movie movieToBeUpdated = new Movie(
                    selectedMovie.getId(),
                    newYear,
                    newTitle
            );
            movieModel.updateMovie(movieToBeUpdated);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Movie Updated");
            alert.setContentText("The movie has been updated successfully.");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Invalid Year", "Please enter a valid year number");
        } catch (Exception e) {
            displayError(e);
        }
    }

    // DELETE button - deletes selected movie and renumbers
    @FXML
    private void handleDelete(ActionEvent event) {
        Movie selected = lstMovies.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                movieModel.deleteMovie(selected);
                clearFields();

                // Renumber after deletion to fix sequence
                movieModel.renumberMovies();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Movie Deleted");
                alert.setContentText("The movie has been deleted and list renumbered.");
                alert.showAndWait();

            } catch (Exception e) {
                displayError(e);
            }
        } else {
            showAlert("No Movie Selected", "Please select a movie to delete");
        }
    }

    private void clearFields() {
        txtTitle.clear();
        txtYear.clear();
        lstMovies.getSelectionModel().clearSelection();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}