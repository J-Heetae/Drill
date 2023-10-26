package project.drill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.drill.domain.*;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;
import project.drill.dto.ReadPostDto;
import project.drill.repository.*;


import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final LikedRepository likedRepository;
    private final CommentRepository commentRepository;

    @Override
    public Post save(PostDto postDto) {
        Optional<Member> member = memberRepository.findByMemberNickname(postDto.getMemberNickname());
        Optional<Course> course = courseRepository.findByCourseNameAndCenterAndIsNewIsTrue(postDto.getCourseName(),
            Center.valueOf(postDto.getCenter()));
        Post post = Post.builder()
                .postId(0L)
                .member(member.get())
                .center(Center.valueOf(postDto.getCenter()))
                .postContent(postDto.getPostContent())
                .postVideo(postDto.getPostVideo())
                .postWriteTime(LocalDateTime.now())
                .course(course.get())
                .postThumbnail(postDto.getPostThumbnail())
                .build();
        return postRepository.save(post);
    }

    @Override
    public ReadPostDto read(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        Long likedCount = likedRepository.countByPostPostId(postId);
        Long commentCount = commentRepository.countByPostPostId(postId);
        ReadPostDto readPostDto = ReadPostDto.builder()
                .memberNickname(post.get().getMember().getMemberNickname())
                .centerName(post.get().getCenter().toString())
                .postContent(post.get().getPostContent())
                .postVideo(post.get().getPostContent())
                .postWriteTime(post.get().getPostWriteTime())
                .courseName(post.get().getCourse().getCourseName())
                .likedCount(likedCount)
                .commentCount(commentCount)
                .build();
        return readPostDto;
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
                    postPage = postRepository.findAllByCenterOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()));
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName());
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
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                    }

                }
            } }
            return postPage;

    }
}



