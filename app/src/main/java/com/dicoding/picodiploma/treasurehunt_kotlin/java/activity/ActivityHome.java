package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class ActivityHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;
    public TextView txtLat, txtLng, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNav);
        //Tambah home fragment di dequeList
        integerDeque.push(R.id.home);
        //Muat home fragment
        loadFragment(new FragmentHome());
        //Jadikan home fragment sebagai default
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Ambil item id yang diselect
                int id = item.getItemId();
                if(integerDeque.contains(id)){
                    //Ketika list deque memiliki selected id
                    //Cek kondisi
                    if(id == R.id.home){
                        //Ketika selected id = home fragment id
                        //Cek kondisi
                        if(integerDeque.size()!=1){
                            //Ketika list deque != 1
                            //Cek kondisi
                            if(flag){
                                //Ketika flag = true
                                //Tambah home fragment di deque list
                                integerDeque.addFirst(R.id.home);
                                flag=false;

                            }
                        }
                    }
                    //Hapus selected id dari list deque
                    integerDeque.remove(id);
                }
                //Push selected id di list deque
                integerDeque.push(id);
                //Muat fragment
                loadFragment(getFragment(item.getItemId()));
                //return true
                return false;
            }
        });
    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.home:
                //Set home fragment yang di cek
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                //Kembali ke home fragment
                return new FragmentHome();
            case R.id.games:
                //Set create fragment yang di cek
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                //Kembali ke create fragment
                return new FragmentGames();
            case R.id.account:
                //Set list fragment yang di cek
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                //Kembali ke list fragment
                return new FragmentAccount();
        }
        //Set checked default home fragment
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        return new FragmentHome();
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment,fragment.getClass().getSimpleName())
                .commit();
    }
}
