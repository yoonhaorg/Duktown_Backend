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
    private User user; //유저


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "check_user_id", nullable = false)
    private User checkUser; //사생회

    @Column(nullable = false)
    private LocalDate date;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "cleaning")
    private List<CleaningUnit> cleaningUnits = new ArrayList<>(); //유닛그룹

    @Builder.Default
    private Boolean cleaned = false;

    @Builder.Default
    private Boolean checked = false;

    public void updateCleaned(){
        this.cleaned = true;
    }

    public void updateChecked(){
        this.checked =true;
    }

    // 비즈니스 로직 //
    public static Cleaning createCleaning(LocalDate newDate, User user){
        Cleaning cleaning = new Cleaning();
        cleaning.date = newDate;
        cleaning.user = user;
        cleaning.checkUser = user;
        return cleaning;
    }


}
