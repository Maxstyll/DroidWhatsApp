package ray.droid.com.droidwhatsapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Robson on 19/06/2017.
 */

public class FireBase {

    private static DatabaseReference databaseReferenceUsers;
    private static DatabaseReference databaseReferenceMensagens;
    private static DatabaseReference databaseReferenceConversas;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFireBaseUsers() {
        if (databaseReferenceUsers == null) {
            databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("Usuario/");
        }
        return databaseReferenceUsers;
    }

    public static DatabaseReference getFireBaseMensagens() {
        if (databaseReferenceMensagens == null) {
            databaseReferenceMensagens = FirebaseDatabase.getInstance().getReference("Mensagens/");
        }
        return databaseReferenceMensagens;
    }

    public static DatabaseReference getFireBaseConversas() {
        if (databaseReferenceConversas == null) {
            databaseReferenceConversas = FirebaseDatabase.getInstance().getReference("Conversas/");
        }
        return databaseReferenceConversas;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static String getUsuarioAutenticado() {
        return getFirebaseAuth().getCurrentUser().getPhoneNumber();
    }

    public static String getUsuarioNomeAutenticado() {
        return getFirebaseAuth().getCurrentUser().getDisplayName();
    }


}
