package com.cos.blog.service;

import com.cos.blog.domain.RoleType;
import com.cos.blog.domain.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User find(String username) {
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            return new User();
        });
        return user;
    }

    @Transactional
    public void join(User user) {
        String rawPassword = user.getPassword(); // 비밀번호 원문
        String encPassword = encoder.encode(rawPassword); // 해쉬
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        // 수정시에는 영속성 컨텍스트 user 오브젝트를 영속화시키고 영속화된 오브젝트를 수정
        // select를 해서 user오브젝트 db로부터 가져오는 이유는 영속화를 위해
        // 영속화된 오브젝트를 변경하면 자동으로 db에 update문 전송
        User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원 찾기 실패");
        });

        // validate 체크해서 oauth 필드에 값이 없으면 수정 가능
        if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }

        // 회원 수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = 자동 commit
        // 영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update 전송
    }
}
