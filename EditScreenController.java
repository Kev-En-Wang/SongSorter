/*
 * Made by Kevin Wang 2022
 * For Java 2
 * ID 991681013
 * This is the controller for the edit screen
 */

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EditScreenController {
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

    /*This is used for saving the original entry*/
    Song oldSong;





    //Initializeing method
    @FXML private void initialize() {
        
        /************Events******************/
        cancel.setOnAction( e -> cancelClicked() );
        confirm.setOnAction( e -> confirmClicked() );

        /************Setting the textfields for the selected song*************/
        songName.setText(SongSorterController.getSelectedSong().getSongName());
        artist.setText(SongSorterController.getSelectedSong().getArtistName());
        genre.setText(SongSorterController.getSelectedSong().getGenre());
        if(SongSorterController.getSelectedSong().getMood()[0]==1){
            lyrics.setSelected(true);
        }
        loudness.setValue(SongSorterController.getSelectedSong().getMood()[1]);
        abstractness.setValue(SongSorterController.getSelectedSong().getMood()[2]);
        happiness.setValue(SongSorterController.getSelectedSong().getMood()[3]);
        pace.setValue(SongSorterController.getSelectedSong().getMood()[4]);
        /********************************************************************/

        /*************Saving the original entry***************************************/
        oldSong = SongSorterController.getSelectedSong();
        /************************************************************************/

    }

    // Close this window
    private void cancelClicked(){
        Stage stage = (Stage) cancel.getScene().getWindow();
        //This tells the primary page to refresh the table view
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        //Closes the window
        stage.close();
    }

    /*The general idea is it's going to delete the selected song
     * and then remake the list again. Since our database is
     * literally a text file, this won't take too long. But if this
     * was a real database, we'd have to find the song inside the
     * the table and edit it directly so we don't recreate the whole database
     * everytime.
    */
    private void confirmClicked(){
        
        SongList.deleteSong(oldSong);
        
        //This creates a song with the specified data
        //Taken and reused from add controller
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
                        "Song has been edited");
        added.show();
        cancelClicked();
    }
}
