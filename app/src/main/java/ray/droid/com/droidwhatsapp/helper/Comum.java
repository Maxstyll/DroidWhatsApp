package ray.droid.com.droidwhatsapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ray.droid.com.droidwhatsapp.model.Usuario;


/**
 * Created by Robson on 04/01/2018.
 */

public class Comum {
    private Context context;

    public static ArrayList<Usuario> ListarContatos(Context context) {

        final ArrayList<Usuario> contatos = new ArrayList<>();

        try {

            String[] PROJECTION = new String[]{ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);

            if (c.moveToFirst()) {
                String clsPhoneName = null;
                String clsphoneNo = null;

                do {
                    clsPhoneName = RemoveCaracteres(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    clsphoneNo = RemoveCaracteres(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    System.out.println(clsPhoneName + " " + clsphoneNo);
                    Usuario contato = new Usuario();
                    contato.setNome(clsPhoneName);
                    contato.setTelefone(clsphoneNo);
                    contatos.add(contato);
                } while (c.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }

    public static String RemoveCaracteres(String valor){
        return valor.replace("(", "").replace(")","").replace("-", "").replace("+55", "").replace("&", "").replace("|", "").replace(" ", "");
    }
}
