package me.antox.funplayer.ui.vm;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.antox.funplayer.R;
import me.antox.funplayer.database.model.SongModel;
import me.antox.funplayer.database.repo.SongRepository;
import me.antox.funplayer.ui.CustomPlayerControl;
import me.antox.funplayer.ui.adapter.DashboardAdapter;

public class DashboardViewModel extends ViewModel implements AdapterView.OnItemSelectedListener {
    private SongRepository songRepository;
    private DashboardAdapter adapter;

    private Disposable songLoadDisposable;
    private Disposable songInsertDisposable;

    private int currentGenre = 0;
    private CustomPlayerControl customPlayerControl;

    public MutableLiveData searchTextLiveData = new MutableLiveData();

    public void init(CustomPlayerControl customPlayerControl, SongRepository songRepository) {
        this.customPlayerControl = customPlayerControl;
        this.songRepository = songRepository;
        this.adapter = new DashboardAdapter(R.layout.item_song, this);

        searchTextLiveData.postValue("");

        searchTextLiveData.observeForever(o -> loadSongs(o.toString(), SongModel.SongGenre.values()[currentGenre]));

        loadSongs(null, SongModel.SongGenre.values()[currentGenre]);
    }

    public DashboardAdapter getAdapter() {
        return adapter;
    }

    public void onDestroyView() {
        resetSongLoadDisposable();
        resetSongInsertDisposable();
    }

    public void onItemClick(SongModel songModel) {
        if(customPlayerControl != null){
            Log.d("TESTFUCK", "CLICKED: " + songModel.getName() + " URL: " + songModel.getUrl());
            customPlayerControl.play(songModel.getUrl());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentGenre = i;
        loadSongs(searchTextLiveData.getValue().toString(), SongModel.SongGenre.values()[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        currentGenre = 0;
        loadSongs(searchTextLiveData.getValue().toString(), null);
    }

    private void loadSongs(@Nullable String name, @Nullable SongModel.SongGenre genre) {
        resetSongLoadDisposable();

        songLoadDisposable = songRepository.getCount().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((count) -> {
                    if(count < 1){
                        insertDefaultItems();
                    }
                }, (e) -> Log.d("ERRORZ", e.getMessage()));

        if (!TextUtils.isEmpty(name) && genre != null) {
            songLoadDisposable = songRepository.loadAllByNamesAndGenre(name, genre.name())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::loadAdapter, (e) -> Log.d("ERRORZ", e.getMessage()));
        } else if (TextUtils.isEmpty(name) && genre != null) {
            songLoadDisposable = songRepository.loadAllByGenre(genre.name()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::loadAdapter, (e) -> Log.d("ERRORZ", e.getMessage()));
        } else if (!TextUtils.isEmpty(name) && genre == null) {
            songLoadDisposable = songRepository.loadAllByName(name).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::loadAdapter, (e) -> Log.d("ERRORZ", e.getMessage()));
        } else {
            songLoadDisposable = songRepository.loadAll().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::loadAdapter, (e) -> Log.d("ERRORZ", e.getMessage()));
        }
    }

    private void insertDefaultItems() {
        resetSongInsertDisposable();

        final List<SongModel> songModelList = new ArrayList<>();

        songModelList.add(new SongModel("Nightcore: Fireflies", "https://audio.jukehost.co.uk/4539dc4206e2cfefc5fd381bb08ca787b2c6fb0a/204d392c47f", "http://i3.ytimg.com/vi/k2pKfFP0anY/maxresdefault.jpg", SongModel.SongGenre.ROCK.name()));

        songInsertDisposable = songRepository.insertAll(songModelList.toArray(new SongModel[0])).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((e) -> loadAdapter(songModelList));
    }

    private void loadAdapter(List<SongModel> songModels) {
        adapter.setSongs(songModels);
        adapter.notifyDataSetChanged();
    }

    private void resetSongInsertDisposable() {
        if (songLoadDisposable != null) {
            songLoadDisposable.dispose();
            songLoadDisposable = null;
        }
    }

    private void resetSongLoadDisposable() {
        if (songInsertDisposable != null) {
            songInsertDisposable.dispose();
            songInsertDisposable = null;
        }
    }
}
