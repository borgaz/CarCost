package com.kuba.carcost.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImExFragment extends Fragment {

    private static View view;
    private DatabaseHelper myDb;

    private Button importDbButton;
    private Button exportDBButton;

    public ImExFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_im_ex, container, false);
        myDb = new DatabaseHelper(view.getContext());
        importDbButton = (Button) view.findViewById(R.id.importDbButton);
        exportDBButton = (Button) view.findViewById(R.id.exportDbButton);

        importDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myDb.importDatabase(getContext());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                myDb.exportDatabase(getContext());
                } catch (Exception e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
