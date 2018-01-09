package ray.droid.com.droidwhatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.model.Usuario;

/**
 * Created by Robson on 08/01/2018.
 */

public class ContatoAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> contatos;
    private Context context;
    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Usuario> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = null;

       if (contatos != null)
       {
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
           view = inflater.inflate(R.layout.lista_simples, parent, false);
           TextView nomeContato = view.findViewById(R.id.tv_titulo);
           TextView telefoneContato = view.findViewById(R.id.tv_subtitulo);
           Usuario contato = contatos.get(position);
           nomeContato.setText(contato.getNome());
           telefoneContato.setText(contato.getTelefone());
       }

       return view;
    }
}
