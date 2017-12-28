package ray.droid.com.droidwhatsapp.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codValidacao = findViewById(R.id.edt_codValidacao);
        validar = findViewById(R.id.btn_validar);

        SimpleMaskFormatter simpleMaskCodValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskCodValidacao = new MaskTextWatcher(codValidacao, simpleMaskCodValidacao);

        codValidacao.addTextChangedListener(maskCodValidacao);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();
                String tokenGerado = usuario.get("token");
                String tokenDigitado = codValidacao.getText().toString();

                if (tokenDigitado.equals(tokenGerado))
                {
                    Toast.makeText(ValidadorActivity.this, "Token Validado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ValidadorActivity.this, "Token Incorreto", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
