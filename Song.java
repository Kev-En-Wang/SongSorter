/*
 * Made by Kevin Wang 2022
 * For Java 2
 * ID 991681013
 * This class models everything you need to know about a song.
 * I took out all the setters that wasn't used.
 */
import javafx.beans.property.SimpleStringProperty;

public class Song {
    
    //The attributes for the song
    private SimpleStringProperty songName, artistName, genre, moodString;
    private int[] mood= new int[5];

    //Setters and getters for song
    /*****************************************************************/
    public String getMoodString() {
        return moodString.get();
    }

    public void setMoodString(SimpleStringProperty moodString) {
        this.moodString = moodString;
    }
    
    public String getSongName() {
        return songName.get();
    }

    public String getArtistName() {
        return artistName.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public int[] getMood() {
        return mood;
    }

    public void setMood(int[] mood) {
        this.mood = mood;
    }
    /***********************************************************************/
    
    //Constructor method for a new song
    public Song(String songName, String artistName, String genre, int[] mood){
        this.songName=new SimpleStringProperty(songName);
        this.artistName=new SimpleStringProperty(artistName);
        this.genre=new SimpleStringProperty(genre);
        this.mood=mood;
        this.moodString=new SimpleStringProperty(moodToString(mood));
    }

    /***********************************************************************/
    /**
    *This converts the mood int array to a string that has the highest
    *value so it displays nicer on the table. Honestly, just a quality of life
    *addition to the app.
    */
    public String moodToString(int[] mood){
        int moodInt=1;
        String moodString=null;

        //iterates through the array to get the maximum value for the mood
        for(int n=0; n<5; n++){
            if (mood[n]>moodInt){
                moodInt=mood[n];
            }
        }

        //The firstTime variable sees if it's the first time something's selected
        boolean firstTime=true;

        //iterates through the array to get the string value of each max value
        for(int n=0; n<5;n++){
            
            //There's a special case for if everything's rated 1
            if(moodInt==1){
                break;
            }

            //This is so there's no comma and space for the first value
            if(moodInt==mood[n]&&firstTime){
                switch (n){
                    case 1:
                        moodString="Loud";
                        firstTime=false;
                        break;
                    case 2:
                        moodString="Abstract";
                        firstTime=false;
                        break;
                    case 3:
                        moodString="Happy";
                        firstTime=false;
                        break;
                    case 4:
                        moodString="Quick";
                        firstTime=false;
                        break;
                    default:
                        break;
                }
                    
            }
            else if(moodInt==mood[n]){
                switch (n){
                    case 2:
                        moodString=moodString + ", Abstract";
                        break;
                    case 3:
                        moodString=moodString + ", Happy";
                        break;
                    case 4:
                        moodString=moodString + ", Quick";
                        break;
                    default:
                        break;
                }
            }
        }
        
        //This is if the user rates everything as a 1
        if(moodString==null){
            return "Ambigious";
        }
        return moodString;
    }
}
