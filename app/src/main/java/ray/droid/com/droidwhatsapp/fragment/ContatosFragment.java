package ray.droid.com.droidwhatsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;

import ray.droid.com.droidwhatsapp.R;
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
    private ArrayList<String> contatos;
    private ArrayList<Usuario> listaContatos;
    private String usuarioAut ;



    public ContatosFragment() {
        // Required empty public constructor
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

        FireBase.getFireBaseUsers().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    Usuario usuario = ds1.getValue(Usuario.class);

                    if (usuario != null) {

                        for (Usuario contatoTelefone: listaContatos) {

                            String contatoTel = RemoveCaracteres(contatoTelefone.getTelefone());
                            String usuarioApp = RemoveCaracteres(usuario.getTelefone());


                            if  (contatoTel.equals(usuarioApp)){
                                if (!usuarioApp.equals(usuarioAut)) {
                                    contatos.add(usuario.getNome());
                                }
                            }

                          //  System.out.println("Compara " + contatoTelefone.getNome() + " - " + RemoveCaracteres(contatoTelefone.getTelefone()) + " = " + RemoveCaracteres(usuario.getTelefone()));

                        }

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_contato,
                contatos

        );

        lv_contatos.setAdapter(adapter);

        return view;
    }

}
