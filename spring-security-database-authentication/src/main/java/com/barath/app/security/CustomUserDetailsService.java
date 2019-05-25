package com.barath.app.security;

import com.barath.app.entity.User;
import com.barath.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserDetailsImpl userDetails=null;
        if(!StringUtils.isEmpty(userName)){
            if(logger.isInfoEnabled()){
                logger.info("Finding the user with user name {}",userName);
            }
              User user=userRepository.findByUserName(userName);
            if (user == null) {
                throw new UsernameNotFoundException(userName);
            }
            userDetails=new UserDetailsImpl(user);
        }

        return userDetails;
    }

    @PostConstruct
    public void preloadDefaultUsers(){
        userRepository.save(new User("barath",passwordEncoder.encode("barath")));
        userRepository.save(new User("test",passwordEncoder.encode("test")));
    }
}
