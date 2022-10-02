package com.cos.blog.controller;

import com.cos.blog.domain.RoleType;
import com.cos.blog.domain.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class DummyController {

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
        }
        return "삭제되었습니다. id : " + id;
    }

    @Transactional // 함수 종료시 자동 commit, 더티 체킹 = 영속성 컨텍스트에 있던 user 오브젝트가 값이 바뀌어 있으므로 update 실행, 변경사항이 없으면 아무 행동x
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
        System.out.println("id : " + id);
        System.out.println("password : " + requestUser.getPassword());
        System.out.println("email : " + requestUser.getEmail());

        User user = userRepository.findById(id).orElseThrow(() -> {return new IllegalArgumentException("수정에 실패하였습니다.");});
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
        //userRepository.save(user);

        //더티 체킹
        return user;
    }

    @GetMapping("/dummy/select/all")
    public List<User> list() {
        return userRepository.findAll();
    }

    // 한 페이지당 2건의 데이터 출력
    @GetMapping("/dummy/user")
    public Page<User> pageList(@PageableDefault(size = 2, sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> pageingUser = userRepository.findAll(pageable);

        List<User> users = pageingUser.getContent();
        return pageingUser;
    }

    // http://localhost:8000/blog/dummy/user/3
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id) {
        // return문에 null이 반환되는 것을 막도록 findbyid메소드는 optional<t>로 감싸서 null을 판단
        User user = userRepository.findById(id).orElseThrow(() -> {return new IllegalArgumentException("해당 사용자는 없습니다.");});

        // 자바 오브젝트 리턴시 MessageConveter가 Jackson 라이브러리를 호출해 user 오브젝트를 json 형태로 브라우저에게 던져줌.
        return user;
    }

    @PostMapping("/dummy/join")
    public String join(User user) {
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
}
