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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.activity.ConversaActivity;
import ray.droid.com.droidwhatsapp.adapter.ContatoAdapter;
import ray.droid.com.droidwhatsapp.adapter.ConversaAdapter;
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.model.Conversa;
import ray.droid.com.droidwhatsapp.model.Usuario;

import static ray.droid.com.droidwhatsapp.helper.Comum.ListarContatos;
import static ray.droid.com.droidwhatsapp.helper.Comum.RemoveCaracteres;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    private ListView lv_conversas;
    private ArrayAdapter adapter;
    private ArrayList<Conversa> conversas;
    private ArrayList<Conversa> listaConversas;
    private String usuarioAut;
    private ValueEventListener eventListenerConversas;


    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        FireBase.getFireBaseConversas().addValueEventListener(eventListenerConversas);
        System.out.println("EventListener addValueEventListener");
    }

    @Override
    public void onStop() {
        super.onStop();
        FireBase.getFireBaseConversas().removeEventListener(eventListenerConversas);
        System.out.println("EventListener removeEventListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        lv_conversas = (ListView) view.findViewById(R.id.lv_conversas);


        conversas = new ArrayList<>();
        usuarioAut = RemoveCaracteres(FireBase.getUsuarioAutenticado());

        eventListenerConversas = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds1.getChildren()) {

                        Conversa conversa = ds2.getValue(Conversa.class);

                        if (conversa != null && conversa.getIdUsuario() != null) {


                            String idUsuario = RemoveCaracteres(conversa.getIdUsuario());


                            if (!idUsuario.equals(usuarioAut)) {
                                conversas.add(conversa);
                            }
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

                adapter = new ConversaAdapter(getActivity(), conversas);
                lv_conversas.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        lv_conversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Conversa conversa = conversas.get(i);
                intent.putExtra("nome", conversa.getNome());
                intent.putExtra("telefone", conversa.getIdUsuario());
                startActivity(intent);
            }
        });


        return view;
    }

}
