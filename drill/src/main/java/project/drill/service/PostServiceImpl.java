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
    public Boolean save(PostDto postDto) {
        Optional<Member> member = memberRepository.findByMemberNickname(postDto.getMemberNickname());
        Optional<Course> course = courseRepository.findByCourseNameAndCenterAndIsNewIsTrue(postDto.getCourseName(),
            Center.valueOf(postDto.getCenter()));
        List<Post> findPost = postRepository.findByMemberMemberNicknameAndCourseCourseNameAndCourseIsNewIsTrue(postDto.getMemberNickname(),postDto.getCourseName());
        if(!findPost.isEmpty()){
            return false;
        }
        else{
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
            postRepository.save(post);
        return true;
        }
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
                    if(entirePostPageDto.getDifficulty().equals("difficulty0")){
                        if(entirePostPageDto.getCourseName().equals("course0")){
                    courses = courseRepository.findCourseNameByIsNewIsTrue();
                    postPage = postRepository.findAllByOrderByPostWriteTimeDesc(pageable);}
                    else{
                        courses =courseRepository.findCourseNameByIsNewIsTrue();
                        postPage= postRepository.findByCourseNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCourseName());}
                    }
                    else{
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByDifficultyOrderByPostWriteTimeDesc(pageable, Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage= postRepository.findByCourseNameOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getCourseName());
                        }
                    }
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        if(entirePostPageDto.getCourseName().equals("course0")){
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()));}
                        else{
                            courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                            postPage = postRepository.findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()), entirePostPageDto.getCourseName());
                        }
                    }else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
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
                    if(entirePostPageDto.getDifficulty().equals("difficulty0")){
                        if(entirePostPageDto.getCourseName().equals("course0")){
                        courses = courseRepository.findCourseNameByIsNewIsTrue();
                        postPage = postRepository.findByLiked(pageable);}
                    else{
                        courses = courseRepository.findCourseNameByIsNewIsTrue();
                        postPage = postRepository.findByCourseNameByLiked(pageable, entirePostPageDto.getCourseName());
                        }
                    }
                    else{
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findByDifficultyOrderByLiked(pageable,entirePostPageDto.getDifficulty());
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findByCourseNameByLiked(pageable, entirePostPageDto.getCourseName());
                        }
                    }
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        if(entirePostPageDto.getCourseName().equals("course0")){
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findByCenterNameOrderByLiked(pageable, entirePostPageDto.getCenterName());}
                        else{
                            courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                            postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName());
                        }
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
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
                    if(entirePostPageDto.getDifficulty().equals("difficulty0")){
                        if(entirePostPageDto.getCourseName().equals("course0")){
                        courses = courseRepository.findCourseNameByIsNewIsTrue();
                        postPage = postRepository.findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(pageable,entirePostPageDto.getMemberNickname());
                    }
                    else{
                            courses = courseRepository.findCourseNameByIsNewIsTrue();
                            postPage = postRepository.findByCourseNameAndMemberNicknameOrderByPostWriteTimeDesc(pageable ,entirePostPageDto.getCourseName() ,entirePostPageDto.getMemberNickname());
                    }
                    }
                    else{
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByMemberMemberNicknameAndDifficultyOrderByPostWriteTimeDesc(pageable, entirePostPageDto.getMemberNickname(),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findByCourseNameAndMemberNicknameOrderByPostWriteTimeDesc(pageable ,entirePostPageDto.getCourseName() ,entirePostPageDto.getMemberNickname());
                        }
                    }
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        if(entirePostPageDto.getCourseName().equals("course0")){
                        courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                        postPage = postRepository.findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(pageable, Center.valueOf(entirePostPageDto.getCenterName()),nickname);
                    }else{
                            courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                            postPage = postRepository.findByCourseNameAndMemberNicknameOrderByPostWriteTimeDesc(pageable,entirePostPageDto.getCourseName() ,nickname);
                        }
                    }else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
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
                    if(entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        if (entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByIsNewIsTrue();
                            postPage = postRepository.findByMemberMemberNicknameAndLiked(pageable, entirePostPageDto.getMemberNickname());
                        }
                        else {
                            courses = courseRepository.findCourseNameByIsNewIsTrue();
                            postPage = postRepository.findByCourseNameAndMemberNicknameByLiked(pageable , entirePostPageDto.getCourseName(),entirePostPageDto.getMemberNickname());
                        }
                    }
                    else{
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findByMemberNicknameAndDifficultyOrderByLiked(pageable,entirePostPageDto.getMemberNickname(),entirePostPageDto.getDifficulty());
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findByCourseNameAndMemberNicknameByLiked(pageable , entirePostPageDto.getCourseName(),entirePostPageDto.getMemberNickname());
                        }
                    }
                } else {
                    if (entirePostPageDto.getDifficulty().equals("difficulty0")) {
                        if (entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()));
                            postPage = postRepository.findByCenterNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getMemberNickname());

                        }
                        else{
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName(),nickname);
                        }
                    } else {
                        if (!entirePostPageDto.getDifficulty().equals("difficulty0") && entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getDifficulty(),nickname);
                        } else if (!entirePostPageDto.getCourseName().equals("course0")) {
                            courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(entirePostPageDto.getCenterName()),Difficulty.valueOf(entirePostPageDto.getDifficulty()));
                            postPage = postRepository.findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(pageable, entirePostPageDto.getCenterName(), entirePostPageDto.getCourseName(),nickname);
                        }

                    }
                }} }
        PostPageAndCourseListDto postPageAndCourseListDto = PostPageAndCourseListDto.builder()
                .postPage(postPage)
                .courseNameList(courses)
                .build();
        return postPageAndCourseListDto;

    }

    @Override
    public List<String> findCourseName(UploadPostDto uploadPostDto) {
        List<String> courses = null;
        if (uploadPostDto.getCenterName().equals("center0")) {
            if(uploadPostDto.getDifficulty().equals("difficulty0")){
                courses = courseRepository.findCourseNameByIsNewIsTrue();}
            else{
                courses = courseRepository.findCourseNameByDifficultyAndIsNewIsTrue(Difficulty.valueOf(uploadPostDto.getDifficulty()));
            }
        }
        else {
            if (uploadPostDto.getDifficulty().equals("difficulty0")) {
                courses = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(uploadPostDto.getCenterName()));
            } else {
                courses = courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(uploadPostDto.getCenterName()),Difficulty.valueOf(uploadPostDto.getDifficulty()));
            }
        }
        return courses;
    }
}



