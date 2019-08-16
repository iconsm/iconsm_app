package com.example.alaiapp.Model;

public class Acto {

    private String Uid_acto;
    private String name_acto;
    private String name_participante;

    public Acto() {
    }

    public String getName_participante() {
        return name_participante;
    }

    public void setName_participante(String name_participante) {
        this.name_participante = name_participante;
    }

    public String getName_acto() {
        return name_acto;
    }

    public void setName_acto(String name_acto) {
        this.name_acto = name_acto;
    }

    public String getUid_acto() {
        return Uid_acto;
    }

    public void setUid_acto(String Uid_acto) {
        this.Uid_acto = Uid_acto;
    }

    @Override
    public String toString() {
        return name_participante;
    }
}
