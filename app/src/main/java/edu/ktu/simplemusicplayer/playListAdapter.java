package edu.ktu.simplemusicplayer;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class playListAdapter extends ArrayAdapter<File>{

    private Context context;

    public playListAdapter(Context context, List<File> objects){
        super(context, R.layout.playlistitemdesign,objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v= convertView;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.playlistitemdesign,null);
        }

        TextView name=(TextView) v.findViewById(R.id.fileName);

        File myFile = getItem(position);

        name.setText(myFile.getName());
        return v;
    }
}
