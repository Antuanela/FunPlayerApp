package me.antox.funplayer.database.repo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import me.antox.funplayer.database.model.SongModel;

@Dao
public interface SongDao {
    @Query("SELECT COUNT(*) FROM song")
    Single<Integer> getCount();

    @Query("SELECT COUNT(*) FROM song where name = :name")
    Single<Integer> songCount(String name);

    @Query("SELECT * FROM song")
    Single<List<SongModel>> getAll();

    @Query("SELECT * FROM song WHERE name IN (:name)")
    Single<List<SongModel>> loadAllByNames(String name);

    @Query("SELECT * FROM song WHERE name IN (:name) AND genre IN (:genre)")
    Single<List<SongModel>> loadAllByNamesAndGenre(String name, String genre);

    @Query("SELECT * FROM song WHERE genre IN (:genre)")
    Single<List<SongModel>> loadAllByGenre(String genre);

    @Insert
    Single<List<Long>> insertAll(SongModel... songs);

    @Delete
    Single<Integer> delete(SongModel song);
}
