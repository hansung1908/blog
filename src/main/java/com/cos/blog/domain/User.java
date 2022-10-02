package com.cos.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@DynamicInsert // insert할 때 NULL은 필드에서 제외
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에 연결된 DB의 넘버링 전략을 따라감
    private int id; // 시퀸스

    @Column(unique = true, nullable = false, length = 100)
    private String username; // 아이디

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    //@ColumnDefault("user")
    //DB는 RoleType이라는게 없다.
    @Enumerated(EnumType.STRING)
    private RoleType role; // Enum 쓰는게 좋다, ADMIN USER

    private String oauth; // kakao, google

    @CreationTimestamp // 시간 자동 입력
    private Timestamp createDate;
}
