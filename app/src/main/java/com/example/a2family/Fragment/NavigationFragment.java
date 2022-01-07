package com.example.a2family.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2family.Activities.CalendarActivity;
import com.example.a2family.Activities.ChatActivity;
import com.example.a2family.Activities.GroceryListActivity;
import com.example.a2family.Activities.GroupPageActivity;
import com.example.a2family.Activities.LoginActivity;
import com.example.a2family.Activities.MapsActivity;
import com.example.a2family.Activities.ToDoActivity;
import com.example.a2family.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExitFragment newInstance(String param1, String param2) {
        ExitFragment fragment = new ExitFragment(null, null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.shopping:
                        if(!(getActivity() instanceof GroceryListActivity)){
                            startActivity(new Intent(getActivity(), GroceryListActivity.class),ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            if (navigationView.isShown()) {
                                dismiss();
                            }
                            if(!(getActivity() instanceof GroupPageActivity)){
                                getActivity().finish();
                            }
                        }
                        break;
                    case R.id.to_do:
                        if(!(getActivity() instanceof ToDoActivity)){
                            startActivity(new Intent(getActivity(), ToDoActivity.class),ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            if (navigationView.isShown()) {
                                dismiss();
                            }
                            if(!(getActivity() instanceof GroupPageActivity)){
                                getActivity().finish();
                            }
                        }
                        break;
                    case R.id.events:
                        if(!(getActivity() instanceof CalendarActivity)){
                            startActivity(new Intent(getActivity(), CalendarActivity.class),ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            if (navigationView.isShown()) {
                                dismiss();
                            }
                            if(!(getActivity() instanceof GroupPageActivity)){
                                getActivity().finish();
                            }
                        }
                        break;
                    case R.id.family_chat:
                        if(!(getActivity() instanceof ChatActivity)) {
                            startActivity(new Intent(getActivity(), ChatActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            if (navigationView.isShown()) {
                                dismiss();
                            }
                            if(!(getActivity() instanceof GroupPageActivity)){
                                getActivity().finish();
                            }
                        }

                        break;
                    case R.id.family_tracking:
                        if(!(getActivity() instanceof MapsActivity)) {
                            startActivity(new Intent(getActivity(), MapsActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                            if (navigationView.isShown()) {
                                dismiss();
                            }
                            if(!(getActivity() instanceof GroupPageActivity)){
                                getActivity().finish();
                            }
                        }

                        break;
            }
            return false;
            }
        });
        return view;
    }
}