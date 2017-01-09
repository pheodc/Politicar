package android.politicar.com.br.politicar.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.politicar.com.br.politicar.R;
import android.politicar.com.br.politicar.WebService.CarregaDadosWS;
import android.politicar.com.br.politicar.WebService.CustomJsonObjectRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    String url;
    String urlFace;
    EditText login;
    EditText senha;
    HashMap<String, String> params;
    LoginButton loginButton;
    //int idUsuario;

    String nomeFace;
    String idFace;
    String emailFace;
    String birthdayFace;
    String generoFace;

    String nomeUsuario;
    String emailUsuario;
    String urlImagem;
    String cadastroFacebook;
    String idusuarioFacebbok;
    String generoUsuario;
    int idUsuario;

    RequestQueue rq;

    CallbackManager callbackManager;

    String URL = "http://52.40.192.10/Politicar/loginPoliticar.php";
    String URLFACE = "http://52.40.192.10/Politicar/cadastroFacebook.php";
    CarregaDadosWS carregaDados;

    private static final String SESSAO = "SessaoPoliticar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        rq = Volley.newRequestQueue(this);
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

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.btnFbLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email", "user_birthday"));

        botaoFace();

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

    public void botaoFace(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Profile profile = Profile.getCurrentProfile();
                // Log.v("Script Facebook:",profile.getName());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.v("LoginActivity", response.toString());
                                params = new HashMap<String, String>();
                                // Application code
                                try {
                                    nomeFace = object.getString("name");

                                    Log.v("Script_Face", nomeFace);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    emailFace = object.getString("email");
                                    //Log.v("Script_Face", emailFace);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    idFace = object.getString("id");

                                    Log.v("Script_Face", idFace);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    birthdayFace = object.getString("birthday"); // 01/31/1980 format

                                    Log.v("Script_Face", birthdayFace);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    generoFace = object.getString("gender"); // 01/31/1980 format

                                    Log.v("Script_Face", generoFace);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                params.put("nomeCompleto", nomeFace);
                                params.put("login", emailFace);
                                params.put("idFace", idFace);
                                params.put("dataNascimento", birthdayFace);
                                params.put("genero", generoFace);
                                //Log.v("Script_Face", emailFace);

                                //carregaDados = new CarregaDadosWS();
                                //carregaDados.loginFace(URLFACE, LoginActivity.this, params, LoginActivity.this);

                                //RequestQueue rq = Volley.newRequestQueue(this);
                                CustomJsonObjectRequest requestFace = new CustomJsonObjectRequest(Request.Method.POST, URLFACE, params, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        SharedPreferences sessao = getSharedPreferences(SESSAO, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sessao.edit();
                                        Log.i("Script, ", "SUCCESS" + response);
                                        try {

                                            String resposta = response.getString("message");


                                            if (resposta.equals("Ok")) {

                                                idUsuario = response.getInt("id");
                                                nomeUsuario = response.getString("nomeUsuario");
                                                emailUsuario = response.getString("loginUsuario");
                                                urlImagem = response.getString("imagemUsuario");
                                                generoUsuario = response.getString("generoUsuario");
                                                cadastroFacebook = response.getString("cadastroFacebook");

                                                editor.putInt("id", idUsuario);
                                                editor.putString("nomeUsuario", nomeUsuario);
                                                editor.putString("loginUsuario", emailUsuario);
                                                editor.putString("imagemUsuario", urlImagem);
                                                editor.putString("cadastroFacebook", cadastroFacebook);
                                                editor.commit();

                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                idUsuario = response.getInt("id");
                                                nomeUsuario = response.getString("nomeUsuario");
                                                emailUsuario = response.getString("loginUsuario");
                                                generoUsuario = response.getString("generoUsuario");
                                                urlImagem = response.getString("imagemUsuario");
                                                cadastroFacebook = response.getString("cadastroFacebook");

                                                editor.putInt("id", idUsuario);
                                                editor.putString("nomeUsuario", nomeUsuario);
                                                editor.putString("loginUsuario", emailUsuario);
                                                editor.putString("imagemUsuario", urlImagem);
                                                editor.putString("generoUsuario", generoUsuario);
                                                editor.putString("cadastroFacebook", cadastroFacebook);
                                                editor.commit();

                                                Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Erro na Conexão", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                requestFace.setTag("tag");
                                rq.add(requestFace);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
