package project.drill.config.auth;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.drill.domain.Member;

public class MemberDetail implements UserDetails {
  private Member member;

  public MemberDetail(Member member){
    this.member = member;
  }

  public Member getMember() {
    return member;
  }

  @Override
  public String getUsername() {
    return member.getMemberEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(()->member.getRole().name());
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }
}
