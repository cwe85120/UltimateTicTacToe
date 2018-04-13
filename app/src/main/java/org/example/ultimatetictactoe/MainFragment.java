package org.example.ultimatetictactoe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainFragment extends Fragment {

    private AlertDialog dialog;
    private SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
     View rootView = inflater.inflate(R.layout.fragment_main,container,false );

        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about_title);
                builder.setMessage(R.string.about_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.show();
            }
        });

        View statsButton = rootView.findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiate variables
                int startedTotal = 0;
                int wonTotal = 0;
                int blueWins = 0;
                int redWins = 0;

                //make database if needed
                wakeUpDB();

                //query database
                int[] returnVars = getStats();

                //setup dialogue with counts
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.stats_title);
                builder.setMessage(String.format(getResources().getString(R.string.stats_text), startedTotal, wonTotal, blueWins, redWins));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.show();
            }
        });

        View tipsButton = rootView.findViewById(R.id.tips_button);
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getRandomTitle());
                builder.setMessage(getRandomText());
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.show();
            }
        });

        View newButton = rootView.findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        View continueButton = rootView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra(GameActivity.KEY_RESTORE, true);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
    //methods to generate random hint text
    private String getRandomTitle(){
        return getString (R.string.tip_title, (new Random().nextInt((1000 - 1) + 1) + 1) + " ");
    }

    private String getRandomText(){
        String[] textList = getResources().getStringArray(R.array.tip_text_array);

        int textSel = new Random().nextInt(textList.length);

        return textList[textSel];
    }

    //methods to deal with database
    public void wakeUpDB(){
        //make or open db

        //make table if none

    }

    private int[] getStats(){
        int[] returnArr = new int[4];



        return returnArr;
    }

    @Override
    public void onPause(){
        super.onPause();

        if(dialog != null){
            dialog.dismiss();
        }
    }
}
