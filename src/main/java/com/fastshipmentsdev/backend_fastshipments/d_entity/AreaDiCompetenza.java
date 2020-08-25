package com.fastshipmentsdev.backend_fastshipments.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "area_di_competenza")
public class AreaDiCompetenza {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idArea;

    @Column(nullable = false)
    private String provincia;

    @OneToMany(mappedBy = "areaDiCompetenza")
    @JsonIgnore
    private List<Dipendente> trasportatori = new LinkedList<>();

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<Dipendente> getTrasportatori() {
        return trasportatori;
    }

    public void setTrasportatori(List<Dipendente> trasportatori) {
        this.trasportatori = trasportatori;
    }
}
