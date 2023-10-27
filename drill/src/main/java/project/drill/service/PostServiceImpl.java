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
<<<<<<< Updated upstream
import project.drill.dto.PostDto2;
=======
import project.drill.dto.PostDto;
import project.drill.dto.PostPageAndCourseListDto;
>>>>>>> Stashed changes
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
    public PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto) {
        Page<Post> postPage = null;
        Pageable pageable = PageRequest.of(entirePostPageDto.getPage(), entirePostPageDto.getSize());
        String nickname = entirePostPageDto.getMemberNickname();
        List<Course> courses = null;
        if(entirePostPageDto.getMemberNickname().equals("")){
            if (entirePostPageDto.getOrder().equals("new")) {
                if (entirePostPageDto.getCenterName().equals("center0")) {
                    courses = courseRepository.findAllByIsNewIsTrue();
                postPage = postRepository.findAllByOrderByPostWriteTimeDesc(pageable);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
<<<<<<< Updated upstream
                    postPage = postRepository.findAllByCenterNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName());
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterNameAndCourseDifficultyOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                    } else if (!entirePostPageDto.getDifficulty().equals("difficulty0") && !entirePostPageDto.getCourseName().equals("all")) {
                        postPage = postRepository.findAllByCenterNameAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
=======
                    courses = courseRepository.findAllByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                    postPage = postRepository.findAllByCenterOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()));
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName());
>>>>>>> Stashed changes
                    }

                }
            }
        } else {
            if (entirePostPageDto.getCenterName().equals("center0")) {
                courses = courseRepository.findAllByIsNewIsTrue();
                postPage = postRepository.findByLiked(pageable);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                    courses = courseRepository.findAllByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                    postPage = postRepository.findByCenterNameOrderByLiked(pageable, entirePostPageDto.getCenterName());
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
<<<<<<< Updated upstream
                        postPage = postRepository.findAllByCenterCenterNameDifficultyOrdeyByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                    } else if (!entirePostPageDto.getDifficulty().equals("difficulty0") && !entirePostPageDto.getCourseName().equals("all")) {
=======
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterCenterNameDifficultyOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
>>>>>>> Stashed changes
                        postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                    }

                }
            } }}
        // 닉네임을 입력 받았을 경우
        else{
        if (entirePostPageDto.getOrder().equals("new")) {
            if (entirePostPageDto.getCenterName().equals("center0")) {
                courses = courseRepository.findAllByIsNewIsTrue();
                postPage = postRepository.findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(pageable,nickname);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                    courses = courseRepository.findAllByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                    postPage = postRepository.findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()),nickname);
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndCourseDifficultyAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), Difficulty.valueOf(entirePostPageDto.getDifficulty()),nickname);
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndCourseCourseNameAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName(),nickname);
                    }

                }
            }
        } else {
            if (entirePostPageDto.getCenterName().equals("center0")) {
                courses = courseRepository.findAllByIsNewIsTrue();
                postPage = postRepository.findByMemberNicknameAndLiked(pageable,nickname);
            } else {
                if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                    courses = courseRepository.findAllByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                    postPage = postRepository.findByCenterNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(),nickname);
                } else {
                    if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty(),nickname);
                    } else if (!entirePostPageDto.getCourseName().equals("all")) {
                        courses = courseRepository.findAllByDifficultyAndCenterAndIsNewIsTrue( Difficulty.valueOf(entirePostPageDto.getDifficulty()),Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName(),nickname);
                    }

                }
            } }}
        PostPageAndCourseListDto postPageAndCourseListDto = PostPageAndCourseListDto.builder()
                .postPage(postPage)
                .courseList(courses)
                .build();
            return postPageAndCourseListDto;

    }
}



