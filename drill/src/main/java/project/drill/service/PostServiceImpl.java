package project.drill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.drill.domain.*;
import project.drill.dto.*;
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
    public ReadPostDto read(String memberNickname , Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        Long likedCount = likedRepository.countByPostPostId(postId);
        Long commentCount = commentRepository.countByPostPostId(postId);
        Optional<Liked> liked = likedRepository.findByPostPostIdAndMemberMemberNickname(postId, memberNickname);
        boolean isLiked = true;
        if(!liked.isPresent()){
            isLiked = false;
        }
        ReadPostDto readPostDto = ReadPostDto.builder()
                .memberNickname(post.get().getMember().getMemberNickname())
                .centerName(post.get().getCenter().toString())
                .postContent(post.get().getPostContent())
                .postVideo(post.get().getPostVideo())
                .postWriteTime(post.get().getPostWriteTime())
                .courseName(post.get().getCourse().getCourseName())
                .likedCount(likedCount)
                .commentCount(commentCount)
                .isLiked(isLiked)
                .build();
        return readPostDto;
    }


    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }



    @Override
    public PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto) {
        Page<PostPageDto> postPage = null;
        Pageable pageable = PageRequest.of(entirePostPageDto.getPage(), entirePostPageDto.getSize());
        String nickname = entirePostPageDto.getMemberNickname();
        List<String> courses = null;
        if(entirePostPageDto.getMemberNickname().equals("")){
            if (entirePostPageDto.getOrder().equals("new")) {
                if (entirePostPageDto.getCenterName().equals("center0")) {
                    courses = courseRepository.findCourseNameByIsNewIsTrue();
                    postPage = postRepository.findAllByOrderByPostWriteTimeDesc(pageable);
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()));
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName());
                        }

                    }
                }
            } else {
                if (entirePostPageDto.getCenterName().equals("center0")) {
                    courses = courseRepository.findCourseNameByIsNewIsTrue();
                    postPage = postRepository.findByLiked(pageable);
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findByCenterNameOrderByLiked(pageable, entirePostPageDto.getCenterName());
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameDifficultyOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty());
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                        }

                    }
                } }}
        // 닉네임을 입력 받았을 경우
        else{
            if (entirePostPageDto.getOrder().equals("new")) {
                if (entirePostPageDto.getCenterName().equals("center0")) {
                    courses = courseRepository.findCourseNameByIsNewIsTrue();
                    postPage = postRepository.findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(pageable,nickname);
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()),nickname);
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterAndCourseDifficultyAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), Difficulty.valueOf(entirePostPageDto.getDifficulty()),nickname);
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterAndCourseCourseNameAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName(),nickname);
                        }

                    }
                }
            } else {
                if (entirePostPageDto.getCenterName().equals("center0")) {
                    courses = courseRepository.findCourseNameByIsNewIsTrue();
                    postPage = postRepository.findByMemberNicknameAndLiked(pageable,nickname);
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        postPage = postRepository.findByCenterNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(),nickname);
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("all")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty(),nickname);
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName(),nickname);
                        }

                    }
                } }}
        PostPageAndCourseListDto postPageAndCourseListDto = PostPageAndCourseListDto.builder()
                .postPage(postPage)
                .courseNameList(courses)
                .build();
        return postPageAndCourseListDto;

    }
}



