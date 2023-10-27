package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto2;
import project.drill.dto.ReadPostDto;
import project.drill.repository.*;


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
    public Post save(PostDto2 postDto2) {
        Optional<Member> member = memberRepository.findByMemberNickname(postDto2.getMemberNickname());
        Optional<Course> course = courseRepository.findByCourseNameAndCenterAndIsNewIsTrue(postDto2.getCourseName(),
            Center.valueOf(postDto2.getCenter()));
        Post post = Post.builder()
                .postId(0L)
                .member(member.get())
                .center(Center.valueOf(postDto2.getCenter()))
                .postContent(postDto2.getPostContent())
                .postVideo(postDto2.getPostVideo())
                .postWriteTime(LocalDateTime.now())
                .course(course.get())
                .postThumbnail(postDto2.getPostThumbnail())
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
    public Page<Post> findAllByOrder(EntirePostPageDto entirePostPageDto) {
        Page<Post> postPage = null;
        Pageable pageable = PageRequest.of(entirePostPageDto.getPage(), entirePostPageDto.getSize());
        if (entirePostPageDto.getOrder().equals("new")) {
            if (entirePostPageDto.getCenterName().equals("center0")) {
                postPage = postRepository.findAllByOrderByPostWriteTimeDesc(pageable);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                    postPage = postRepository.findAllByCenterNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName());
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterNameAndCourseDifficultyOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                    } else if (!entirePostPageDto.getDifficulty().equals("difficulty0") && !entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterNameAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                    }

                }
            }
        } else {
            if (entirePostPageDto.getCenterName().equals("center0")) {
                postPage = postRepository.findByLiked(pageable);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                    postPage = postRepository.findByCenterNameOrderByLiked(pageable, entirePostPageDto.getCenterName());
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterCenterNameDifficultyOrdeyByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                    } else if (!entirePostPageDto.getDifficulty().equals("difficulty0") && !entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                    }

                }
            } }
            return postPage;

    }
}



