package android.politicar.com.br.politicar.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.politicar.com.br.politicar.Activitys.HomeActivity;
import android.politicar.com.br.politicar.Activitys.LoginActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by pheli on 16/12/2016.
 */

public class CarregaDadosWS {
    ProgressDialog carregando;
    String nomeUsuario;
    String emailUsuario;
    String urlImagem;
    String cadastroFacebook;
    String idusuarioFacebbok;
    String generoUsuario;
    int idUsuario;




    HashMap<String, String> params;

    private static final String SESSAO = "SessaoPoliticar";

    public interface ResultListener {
        public void onResult(JSONObject resultado) throws JSONException;
    }

    private ResultListener listener;

    public void cadastroVolley(String url, final Context context, Map<String, String> params, final Activity activity) {
        RequestQueue rq = Volley.newRequestQueue(context);


        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Script, ", "SUCCESS" + response);
                try {

                    String resposta = response.getString("message");
                    if (resposta.equals("Ótimo, Seu Cadastro está Pronto, Divirta-se!")) {
                        carregando = ProgressDialog.show(context, "Criando seu Cadastro", "Carregando...!", false, true);
                        carregando.setCancelable(false);
                        //Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();
                        activity.finish();
                    } else {
                        Toast.makeText(context, resposta, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "ERRO COM O NOSSO SERVIDOR, DESCULPE!", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("tag");
        rq.add(request);

    }

    public void setResultListener(ResultListener listener) {
        this.listener = listener;
    }

    public void loginVolley(String url, final Context context, Map<String, String> params, final Activity activity) {
        RequestQueue rq = Volley.newRequestQueue(context);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Script, ", "SUCCESS" + response);
                try {

                    String resposta = response.getString("message");


                    if (resposta.equals("Ok")) {
                        carregando = ProgressDialog.show(context, "Verificando seu Login", "Carregando...!", false, true);
                        carregando.setCancelable(false);
                        idUsuario = response.getInt("id");
                        nomeUsuario = response.getString("nomeUsuario");
                        emailUsuario = response.getString("loginUsuario");
                        idusuarioFacebbok = response.getString("senhaUsuario");
                        urlImagem = response.getString("imagemUsuario");
                        cadastroFacebook = response.getString("cadastroFacebook");
                        //Log.i("Script, ", "ID " + idUsuario);
                        SharedPreferences sessao = activity.getSharedPreferences(SESSAO, MODE_PRIVATE);


                        SharedPreferences.Editor editor = sessao.edit();
                        editor.putInt("id", idUsuario);
                        editor.putString("nomeUsuario", nomeUsuario);
                        editor.putString("loginUsuario", emailUsuario);
                        editor.putString("senhaUsuario", idusuarioFacebbok);
                        editor.putString("imagemUsuario", urlImagem);
                        editor.putString("cadastroFacebook", cadastroFacebook);
                        editor.commit();

                        Log.i("Script, ", " " + idUsuario);
                        Log.i("Script, ", "ID_SHARED " + nomeUsuario);
                        Log.i("Script, ", "ID_SHARED " + emailUsuario);
                        Log.i("Script, ", "ID_SHARED " + idusuarioFacebbok);
                        Log.i("Script, ", "ID_SHARED " + urlImagem);
                        Log.i("Script, ", "ID_SHARED " + cadastroFacebook);
                        Intent intent = new Intent(context, HomeActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Toast.makeText(context.getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "ERRO COM O NOSSO SERVIDOR, DESCULPE!", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("tag");
        rq.add(request);
    }

    public void loginFace(String url, final Context context, Map<String, String> params, final Activity activity) {
        RequestQueue rq = Volley.newRequestQueue(context);
        CustomJsonObjectRequest requestFace = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences sessao = activity.getSharedPreferences(SESSAO, MODE_PRIVATE);
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

                        Intent intent = new Intent(activity, HomeActivity.class);
                        context.startActivity(intent);
                        activity.finish();
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

                        Toast.makeText(context.getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, HomeActivity.class);
                        context.startActivity(intent);
                        activity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "Erro na Conexão", Toast.LENGTH_SHORT).show();
            }
        });
        requestFace.setTag("tag");
        rq.add(requestFace);
    }
}



