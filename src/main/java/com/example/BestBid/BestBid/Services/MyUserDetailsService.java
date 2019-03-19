package com.example.BestBid.BestBid.Services;

import com.example.BestBid.BestBid.Models.MyUserPrincipal;
import com.example.BestBid.BestBid.Models.User;
import com.example.BestBid.BestBid.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.getUserByUserName(username);
        System.out.println(user.get().toString());
        if (!user.isPresent() || user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user.get());
    }
}
