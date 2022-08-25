/*
 * Made by Kevin Wang 2022
 * For Java 2
 * ID 991681013
 * This is the class that has everything to do with the methods
 * that change the song array list which is uploaded via text
 * This class also has the read/write to file utility
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SongList{

    //I made the array list static because this is what the observable
    //array list is going to read
    private static ArrayList<Song> songList= new ArrayList<Song>();

    //Getter for the array list
    public ArrayList<Song> getSongList(){
        return songList;
    }

    /**
     * This loads the database into the arraylist so the song sorter can modify it
     */
    public void load(){
        Scanner reader = null;
        
        try {
            //This references the database file
            File database = new File("SongList.txt");
            reader = new Scanner(database);

            //While there's still data, it'll read it
            while (reader.hasNext()) {
                String record = reader.nextLine();

                //Split the next line into sizeable chunks
                String[]fields = record.split(",");

                //This makes sure that none of the fields are null
                //If any parameter is null, then it skips loading that song
                boolean isNull=false;
                for(int n=0; n<fields.length;n++){
                    if(fields[n]==null){
                        isNull=true;
                        break;
                    }
                }

                //This skips the current loop if one of the fields was null
                if(isNull){
                    continue;
                }

                /*Song Name fields */
                String songName = fields[0];
                String artist = fields[1];
                String genre = fields[2];

                int[] mood= new int[5];

                //This is to store the mood parameters

                for (int n=0; n<5; n++){
                    mood[n]=Character.getNumericValue(fields[n+3].charAt(1));
                }

                // Create a new object for each record
                Song sing = new Song(songName, artist, genre, mood);
                songList.add(sing);    // Add object to list
            }
        }
        //This creates a new file if the file doesn't exist
        catch (FileNotFoundException e) {
            createEmptyFile();
            
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    /**
     * This adds a song to the song list and updates the database
     * @param song
     */
    public static void addSong(Song song){

        songList.add(song);

        //This saves the added song into the text file
        PrintWriter writer = null;
        try {
            File database = new File("SongList.txt");
            writer = new PrintWriter(database);
            for (int n = 0; n < songList.size(); n++) {
                Song sing = songList.get(n);
                writer.println(sing.getSongName() + ","+
                               sing.getArtistName()+ ","+
                               sing.getGenre()+ ","+
                               Arrays.toString(sing.getMood()));
            }
        }
        catch (FileNotFoundException e) {
            createEmptyFile();
        }

        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * This deletes a song from the song list and updates the database
     * @param song
     */
    public static void deleteSong(Song song){
        songList.remove(song);

        //This saves the removed song into the text file
        PrintWriter writer = null;
        try {
            File database = new File("SongList.txt");
            writer = new PrintWriter(database);
            for (int n = 0; n < songList.size(); n++) {
                Song sing = songList.get(n);
                writer.println(sing.getSongName() + ","+
                               sing.getArtistName()+ ","+
                               sing.getGenre()+ ","+
                               Arrays.toString(sing.getMood()));
            }
        }
        catch (FileNotFoundException e) {
            createEmptyFile();
        }

        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * This is used to create an empty file if the file isn't found
     */
    private static void createEmptyFile(){
        File newList = new File("SongList.txt");
            try {
                if(newList.createNewFile()){
                    Alert fileNotFound1 = new Alert(AlertType.INFORMATION, 
                        "File not found, new empty file has been created");
                    fileNotFound1.show();
                }
            } 
            
            //Honestly I don't know how you would get to here.
            catch (IOException e1) {
                Alert fileNotFound2 = new Alert(AlertType.INFORMATION, 
                    "File not found, empty file cannot be created.");
                fileNotFound2.show();
                System.exit(1);
            }
            
    }

    /**
     * This is used to make sure the user doesn't enter a 
     * a field that has commas, becuase then it'd
     * mess up the regex from the file read
     * @param song
     * @return
     */
    public static boolean isValidSong(Song song){
        if(song.getGenre().indexOf(",")==-1 
            && song.getArtistName().indexOf(",")==-1
            && song.getSongName().indexOf(",")==-1){
                return true;
            }
        return false;
    }
}
