package ray.droid.com.droidwhatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import ray.droid.com.droidwhatsapp.Manifest;
import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.helper.Permissao;
import ray.droid.com.droidwhatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {
    private EditText nome;
    private EditText telefone;
    private EditText codPais;
    private EditText codRegiao;
    private Button cadastrar;
    private String[] permissoesNecessarias = new String[]
            {
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.INTERNET


            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.ValidaPermissoes(1, this, permissoesNecessarias);


        nome = findViewById(R.id.edt_nome);
        codPais = findViewById(R.id.edt_codPais);
        codRegiao = findViewById(R.id.edt_codRegiao);
        telefone = findViewById(R.id.edt_telefone);
        cadastrar = findViewById(R.id.btn_cadastrar);

        SimpleMaskFormatter simpleMaskCodPais = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskCodPais = new MaskTextWatcher(codPais, simpleMaskCodPais);

        SimpleMaskFormatter simpleMaskCodRegiao = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskCodRegiao = new MaskTextWatcher(codRegiao, simpleMaskCodRegiao);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        codPais.addTextChangedListener(maskCodPais);
        codRegiao.addTextChangedListener(maskCodRegiao);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        codPais.getText().toString() +
                                codRegiao.getText().toString() +
                                telefone.getText().toString();

                String telefonSemFormatacao = telefoneCompleto.replace("+", "").replace("-", "");

                //  Log.i("Telefone", telefonSemFormatacao);
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;

                String token = String.valueOf(numeroRandomico);
                //   Log.i("Telefone", token);

                String mensagemEnvio = "Whatsapp Código de Confirmação: " + token;

                Preferencias preferencias = new Preferencias(getApplicationContext());
                preferencias.SalvarUsuarioPreferencias(nomeUsuario, telefonSemFormatacao, token);

                /*
                HashMap<String, String>  usuario = preferencias.getDadosUsuario();
                 Log.i("TOKEN", "Token " + usuario.get("token") +
                         " Nome " + usuario.get("nome") +
                         " Telefone " + usuario.get("telefone") );

                  */

                // Envio de SMS
                boolean enviadoSM = EnviaSMS("+" + telefonSemFormatacao, mensagemEnvio);

                if (enviadoSM)
                {
                    Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Problema ao enviar o SMS", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private boolean EnviaSMS(String telefone, String mensagens) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagens, null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResult);

        for (int resultado : grandResult) {
            if (resultado == PackageManager.PERMISSION_DENIED) {
                AlertaValidacaoPermissao();

            }
        }
    }

    private void AlertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
