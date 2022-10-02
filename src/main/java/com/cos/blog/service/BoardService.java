package com.cos.blog.service;

import com.cos.blog.domain.Board;
import com.cos.blog.domain.Reply;
import com.cos.blog.domain.User;
import com.cos.blog.dto.ReplySaveReqeustDto;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void write(Board board, User user) {
        board.setCount(0);
        board.setUser(user);
       boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board board(int id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Board> boardlist(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public void delete(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.")); // 영속화
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수 종료시 트랜잭션이 종료, 이때 더티 체킹 - 자동 업데이트, db flush
    }

    @Transactional
    public void replyWrite(ReplySaveReqeustDto replySaveReqeustDto) {
        int result = replyRepository.mSave(replySaveReqeustDto.getUserId(), replySaveReqeustDto.getBoardId(), replySaveReqeustDto.getContent());
        System.out.println(result); // 오브젝트를 출력하게되면 자동으로 toString()이 호출됨.
    }

    @Transactional
    public void replyDelete(int replyId) {
        replyRepository.deleteById(replyId);
    }
}
