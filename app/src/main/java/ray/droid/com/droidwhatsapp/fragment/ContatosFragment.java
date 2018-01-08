package ray.droid.com.droidwhatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.activity.ConversaActivity;
import ray.droid.com.droidwhatsapp.adapter.ContatoAdapter;
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.model.Usuario;

import static ray.droid.com.droidwhatsapp.helper.Comum.ListarContatos;
import static ray.droid.com.droidwhatsapp.helper.Comum.RemoveCaracteres;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView lv_contatos;
    private ArrayAdapter adapter;
    private ArrayList<Usuario> contatos;
    private ArrayList<Usuario> listaContatos;
    private String usuarioAut ;
    private ValueEventListener eventListenerContatos;



    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        FireBase.getFireBaseUsers().addValueEventListener(eventListenerContatos);
        System.out.println("EventListener addValueEventListener");
    }

    @Override
    public void onStop() {
        super.onStop();
        FireBase.getFireBaseUsers().removeEventListener(eventListenerContatos);
        System.out.println("EventListener removeEventListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        lv_contatos = (ListView) view.findViewById(R.id.lv_contatos);



        contatos = new ArrayList<>();
        listaContatos = ListarContatos (getContext());
        usuarioAut = RemoveCaracteres(FireBase.getUsuarioAutenticado());

        eventListenerContatos = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contatos.clear();
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    Usuario usuario = ds1.getValue(Usuario.class);

                    if (usuario != null) {

                        for (Usuario contatoTelefone: listaContatos) {

                            String contatoTel = RemoveCaracteres(contatoTelefone.getTelefone());
                            String usuarioApp = RemoveCaracteres(usuario.getTelefone());

                            if  (contatoTel.equals(usuarioApp)){
                                if (!usuarioApp.equals(usuarioAut)) {
                                    if (!contatos.contains(usuario)) {
                                        contatos.add(usuario);
                                    }
                                }
                            }

                            System.out.println("Compara " + contatoTelefone.getNome() + " - " + RemoveCaracteres(contatoTelefone.getTelefone()) + " = " + RemoveCaracteres(usuario.getTelefone()));

                        }

                    }
                }
/*
                adapter = new ArrayAdapter(
                        getActivity(),
                        R.layout.lista_contato,
                        contatos

                );


*/

                adapter = new ContatoAdapter(getActivity(), contatos);
                lv_contatos.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


      lv_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Intent intent = new Intent(getActivity(), ConversaActivity.class);

              Usuario contato = contatos.get(i);
              intent.putExtra("nome", contato.getNome());
              intent.putExtra("telefone", contato.getTelefone());
              startActivity(intent);
          }
      });


        return view;
    }


}
