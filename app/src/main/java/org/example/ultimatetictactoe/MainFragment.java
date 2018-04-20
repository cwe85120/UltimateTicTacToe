package org.example.ultimatetictactoe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    private AlertDialog dialog;
    public SQLiteDatabase database;
    public static final String DATABASENAME = "tictac";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        database = getActivity().openOrCreateDatabase(DATABASENAME, MODE_PRIVATE, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
                int startedTotal;
                int continuedTotal;
                int wonTotal;
                int blueWins;
                int redWins;
                int tieWins;

                //make database if needed
                wakeUpDB();

                //query database
                int[] returnVars = getStats();
                startedTotal = returnVars[0];
                continuedTotal = returnVars[1];
                wonTotal = returnVars[2];
                blueWins = returnVars[3];
                redWins = returnVars[4];
                tieWins = returnVars[5];

                //setup dialogue with counts
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.stats_title);
                builder.setMessage(String.format(getResources().getString(R.string.stats_text), startedTotal, continuedTotal, wonTotal, blueWins, redWins, tieWins));
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
                addStartStat();
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        View continueButton = rootView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContinueStat();
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
        String databaseName = "ticTac";
        String tableName = "ticTacStats";
        //make table if none
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor==null){
            String sql = "create table " + tableName + " (_id integer PRIMARY KEY autoincrement, type text, time integer)";
            database.execSQL(sql);
        }
        cursor.close();
    }

    private int[] getStats(){
        int[] returnArr = new int[6];

        //query strings
        String startsQuery = "select count(*) from ticTacStats where type = start";
        String continuesQuery = "select count(*) from ticTacStats where type = continue";
        String winsQuery = "select count(*) from ticTacStats where type = win";
        String blueWinsQuery = "select count(*) from ticTacStats where type = blue";
        String redWinsQuery = "select count(*) from ticTacStats where type = red";
        String tieWinsQuerry = "select count(*) from ticTacStats where type = tie";

        //run querys
        //Cursor cursor = database.rawQuery(startsQuery, null);
        returnArr[0] = 0; //cursor.getCount();
        //cursor = database.rawQuery(continuesQuery, null);
        returnArr[1] = 0; //cursor.getCount();
        //cursor = database.rawQuery(winsQuery, null);
        returnArr[2] = 0; //cursor.getCount();
        //cursor = database.rawQuery(blueWinsQuery, null);
        returnArr[3] = 0; //cursor.getCount();
        //cursor = database.rawQuery(redWinsQuery, null);
        returnArr[4] = 0; //cursor.getCount();

        returnArr[5] = 0;

        //cursor.close();

        return returnArr;
    }

    private void addStartStat(){
        wakeUpDB();

        String addStartSQL = "insert into ticTacStats (type, time) values (start, 'now');";
        database.execSQL(addStartSQL);
    }

    private void addContinueStat(){
        wakeUpDB();

        String addContinueSQL = "insert into ticTacStats (type, time) values (continue, 'now');";
        database.execSQL(addContinueSQL);
    }

    @Override
    public void onPause(){
        super.onPause();

        if(dialog != null){
            dialog.dismiss();
        }
    }
}
