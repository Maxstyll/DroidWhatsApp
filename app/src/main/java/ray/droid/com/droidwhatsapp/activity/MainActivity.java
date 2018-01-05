package ray.droid.com.droidwhatsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.adapter.TabAdapter;
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.helper.SlidingTabLayout;
import ray.droid.com.droidwhatsapp.model.Usuario;

import static ray.droid.com.droidwhatsapp.helper.Comum.ListarContatos;
import static ray.droid.com.droidwhatsapp.helper.Comum.RemoveCaracteres;


public class MainActivity extends AppCompatActivity {

    //  private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuarioAutenticacao;

    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference.child("pontos").setValue(100);

        toolbar = findViewById(R.id.toolbar_principal);

        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);
        usuarioAutenticacao = FirebaseAuth.getInstance();

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        slidingTabLayout.setDistributeEvenly(true);

        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:
                DeslogarUsuario();
                return true;

            case R.id.item_configuracoes:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void DeslogarUsuario() {
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
