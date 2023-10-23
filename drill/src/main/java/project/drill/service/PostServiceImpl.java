package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.drill.domain.Course;
import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.PostDto;
import project.drill.repository.CourseRepository;
import project.drill.repository.MemberRepository;
import project.drill.repository.PostRepository;

import java.time.LocalDateTime;
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
                .postContent(postDto.getPostContent())
                .postVideo(postDto.getPostVideo())
                .postWriteTime(LocalDateTime.now())
                .course(course.get())
                .postThumbnail(postDto.getPostThumbnail())
                .build();
        return postRepository.save(post);
    }

}
