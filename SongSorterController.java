/*
 * Made by Kevin Wang 2022
 * For Java 2
 * ID 991681013
 * This is the main controller for the song sorter.
 * It has a lot of methods regarding all the components that's shown,
 * but delegates all the song list and file manipulation parts to the 
 * SongList class. Everything here is cohesive for only the SongSorter
 * window.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;

public class SongSorterController extends SongSorterApp {

    //The song list that we're using
    private static SongList songList=new SongList();

    /********************Control Settings*******************/
    @FXML private Button add;
    @FXML private Button edit;
    @FXML private Button delete;
    @FXML private Button search;
    @FXML private Button refresh;
    @FXML private TextField searchField;
    @FXML private TableView<Song> output;
    @FXML private TableColumn<Song, String> songNameCol;
    @FXML private TableColumn<Song, String> artistCol;
    @FXML private TableColumn<Song, String> genreCol;
    @FXML private TableColumn<Song, String> moodStringCol;
    /*******************************************************/
    /********************TableView Components*******************/
    @FXML private TableViewSelectionModel<Song> selection; 
    @FXML private ObservableList<Song> songListObs;
    @FXML private static ObservableList<Song> selectedSong;
    /**********************************************************/
    /********************Windown Components*******************/
    @FXML private Stage addScreenStage=new Stage();
    @FXML private Stage editScreenStage=new Stage();
    /*********************************************************/
    //String choices[] = {"abc","bsbd","asdsa"};
    @FXML ChoiceBox<String> searchBy = new ChoiceBox<String>();

    //Startup Code
    @FXML private void initialize() {

        //Configuring the choicebox
        searchBy.getItems().add("Song Name");
        searchBy.getItems().add("Artist");
        searchBy.getItems().add("Genre");
        searchBy.setValue("Song Name");
        
        //Event Handlers
        add.setOnAction(new addHandler());
        edit.setOnAction(new editHandler());
        delete.setOnAction(new deleteHandler());
        addScreenStage.setOnHidden(new saveOnExit());
        editScreenStage.setOnHidden(new saveOnExit());
        
        //Outer Methods
        search.setOnAction((event) ->  searchClicked());
        refresh.setOnAction(e->refreshClicked());

        //This loads the other two modalities so it doesn't load twice
        addScreenStage.initModality(Modality.APPLICATION_MODAL);
        editScreenStage.initModality(Modality.APPLICATION_MODAL);
        
        //This loads the songs from the text file into the arraylist
        songList.load();

        /************************Table view part*******************************/
        //This makes an observable song list from the loaded song list for the controller
        songListObs = FXCollections.observableArrayList(songList.getSongList());
        selection = output.getSelectionModel();


        //Sets the placeholder for no data
        output.setPlaceholder(new Label("No data"));

        //Sets the selection model for the tableview so you can only select one thing at a time

        selection.setSelectionMode(SelectionMode.SINGLE);

        //This loads the tableview from whatever information is in the
        //observable list
        tableLoad();
        /*********************************************************************/
    }

    /***********************INNER CLASSES*************************************/
    //This opens the Add screen
    private class addHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent c){
            try{
                Parent root= FXMLLoader.load(getClass().getResource("AddScreen.fxml"));

                Scene addScreen = new Scene(root);

                addScreenStage.setScene(addScreen);
                addScreenStage.show();
            }
            catch(IOException a){
                a.printStackTrace();
            }
        }
    }

    //This opens the edit window
    private class editHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent c){

            //This sets the selected song
            selectedSong= selection.getSelectedItems();

            //This is if nothing was selected
            try{
                selectedSong.get(0).getArtistName();
            }
            catch(IndexOutOfBoundsException e){
                Alert nothing = new Alert(AlertType.WARNING, 
                        "Nothing was selected");
                nothing.show();
                return;
            }

            try{
                Parent root= FXMLLoader.load(getClass().getResource("EditScreen.fxml"));

                Scene editScreen = new Scene(root);

                editScreenStage.setScene(editScreen);
                editScreenStage.show();
            }
            catch(IOException a){
                a.printStackTrace();
            }
        }
    }
    
    //This deletes whatever song is selected
    private class deleteHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent c){

            //This deletes the selected song
            selectedSong= selection.getSelectedItems();
            SongList.deleteSong(selectedSong.get(0));
            
            //This updates the list
            songList.getSongList().clear();
            songList.load();
            songListObs = FXCollections.observableArrayList(songList.getSongList());
            tableLoad();


        }
    }    
    //This reloads the tableview with new info after you edit it
    private class saveOnExit implements EventHandler<WindowEvent>{
        @Override
        public void handle(WindowEvent closeWindowEvent) {
            songList.getSongList().clear();
            songList.load();
            songListObs = FXCollections.observableArrayList(songList.getSongList());
            tableLoad();
        }

    }
    /*********************************************************/


    /***********************OUTER METHODS*********************/
    //This searches and displays only what was searched for
    private void searchClicked(){
        ArrayList<Song> searchSong = new ArrayList<>();
        ArrayList<Song> searchFrom = songList.getSongList();
        String query = searchField.getText();

        String searchType = searchBy.getValue();
        if(searchType=="Song Name"){
            for(int n=0; n<searchFrom.size(); n++){
                if(searchFrom.get(n).getSongName().equals(query)){
                    searchSong.add(searchFrom.get(n));
                }
        }
        }
        else if(searchType=="Artist"){
            for(int n=0; n<searchFrom.size(); n++){
                if(searchFrom.get(n).getArtistName().equals(query)){
                    searchSong.add(searchFrom.get(n));
                }
            }
        }
        else if(searchType=="Genre"){
            for(int n=0; n<searchFrom.size(); n++){
                if(searchFrom.get(n).getGenre().equals(query)){
                    searchSong.add(searchFrom.get(n));
                }
            }
        }
        else{
            Alert nothing = new Alert(AlertType.WARNING, 
                        "Please select search type");
            nothing.show();
            return;
        }
        
        //This refreshes the table with only what's selected
        songListObs = FXCollections.observableArrayList(searchSong);
        tableLoad();
    }

    //For when someone hits the refresh button
    private void refreshClicked(){
        songList.getSongList().clear();
        songList.load();
        songListObs = FXCollections.observableArrayList(songList.getSongList());
        tableLoad();
    }

    //This loads the observable list and displays it on the tableview
    @FXML private void tableLoad(){
        songNameCol.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("artistName"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Song, String>("genre"));
        moodStringCol.setCellValueFactory(new PropertyValueFactory<Song, String>("moodString"));
        output.setItems(songListObs);
    }

    //This method is called by other windows to get the selected song
    public static Song getSelectedSong(){
        return selectedSong.get(0);
    }
    /*********************************************************/
}


