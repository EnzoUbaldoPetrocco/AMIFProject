package com.parkingapp.homeactivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.parkingapp.homeactivity.R;

import fragment_home_activity.HomeFragment;
import fragment_home_activity.MappeScaricate;
import fragment_home_activity.Parcheggio;
import fragment_home_activity.PromemoriaNotifica;
import fragment_home_activity.ScaricaMappa;
import fragment_home_activity.Utente;

public class HomeActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mToggle=null;
    private DrawerLayout drawerLayout=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Intent i= getIntent();


        drawerLayout =(DrawerLayout) findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        NavigationView navigationView= findViewById(R.id.nvNavigationViewHomeActivity);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);


        //Quest parte è identica all parte più sotto e serve a far si che all'apertura si apra la home con i comandi per far partire l'app
        Fragment myFragmant=null;
        Class fragmentClass=HomeFragment.class;

        try {
            myFragmant=(Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragmant).commit();



    }

    //Associo ad ogni item la classe a cui deve fare riferimento quando premo
    public void selectItemDrawer(MenuItem menuItem)
    {
        Fragment myFragmant=null;
        Class fragmentClass;

        switch (menuItem.getItemId())
        {
            case R.id.btMappeDaScaricare:
                fragmentClass= ScaricaMappa.class;
                break;

            case R.id.btMappeScaricate:
                fragmentClass= MappeScaricate.class;
                break;

            case R.id.btParcheggio:
                fragmentClass= Parcheggio.class;
                break;

            case R.id.btOrario:
                fragmentClass= PromemoriaNotifica.class;
                break;

            case R.id.btUtente:
                fragmentClass= Utente.class;
                break;

            case R.id.btHomeMenu:
                fragmentClass= HomeFragment.class;
                break;

                default:
                    fragmentClass=HomeFragment.class; //Di default torna alla HomeActivity
        }
        try {
            myFragmant=(Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragmant).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    //Capire qual è l'item selezionato nel menu a tendina
    private void setupDrawerContent (NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    //Pulsante per aprire menu a tendina
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
       // return super.onOptionsItemSelected(item); //Per ora non ci serve
        return  false;
    }




}
