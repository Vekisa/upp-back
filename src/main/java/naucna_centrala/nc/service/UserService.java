package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean userExistUsername(String username){
        CustomUser u = userRepository.findUserByUsername(username);
        if(u != null)
            return true;
        return false;
    }

    public Boolean userExistEmail(String email){
        CustomUser u = userRepository.findUserByEmail(email);
        if(u != null)
            return true;
        return false;
    }

    public void save(CustomUser user){
        userRepository.save(user);
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
