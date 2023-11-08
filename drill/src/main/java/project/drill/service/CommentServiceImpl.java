package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.drill.domain.Comment;
import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;
import project.drill.repository.CommentRepository;
import project.drill.repository.MemberRepository;
import project.drill.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    @Override
    public void save(CommentDto commentDto) {
        Optional<Member> member =memberRepository.findByMemberNickname(commentDto.getMemberNickname());
        if(!member.isPresent())
            throw new NullPointerException("해당하는 멤버 없음");
        Optional<Post> post = postRepository.findById(commentDto.getPostId());
        if(!post.isPresent())
            throw new NullPointerException("글이 존재하지 않습니다.");
        Comment comment = Comment.builder()
                .commentId(0L)
                .post(post.get())
                .member(member.get())
                .commentContent(commentDto.getCommentContent())
                .commentWriteTime(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }
    @Override
    public void delete(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(!comment.isPresent())
            throw new NullPointerException("해당 댓글이 없습니다.");
        Optional<Post> post = postRepository.findById(comment.get().getPost().getPostId());
        if(!post.isPresent())
            throw new NullPointerException("글이 존재하지 않습니다.");
        commentRepository.deleteById(commentId);
    }
    @Override
    public List<CommentListDto> getCommentList(Long postId) {
        List<CommentListDto> commentList = commentRepository.findAllByPostPostIdOrderByCommentWriteTimeAsc(postId);
        return commentList;
    }
}
