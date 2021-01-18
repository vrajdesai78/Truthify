package com.vrajdesai78.truthify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    List<MyList>myLists;
    RecyclerView rv;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rv = findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        myLists=new ArrayList<>();
        getdata();
    }

    private void getdata() {
        myLists.add(new MyList(R.drawable.adidas));
        myLists.add(new MyList(R.drawable.apple));
        myLists.add(new MyList(R.drawable.armani));
        myLists.add(new MyList(R.drawable.nike));
        myLists.add(new MyList(R.drawable.puma));
        myLists.add(new MyList(R.drawable.reebok));
//        myLists.add(new MyList(R.drawable.calendar));
//        myLists.add(new MyList(R.drawable.notes));

        adapter=new MyAdapter(myLists,this);
        rv.setAdapter(adapter);
    }
}
