package com.titan.jnly.vector.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;

@Table(name = "TB_Species")
public class Species implements Serializable {

    @Column(isPKey = true)
    private String code;

    @Column
    private String species;

    @Column
    private String family;

    @Column
    private String genus;

    @Column
    private String iatin;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getIatin() {
        return iatin;
    }

    public void setIatin(String iatin) {
        this.iatin = iatin;
    }

    @Override
    public String toString() {
        return "Species{" +
                "code='" + code + '\'' +
                ", species='" + species + '\'' +
                ", family='" + family + '\'' +
                ", genus='" + genus + '\'' +
                ", iatin='" + iatin + '\'' +
                '}';
    }
}
