package Proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver{
    public static void main(String[] args){
        SongService songServerAccess = new SongServiceProxy();

        Song song;
        int duration;
        List<Song> mySongs;

        System.out.println("Searching by ID: 1");
        song = songServerAccess.searchById(1);
        System.out.println(song);
        duration = song.getDuration();
        System.out.println("Duration: " + duration + " Seconds");
        System.out.println();

        System.out.println("Searching by ID: 4");
        song = songServerAccess.searchById(4);
        System.out.println(song);
        duration = song.getDuration();
        System.out.println("Duration: " + duration + " Seconds");
        System.out.println();

        System.out.println("Searching by album: Utopia");
        mySongs = songServerAccess.searchByAlbum("Utopia");
        System.out.println("Songs in Album Utopia:");
        mySongs.forEach(System.out::println);
        System.out.println();

        System.out.println("Searching by album: The Melodic Blue");
        mySongs = songServerAccess.searchByAlbum("The Melodic Blue");
        System.out.println("Songs in Album The Melodic Blue:");
        mySongs.forEach(System.out::println);
        System.out.println();

        System.out.println("Searching by title: 16");
        mySongs = songServerAccess.searchByTitle("16");
        System.out.println("Songs with Title '16':");
        mySongs.forEach(System.out::println);
        System.out.println();
    }
}


class Song{
    private final Integer id;
    private final String title;
    private final String artist;
    private final String album;
    private final int duration;

    public Song(Integer id, String title, String artist, String album, int duration){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public Integer getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAlbum(){
        return album;
    }

    public int getDuration(){
        return duration;
    }

    @Override
    public String toString(){
        return "Song = " + title + ", Artist = " + artist + ", Album = " + album ;
    }
}

interface SongService{
    Song searchById(Integer songID);
    List<Song> searchByTitle(String title);
    List<Song> searchByAlbum(String album);
}

class SongServiceServer implements SongService{
    private final List<Song> songDatabase;

    public SongServiceServer(){
        songDatabase = new ArrayList<>();
        songDatabase.add(new Song(1, "HYAENA", "Travis Scott", "Utopia", 240));
        songDatabase.add(new Song(2, "16", "Baby Keem", "The Melodic Blue", 180));
        songDatabase.add(new Song(3, "Durag Activity", "Baby Keem", "The Melodic Blue", 300));
        songDatabase.add(new Song(4, "MODERN JAM", "Travis Scott", "Utopia", 210));
        songDatabase.add(new Song(5, "MY EYES", "Travis Scott", "Utopia", 437));
    }

    @Override
    public Song searchById(Integer songID){
        delay();

        for(Song song : songDatabase){
            if(song.getId().equals(songID)){
                return song;
            }
        }

        return null;
    }

    @Override
    public List<Song> searchByTitle(String title){
        delay();
        List<Song> results = new ArrayList<>();

        for(Song song : songDatabase){
            if(song.getTitle().equalsIgnoreCase(title)){
                results.add(song);
            }
        }

        return results;
    }

    @Override
    public List<Song> searchByAlbum(String album){
        delay();
        List<Song> results = new ArrayList<>();

        for(Song song : songDatabase){
            if(song.getAlbum().equalsIgnoreCase(album)){
                results.add(song);
            }
        }

        return results;
    }

    private void delay(){
        try{
            Thread.sleep(1000);
        }catch(Exception ignored){}
    }
}

class SongServiceProxy implements SongService{
    private final SongServiceServer realSongService;
    private final Map<Integer, Song> songCache;

    public SongServiceProxy(){
        this.realSongService = new SongServiceServer();
        this.songCache = new HashMap<>();
    }

    @Override
    public Song searchById(Integer songID){
        Song cachedSong = songCache.get(songID);

        if(cachedSong != null){
            System.out.println("Retrieving song " + songID + " from cache.");
            return cachedSong;
        }

        Song song = realSongService.searchById(songID);

        if(song != null){
            songCache.put(songID, song);
        }

        return song;
    }

    @Override
    public List<Song> searchByTitle(String title){
        return realSongService.searchByTitle(title);
    }

    @Override
    public List<Song> searchByAlbum(String album){
        return realSongService.searchByAlbum(album);
    }
}