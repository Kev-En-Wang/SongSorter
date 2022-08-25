/*
 * Made by Kevin Wang 2022
 * For Java 2
 * ID 991681013
 * This is the controller for the add screen
 */
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddScreenController {
    //Variables for each component
    @FXML private Slider loudness;
    @FXML private Slider abstractness;
    @FXML private Slider happiness;
    @FXML private Slider pace;
    @FXML private TextField songName;
    @FXML private TextField artist;
    @FXML private TextField genre;
    @FXML private Button cancel;
    @FXML private Button confirm;
    @FXML private CheckBox lyrics;
    
    @FXML private void initialize() {
        cancel.setOnAction( e -> cancelClicked() );
        confirm.setOnAction( e -> confirmClicked() );
    }

    /*Outer Methods************************************************/
    // Close this window
    private void cancelClicked(){
         Stage stage = (Stage) cancel.getScene().getWindow();
         //This tells the primary page to refresh the table view
         stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

         stage.close();
    }

    //Saves whatever you have and closes the window
    private void confirmClicked(){
        //This creates a song with the specified data
        int lyricsInt=0;
        if (lyrics.isSelected()){
            lyricsInt=1;
        }
        int loudInt = (int) loudness.getValue();
        int abstractInt = (int) abstractness.getValue();
        int happyInt = (int) happiness.getValue();
        int paceInt=(int) pace.getValue();
        int[] moodArray = {lyricsInt, loudInt,abstractInt,happyInt,paceInt};

        String song = songName.getText();
        String artistName = artist.getText();
        String genreName=genre.getText();

        Song newSong = new Song(song, artistName, genreName,moodArray);
        
        if (!SongList.isValidSong(newSong)){
            Alert invalidFile = new Alert(AlertType.WARNING, 
                    "You can't have commas in your fields");
                invalidFile.show();
            return;
        }
        SongList.addSong(newSong);
        
        //Lets the user know it's been added
        Alert added = new Alert(AlertType.INFORMATION, 
                        "Song has been added");
        added.show();
        cancelClicked();
    }
}
