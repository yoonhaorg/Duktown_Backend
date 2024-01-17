package com.duktown.domain.cleaning.entity;

import com.duktown.domain.cleaningUnit.entity.CleaningUnit;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Cleaning {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cleaning_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "check_user_id", nullable = true)
    private User checkUser;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(fetch = LAZY, mappedBy = "cleaning")
    private List<CleaningUnit> cleaningUnits = new ArrayList<>();

    private Boolean cleaned;
    private Boolean checked;

    public void updateCleaned(){
        this.cleaned = true;
    }

    public void updateChecked(){
        this.checked =true;
    }
}
