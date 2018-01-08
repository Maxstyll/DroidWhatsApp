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
import ray.droid.com.droidwhatsapp.helper.FireBase;
import ray.droid.com.droidwhatsapp.model.Mensagem;

/**
 * Created by Robson on 08/01/2018.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private ArrayList<Mensagem> mensagens;
    private Context context;

    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.mensagens = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (mensagens != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            Mensagem mensagem = mensagens.get(position);
            if (FireBase.getUsuarioAutenticado().equals(mensagem.getIdUsuario()))
            {
                view = inflater.inflate(R.layout.lista_mensagem_direita, parent, false);
            }
            else {
                view = inflater.inflate(R.layout.lista_mensagem_esquerda, parent, false);
            }

            TextView edt_mensagem = view.findViewById(R.id.tv_mensagem);

            edt_mensagem.setText(mensagem.getMensagem());
        }

        return view;
    }
}
