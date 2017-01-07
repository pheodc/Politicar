package android.politicar.com.br.politicar.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.politicar.com.br.politicar.R;
import android.politicar.com.br.politicar.WebService.CarregaDadosWS;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    String url;
    String urlFace;
    EditText login;
    EditText senha;
    HashMap<String, String> params;
    LoginButton loginButton;
    int idUsuario;
    String URL = "http://52.40.192.10/Politicar/loginPoliticar.php";
    CarregaDadosWS carregaDados;

    private static final String SESSAO = "SessaoPoliticar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        SharedPreferences sessao = getSharedPreferences(SESSAO, MODE_PRIVATE);

        idUsuario = sessao.getInt("id", 0);
        Log.d("LogShared", String.valueOf(idUsuario));
        if(idUsuario > 0){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        login = (EditText) findViewById(R.id.editLogin);
        senha = (EditText) findViewById(R.id.editSenha);

        loginButton = (LoginButton) findViewById(R.id.btnFbLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email", "user_birthday"));

    }

    public void validaLogin(View view){
        params = new HashMap<String, String>();
        params.put("login", login.getText().toString());
        params.put("senha", senha.getText().toString());

        carregaDados = new CarregaDadosWS();
        carregaDados.loginVolley(URL, this, params, this);
    }

    public void abrirCadastro(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
