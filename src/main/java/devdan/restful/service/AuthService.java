package devdan.restful.service;

import devdan.restful.config.TokenBlacklist;
import devdan.restful.entity.User;
import devdan.restful.model.request.LoginUserRequest;
import devdan.restful.model.response.TokenResponse;
import devdan.restful.repository.UserRepository;
import devdan.restful.resolver.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Transactional
    public TokenResponse login(LoginUserRequest request){
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong");
        }

        String token = jwtUtil.generatedToken(user.getUsername());
        Long expiredAt = jwtUtil.getExpirationTime(token);

        return TokenResponse.builder()
                .token(token)
                .expiredAt(expiredAt)
                .build();
    }

    @Transactional
    public void logout(HttpServletRequest request){
        String authHeader =  request.getHeader("Authorization");
        if (authHeader != null){
            tokenBlacklist.add(authHeader);
        }
    }

}
