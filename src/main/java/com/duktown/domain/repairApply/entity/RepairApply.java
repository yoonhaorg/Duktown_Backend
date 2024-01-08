package com.duktown.domain.repairApply.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "repair_apply")
public class RepairApply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "repair_apply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    //기숙사 종류 (국제/가온1관/가온2관..)
    private HallName hallName;

    //기숙사 호수
    private String roomNumber;

    @Column(columnDefinition = "longtext", nullable = false)
    //수리요청 내용
    private String content;

    //
    private Boolean checked;

    //
    private Boolean solved;

    public void update(String title, String content){
        this.roomNumber =roomNumber;
        this.content =content;
    }

    public void check(){
        this.checked = true;
    }

    public void solve(){
        this.solved = true;
    }
}
