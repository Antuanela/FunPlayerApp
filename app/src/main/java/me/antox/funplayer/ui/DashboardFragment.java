package me.antox.funplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import me.antox.funplayer.database.repo.SongRepository;
import me.antox.funplayer.databinding.FragmentDashboardBinding;
import me.antox.funplayer.player.FunPlayer;
import me.antox.funplayer.ui.vm.DashboardViewModel;

public class DashboardFragment extends Fragment implements CustomPlayerControl {

    private DashboardViewModel dashboardViewModel;
    private FunPlayer funPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);

        if (savedInstanceState == null) {
            dashboardViewModel.init(this, new SongRepository(Objects.requireNonNull(getActivity())));
            funPlayer = new FunPlayer(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDashboardBinding binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(dashboardViewModel);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(dashboardViewModel != null) {
            dashboardViewModel.onDestroyView();
        }
    }

    @Override
    public void play(String url) {
        funPlayer.load(url);
    }
}
