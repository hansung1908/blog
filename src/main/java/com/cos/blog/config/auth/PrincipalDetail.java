package com.cos.blog.config.auth;

import com.cos.blog.domain.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션 저장소에 저장
@Data
public class PrincipalDetail implements UserDetails {

    private User user; // 컴포지션

    public PrincipalDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료됬는지 확인 (true : 만료x)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨잇는지 확인 (true : 잠김x)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료됬는지 확인 (true : 만료x)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 확인 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정이 갖고 있는 권한 목록을 리턴 (권한이 여러개일수도 있어서 루프를 돌아야 하는데 우리는 하나만)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {return "ROLE_" + user.getRole();}); // ROLE_USER

        return collectors;
    }
}
