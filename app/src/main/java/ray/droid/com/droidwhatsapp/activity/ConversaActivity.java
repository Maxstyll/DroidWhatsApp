package ray.droid.com.droidwhatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.adapter.MensagemAdapter;
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.model.Conversa;
import ray.droid.com.droidwhatsapp.model.Mensagem;
import ray.droid.com.droidwhatsapp.model.Usuario;

public class ConversaActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private String nome;
    private String telefone;
    private String telefoneAutenticado;
    private String nomeAutenticado;
    private EditText edt_mensagem;
    private ImageButton btn_enviar;
    private ListView lv_conversas;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListener;
    private EditText edt_remetente;
    private EditText edt_destinatario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);
        edt_mensagem = findViewById(R.id.edt_menssagem);
        btn_enviar = findViewById(R.id.btn_enviar);
        lv_conversas = findViewById(R.id.lv_conversas);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nome = extra.getString("nome");
            telefone = extra.getString("telefone");
        }
        telefoneAutenticado = FireBase.getUsuarioAutenticado();
        toolbar.setTitle(nome);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();

      //  adapter = new ArrayAdapter(ConversaActivity.this, android.R.layout.simple_list_item_1, mensagens);

        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);


        lv_conversas.setAdapter(adapter);


        FireBase.getFireBaseUsers().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.child(telefoneAutenticado).getValue(Usuario.class);
                nomeAutenticado = usuario.getNome();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        FireBase.getFireBaseMensagens().child(telefoneAutenticado).child(telefone).addValueEventListener(valueEventListener);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtMensagem = edt_mensagem.getText().toString();
                if (txtMensagem.isEmpty()) {
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                } else {
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(FireBase.getUsuarioAutenticado());
                    mensagem.setMensagem(txtMensagem);
                    Boolean retornoMensagemRemetente =  SalvarMensagem(telefoneAutenticado, telefone, mensagem);

                    if (!retornoMensagemRemetente)
                    {
                        Toast.makeText(ConversaActivity.this, "Problema ao ennviar a mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Boolean retornoMensagemDestinatario = SalvarMensagem(telefone, telefoneAutenticado, mensagem);
                        if (!retornoMensagemDestinatario)
                        {
                            Toast.makeText(ConversaActivity.this, "Problema ao ennviar a mensagem, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(telefone);
                    conversa.setNome(nome);
                    conversa.setMensagem(txtMensagem);

                   Boolean retornoConversaRemetente =  SalvarConversa(telefoneAutenticado, telefone, conversa );

                    if (!retornoConversaRemetente)
                    {
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar a conversa, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        conversa = new Conversa();
                        conversa.setIdUsuario(telefoneAutenticado);
                        conversa.setNome(nomeAutenticado);
                        conversa.setMensagem(txtMensagem);

                        Boolean retornoConversaDestinatario = SalvarConversa(telefone, telefoneAutenticado, conversa);
                        if (!retornoConversaDestinatario)
                        {
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar a conversa, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    edt_mensagem.setText("");
                }
            }
        });
    }


    private boolean SalvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {

            FireBase.getFireBaseMensagens().child(idRemetente).child(idDestinatario).push().setValue(mensagem);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean SalvarConversa(String idRemetente, String idDestinatario, Conversa conversa) {
        try {

            FireBase.getFireBaseConversas().child(idRemetente).child(idDestinatario).setValue(conversa);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FireBase.getFireBaseMensagens().child(telefoneAutenticado).child(telefone).removeEventListener(valueEventListener);
    }
}
