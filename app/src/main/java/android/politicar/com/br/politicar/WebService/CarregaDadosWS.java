package android.politicar.com.br.politicar.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

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
    int idUsuario;

    CallbackManager callbackManager;
    LoginButton loginButton;
    String nomeFace;
    String idFace;
    String emailFace;
    String birthdayFace;
    private static final String SESSAO = "SessaoPoliticar";

    public interface ResultListener {
        public void onResult(JSONObject resultado) throws JSONException;
    }

    private ResultListener listener;

    public void cadastroVolley(String url, final Context context, Map<String,String> params, final Activity activity){
        RequestQueue rq = Volley.newRequestQueue(context);


        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Script, ", "SUCCESS" + response);
                try {

                    String resposta = response.getString("message");
                    if(resposta.equals("Ótimo, Seu Cadastro está Pronto, Divirta-se!")){
                        carregando = ProgressDialog.show(context,"Criando seu Cadastro","Carregando...!", false, true);
                        carregando.setCancelable(false);
                        //Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }else {
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
    public void setResultListener(ResultListener listener){
        this.listener = listener;
    }

    public void loginVolley(String url, final Context context, Map<String,String> params, final Activity activity){
        RequestQueue rq = Volley.newRequestQueue(context);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Script, ", "SUCCESS" + response);
                try {

                    String resposta = response.getString("message");


                    if(resposta.equals("Ok")){
                        carregando = ProgressDialog.show(context,"Verificando seu Login","Carregando...!", false, true);
                        carregando.setCancelable(false);
                        idUsuario =  response.getInt("id");
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
                    }else {
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
}

