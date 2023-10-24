package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.drill.domain.Course;
import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.PostDto;
import project.drill.repository.CourseRepository;
import project.drill.repository.MemberRepository;
import project.drill.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    @Override
    public Post save(PostDto postDto) {
        Optional<Member> member = memberRepository.findByMemberNickname(postDto.getMemberNickname());
        Optional<Course> course = courseRepository.findById(postDto.getCourseId());
        Post post = Post.builder()
                .postId(0L)
                .member(member.get())
                .center(postDto.getCenter())
                .postContent(postDto.getPostContent())
                .postVideo(postDto.getPostVideo())
                .postWriteTime(LocalDateTime.now())
                .course(course.get())
                .postThumbnail(postDto.getPostThumbnail())
                .build();
        return postRepository.save(post);
    }

    @Override
    public Post read(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.get();
    }

    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<Post> findAllByMemberEmail(String memberEmail, int page, int size) {
        Optional<Member> member = memberRepository.findById(memberEmail);
        String nickname = member.get().getMemberNickname();
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> myPostPage = postRepository.findAllByMemberMemberNickname(pageable, nickname);
        return myPostPage;
    }

    @Override
    public Page<Post> findAllByOrder(String order, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = null;
        switch (order) {
            case "liked":
                postPage = postRepository.findByLiked(pageable);
            break;
            case "new" :
                postPage = postRepository.findAllByOrderByPostWriteTimeDesc(pageable);
                break;
            case "old" :
                postPage = postRepository.findAllByOrderByPostWriteTimeAsc(pageable);
                break;
        }
    return postPage;}
}



