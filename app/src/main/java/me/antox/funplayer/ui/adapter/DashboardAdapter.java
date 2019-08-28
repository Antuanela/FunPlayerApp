package me.antox.funplayer.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.antox.funplayer.BR;
import me.antox.funplayer.database.model.SongModel;
import me.antox.funplayer.ui.vm.DashboardViewModel;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.GenericViewHolder> {

    private int layoutId;
    private List<SongModel> songModels;
    private DashboardViewModel viewModel;

    public DashboardAdapter(@LayoutRes int layoutId, DashboardViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return songModels == null ? 0 : songModels.size();
    }

    public SongModel getSongModel(int index){
        return songModels.get(index);
    }

    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setSongs(List<SongModel> songs) {
        this.songModels = songs;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DashboardViewModel viewModel, Integer position) {
            SongModel songModel = getSongModel(position);

            binding.setVariable(BR.vm, viewModel);

            if(songModel != null) {
                binding.setVariable(BR.song, songModel);
            }

            binding.executePendingBindings();
        }

    }
}
