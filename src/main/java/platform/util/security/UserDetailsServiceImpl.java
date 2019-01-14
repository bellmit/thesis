package platform.util.security;

import platform.model.DPUser;
import platform.persistence.DPUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private platform.persistence.DPUserRepository DPUserRepository;


    public UserDetailsServiceImpl(DPUserRepository DPUserRepository) {
        this.DPUserRepository = DPUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DPUser DPUser = DPUserRepository.findByUserName(username);
        if (DPUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(DPUser.getUserName(), DPUser.getPassword(), emptyList());
    }


}