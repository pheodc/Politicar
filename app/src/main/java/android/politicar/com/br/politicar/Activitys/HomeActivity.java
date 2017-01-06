package android.politicar.com.br.politicar.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.politicar.com.br.politicar.R;

public class HomeActivity extends AppCompatActivity {
    private static final String SESSAO = "SessaoPoliticar";
    String nomeUsuario;
    SharedPreferences sessao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbHome);
        setSupportActionBar(toolbar);
        sessao = getSharedPreferences(SESSAO, MODE_PRIVATE);
        nomeUsuario = sessao.getString("nomeUsuario", "");
        toolbar.setTitle("Politicar");
        toolbar.setSubtitle(nomeUsuario);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void sairApp(View view) {
        sessao = getSharedPreferences(SESSAO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sessao.edit();
        editor.clear().commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
