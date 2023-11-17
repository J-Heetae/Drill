package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.drill.domain.Liked;
import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.LikedDto;
import project.drill.repository.LikedRepository;
import project.drill.repository.MemberRepository;
import project.drill.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedServiceImpl implements LikedService {
    private final LikedRepository likedRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    @Override
    public void save(LikedDto likedDto){
        Optional<Liked> findLiked = likedRepository.findByPostPostIdAndMemberMemberNickname(likedDto.getPostId(),likedDto.getMemberNickname());
        Optional<Post> post = postRepository.findById(likedDto.getPostId());
        Optional<Member> member = memberRepository.findByMemberNickname(likedDto.getMemberNickname());
        if(!findLiked.isPresent()){
        Liked liked = Liked.builder()
                .likedId(0L)
                .post(post.get())
                .member(member.get())
                .build();
            likedRepository.save(liked);}
        else{
            likedRepository.deleteById(findLiked.get().getLikedId());
        }

    }
}
