package org.acme.hibernate.orm.panache;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CreaturePower extends PanacheEntityBase {

    static CreaturePower withName(String name) {
        CreaturePower p = new CreaturePower();
        p.name = name;
        return p;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    public Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creature_id")
    public Creature creature;

    public String name;

}
