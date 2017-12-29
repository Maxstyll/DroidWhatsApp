package ray.droid.com.droidwhatsapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import ray.droid.com.droidwhatsapp.helper.FireBase;

/**
 * Created by Robson on 29/12/2017.
 */

public class Usuario {
    private String id;
    private String nome;
    private String telefone;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void Salvar() {
        FireBase.getFireBaseUsers().child(String.valueOf(getTelefone())).setValue(this);
    }
}
