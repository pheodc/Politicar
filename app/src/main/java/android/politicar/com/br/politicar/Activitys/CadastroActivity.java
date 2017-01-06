package android.politicar.com.br.politicar.Activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.politicar.com.br.politicar.R;
import android.politicar.com.br.politicar.WebService.CarregaDadosWS;
import android.politicar.com.br.politicar.WebService.CustomJsonObjectRequest;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class CadastroActivity extends AppCompatActivity {
    public static final int IMAGEM_INTERNA = 12;
    ImageView imgUser;
    EditText nomeCompleto;
    EditText dataNascimento;
    Spinner genero;
    EditText login;
    EditText senha;
    EditText confirmaSenha;
    RequestQueue rq;
    HashMap<String, String> params;
    String url;
    Bitmap image;
    String imagem;
    String[] listaGenero = new String[]{"Masculino", "Feminino"};
    String generoString;
    ProgressDialog carregando;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        url = "http://52.40.192.10/Politicar/cadastroPoliticar.php";
        rq = Volley.newRequestQueue(this);

        imgUser = (ImageView) findViewById(R.id.imageView_foto);
        nomeCompleto = (EditText) findViewById(R.id.editNome);
        dataNascimento = (EditText) findViewById(R.id.editNascimento);
        genero = (Spinner) findViewById(R.id.editGenero);
        login = (EditText) findViewById(R.id.editLogin);
        senha = (EditText) findViewById(R.id.editSenha);
        confirmaSenha = (EditText) findViewById(R.id.editconfirmaSenha);

        /*dataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });*/

        listarGenero();
    }

    public void voltarLogin(View view){
        finish();
    }

    public void adicionaImagem(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Selecione a Imagem"), IMAGEM_INTERNA);

    }

    public void cadastrarUsuario(View view){

        cadastroVolley();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGEM_INTERNA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgUser.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImagem(Bitmap bmp){
        String encodedImage = null;
        if(bmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //Log.i("Script", encodedImage);

        }
        return encodedImage;
    }
    public void cadastroVolley(){

        imagem = getStringImagem(image);

        if(imagem == null){
            imagem = "";
        }
        Log.i("Script", generoString);
        Log.i("Script, ", "IMAGE" + imagem);
        params = new HashMap<String, String>();
        params.put("image", imagem);
        params.put("nomeCompleto", nomeCompleto.getText().toString());
        params.put("dataNascimento", dataNascimento.getText().toString());
        params.put("genero", generoString);
        params.put("login", login.getText().toString());
        params.put("senha", senha.getText().toString());
        params.put("confirma", confirmaSenha.getText().toString());

        CarregaDadosWS carregaCadastro = new CarregaDadosWS();
        carregaCadastro.cadastroVolley(url, this, params, this);
    }

    public void listarGenero(){
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaGenero);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genero.setAdapter(adapterGenero);
        genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generoString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /*public void abrirCalendario(){

        initDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar cMax = Calendar.getInstance();
        cMax.set(1999, 12, 31);
        datePickerDialog.setMaxDate(cMax);
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private void initDateData(){
        if(year == 0){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = 0;
        dataNascimento.setText("");
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int yearSet, int monthOfYearSet, int dayOfMonthSet) {
        year = yearSet;
        month = monthOfYearSet;
        day = dayOfMonthSet;

        dataNascimento.setText(day + "/" + (month + 1) + "/" + year);

    }*/

}



