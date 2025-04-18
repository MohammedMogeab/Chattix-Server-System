package com.example.chatwebsite.Security.Service;
import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.Security.UserPricipal;
import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {
@Autowired
private UserRepositry userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<User> optionalUser = userRepo.findByUsername(username);
      User users = optionalUser.get();
      if(users == null){
          System.out.println("user not found");
          throw new UsernameNotFoundException("user not found");
      }
      return new UserPricipal(users);
    }
}
