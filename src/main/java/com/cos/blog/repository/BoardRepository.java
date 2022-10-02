package com.cos.blog.repository;

import com.cos.blog.domain.Board;
import com.cos.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}