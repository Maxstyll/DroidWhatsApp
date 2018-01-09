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

import ray.droid.com.droidwhatsapp.R;
import ray.droid.com.droidwhatsapp.model.Conversa;
import ray.droid.com.droidwhatsapp.model.Usuario;

/**
 * Created by Robson on 08/01/2018.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;
    public ConversaAdapter(@NonNull Context c, @NonNull ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = null;

       if (conversas != null)
       {
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
           view = inflater.inflate(R.layout.lista_simples, parent, false);
           TextView nomeContato = view.findViewById(R.id.tv_titulo);
           TextView conversaContato = view.findViewById(R.id.tv_subtitulo);
           Conversa conversa = conversas.get(position);
           nomeContato.setText(conversa.getNome());
           conversaContato.setText(conversa.getMensagem());
       }

       return view;
    }
}
