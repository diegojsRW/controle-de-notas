package com.example.diegosilva.prova1;

import android.app.*;
import android.content.*;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.*;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import com.example.diegosilva.prova1.utils.SQLiteMyAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.SimpleFormatter;


public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);

        if(getIntent().getBooleanExtra("QUITFLAG", false)){
            finish();
            return;
        }
//        Button btnAccess = (Button)findViewById(R.id.btnAccess);
//        btnAccess.setOnClickListener(new View.OnClickListener() { public void onClick(View v){ LoginActivity.this.tryLogin(v);} });

    }
    public void tryLogin(View view){
        EditText txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        EditText txtSenha = (EditText)findViewById(R.id.txtSenha);

        String usuario = txtUsuario.getText().toString().toLowerCase();
        String senha = txtSenha.getText().toString();

        if(usuario.equals("diego") && senha.equals("qwerty")){
            Intent inte = new Intent(this, HomeActivity.class);
            startActivity(inte);
        } else Toast.makeText(getApplicationContext(), "Usuário ou senha inválidos.", Toast.LENGTH_LONG).show();
    }

    public void reseta(View v){
        SQLiteMyAdapter adp = new SQLiteMyAdapter(this);
        adp.close();
        deleteDatabase(adp.getDatabaseName());
    }
    public String toByteUnit(long bytes){
        String[] un = new String[]{"byte", "KB", "MB", "GB", "TB"};
        int DV = (int)Math.floor(Math.log(bytes) / Math.log(1024));
        double nmb = bytes / Math.pow(1024, DV);

        return new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR"))).format(nmb) + " " + un[DV] + (bytes == 1 ? "s" : "");
    }
    public void copia(View v){
        SQLiteMyAdapter adp = new SQLiteMyAdapter(this);

        adp.close();

        try {
            File sd = Environment.getExternalStorageDirectory();
            if(sd.canWrite()) {
                File bkp = new File(sd, adp.getDatabaseName());
                FileChannel fDB = new FileInputStream(getDatabasePath(adp.getDatabaseName())).getChannel();
                FileChannel fcopy = new FileOutputStream(bkp, false).getChannel();
                long transferred = fcopy.transferFrom(fDB, 0, fDB.size());
                fDB.close();
                fcopy.close();
                Toast.makeText(this, "Banco de dados copiado. " + this.toByteUnit(transferred) + " em arquivo transferido para o SDCard", Toast.LENGTH_LONG).show();
            }
            else {
                throw new IOException("Permissão de escrita negada.");
            }
        }
        catch(Exception e){
            new AlertDialog.Builder(this)
                    .setTitle("Erro ao copiar")
                    .setMessage("Não foi possível copiar o banco pois: " +
                            e.getMessage() + "\r\n\r\n" +
                            "Caminho do BD: " + getDatabasePath(adp.getDatabaseName()).getAbsolutePath() + "\r\n\r\n" +
                            "Tentou copiar para: " + System.getenv("EXTERNAL_STORAGE"))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNeutralButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setCancelable(true).show();


        }//*/

    }
}
