package org.acme.hibernate.orm.panache;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Creature extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public Long id;
    
    public String name;

    @OneToMany(mappedBy = "creature", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CreaturePower> powers = new ArrayList<>();

    void addCreaturePower(CreaturePower p) {
        p.creature = this;
        powers.add(p);
    }

}
