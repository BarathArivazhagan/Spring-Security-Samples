package com.barath.app.security;

import com.barath.app.entity.User;
import com.barath.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserRepository userRepository;

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
}
