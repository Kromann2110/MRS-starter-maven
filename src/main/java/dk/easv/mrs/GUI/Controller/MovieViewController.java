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

// Controls the main movie screen
public class MovieViewController implements Initializable {
    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;
    private MovieModel movieModel;

    @FXML
    private TextField txtTitle, txtYear;

    public MovieViewController() {
        try {
            movieModel = new MovieModel(); // Initialize data model
        } catch (Exception e) {
            displayError(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect ListView to movie data
        lstMovies.setItems(movieModel.getObservableMovies());

        // Real-time search as user types
        txtMovieSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue); // Filter movies
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

    @FXML
    private void btnHandleClick(ActionEvent actionEvent) throws Exception {
        // Get input from text fields
        String title = txtTitle.getText();
        int year = Integer.parseInt(txtYear.getText());

        // Create and save new movie
        Movie newMovie = new Movie(-1, year, title);
        movieModel.createMovie(newMovie);
    }

    @FXML
    private void btnHandleDelete(ActionEvent actionEvent) {
        Movie selected = lstMovies.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                movieModel.deleteMovie(selected); // Delete selected movie
                clearFields();
            } catch (Exception e) {
                displayError(e);
            }
        } else {
            showAlert("No movie selected", "Please select a movie to delete");
        }
    }

    @FXML
    private void btnHandleUpdate(ActionEvent actionEvent) {
        try {
            // This will now renumber all movies sequentially
            movieModel.renumberMovies();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Movies renumbered successfully");
            alert.setContentText("All movies now have sequential IDs.");
            alert.showAndWait();

        } catch (Exception e) {
            displayError(e);
        }
    }

    private void clearFields() {
        lstMovies.getSelectionModel().clearSelection();
        txtTitle.clear();
        txtYear.clear();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}