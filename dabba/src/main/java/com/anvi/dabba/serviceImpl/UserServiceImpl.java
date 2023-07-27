package com.anvi.dabba.serviceImpl;

import com.anvi.dabba.error.TiffinUtils;
import com.anvi.dabba.jwt.CustomerUserDetailsService;
import com.anvi.dabba.jwt.JwtFilter;
import com.anvi.dabba.jwt.JwtUtil;
import com.anvi.dabba.model.User;
import com.anvi.dabba.repo.UserRepo;
import com.anvi.dabba.service.UserService;
import com.anvi.dabba.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try{
            if(this.validateSignupMap(requestMap)){
                User user= userRepo.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepo.save(this.getUserFromMap(requestMap));
                    return TiffinUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                }else{
                    return TiffinUtils.getResponseEntity("Email exists",HttpStatus.BAD_REQUEST);
                }
            }else {
                return  TiffinUtils.getResponseEntity("Invalid Data",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        return  TiffinUtils.getResponseEntity("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private boolean validateSignupMap(Map<String, String> requestMap) {
        if(requestMap.containsKey("firstName")
                && requestMap.containsKey("lastName")
                && requestMap.containsKey("userName")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")){
            return true;
        }
        return  false;
    }
    private User getUserFromMap(Map<String, String> requestMap) {
        User user= new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setUserName(requestMap.get("userName"));
        user.setEmail(requestMap.get("email"));
//        user.setPassword(requestMap.get("password"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("true");
        user.setRole("user");
        return user;

    }
    @Override
    public ResponseEntity<String> login (Map<String,String>requestMap){
        log.info("Inside login");
        try{
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (requestMap.get("email"),requestMap.get("password")));
            if(authentication.isAuthenticated()){
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
//                    return  new ResponseEntity<String>("{\"token\":\""+
//                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
//                                    customerUserDetailsService.getUserDetail().getRole())+"\"}",HttpStatus.OK);
                    return  new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole())+"\",\"role\":\""+customerUserDetailsService.getUserDetail().getRole()+"\"}",HttpStatus.OK);

                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval"+"\"}",HttpStatus.BAD_REQUEST );
                }
            }
        }catch (Exception exception){
            log.error("{}",exception);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad credentials"+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userRepo.getAllUser(),HttpStatus.OK);
            }else
                return new ResponseEntity<>(userRepo.getByUsername(jwtFilter.getCurrentUser()),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
