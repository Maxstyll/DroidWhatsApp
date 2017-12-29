package ray.droid.com.droidwhatsapp.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.model.Usuario;


public class LoginActivity extends AppCompatActivity {
    private String TAG = "Validador";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ViewGroup telaLogin;
    private ViewGroup telaValidador;
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

    private EditText codValidacao;
    private Button validar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome = findViewById(R.id.edt_nome);
        codPais = findViewById(R.id.edt_codPais);
        codRegiao = findViewById(R.id.edt_codRegiao);
        telefone = findViewById(R.id.edt_telefone);
        cadastrar = findViewById(R.id.btn_cadastrar);
        telaLogin = (ViewGroup) findViewById(R.id.TelaLogin);
        telaValidador = (ViewGroup) findViewById(R.id.TelaValidador);
        codValidacao = findViewById(R.id.edt_codValidacao);
        validar = findViewById(R.id.btn_validar);

        SimpleMaskFormatter simpleMaskCodPais = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskCodPais = new MaskTextWatcher(codPais, simpleMaskCodPais);

        SimpleMaskFormatter simpleMaskCodRegiao = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskCodRegiao = new MaskTextWatcher(codRegiao, simpleMaskCodRegiao);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        SimpleMaskFormatter simpleMaskCodValidacao = new SimpleMaskFormatter("NNNNNN");
        MaskTextWatcher maskCodValidacao = new MaskTextWatcher(codValidacao, simpleMaskCodValidacao);

        codPais.addTextChangedListener(maskCodPais);
        codRegiao.addTextChangedListener(maskCodRegiao);
        telefone.addTextChangedListener(maskTelefone);
        codValidacao.addTextChangedListener(maskCodValidacao);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        codPais.getText().toString() +
                                codRegiao.getText().toString() +
                                telefone.getText().toString();

                String telefonSemFormatacao = telefoneCompleto.replace("+", "").replace("-", "");

                telaLogin.setVisibility(View.GONE);
                telaValidador.setVisibility(View.VISIBLE);

                startPhoneNumberVerification(telefonSemFormatacao);

            }
        });


        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = codValidacao.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    codValidacao.setError("Não pode ser em branco");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                // signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    public void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FireBase.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "LOGIN FEITO COM SUCESSO", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();

                            Usuario usuario = new Usuario();
                            usuario.setId(user.getUid());
                            usuario.setTelefone(user.getPhoneNumber().toString());
                            usuario.setNome(nome.getText().toString());
                            usuario.Salvar();

                        } else {

                            /*
                            String erroExcessao = "";
;
                            try
                            {
                                throw task.getException();

                            }
                            catch (FirebaseAuthInvalidUserException e)
                            {
                                erroExcessao= "Usuário desabilitado";
                            }
                            catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                erroExcessao= "Dados do usuário inválido";
                            }

                            catch (FirebaseAuthUserCollisionException e)
                            {
                                erroExcessao= "Usuário já existe cadastrado";

                            } catch (Exception e) {
                                erroExcessao= "Erro ao efetuar o cadastro";
                                e.printStackTrace();
                            }

                            Toast.makeText(LoginActivity.this, "Erro: " + erroExcessao, Toast.LENGTH_SHORT).show();
*/
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this, "Erro ao efetuar o registro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

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
