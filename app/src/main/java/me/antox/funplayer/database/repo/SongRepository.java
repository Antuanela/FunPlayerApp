package me.antox.funplayer.database.repo;

import android.content.Context;

import java.util.List;

import io.reactivex.Single;
import me.antox.funplayer.database.AppDatabase;
import me.antox.funplayer.database.model.SongModel;

public class SongRepository {
    private final AppDatabase database;
    private final SongDao songDao;

    public SongRepository(Context context)
    {
        this.database = AppDatabase.getAppDatabase(context.getApplicationContext());
        this.songDao = this.database.songDao();
    }


    public Single<List<SongModel>> loadAllByName(String name) {
        return songDao.loadAllByNames(name);
    }

    public Single<List<SongModel>> loadAllByGenre(String genre) {
        return songDao.loadAllByGenre(genre);
    }

    public  Single<List<SongModel>> loadAllByNamesAndGenre(String name, String genre) {
        return songDao.loadAllByNamesAndGenre(name, genre);
    }

    public  Single<List<SongModel>> loadAll(){
        return songDao.getAll();
    }

    public Single<List<Long>> insertAll(SongModel... songModels){
        return songDao.insertAll(songModels);
    }

    public Single<Integer> getCount(){
        return songDao.getCount();
    }
}
