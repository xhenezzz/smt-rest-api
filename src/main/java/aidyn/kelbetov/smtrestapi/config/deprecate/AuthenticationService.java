//package aidyn.kelbetov.smtrestapi.config.deprecate;
//
//import aidyn.kelbetov.smtrestapi.config.MyUserDetailsService;
//import aidyn.kelbetov.smtrestapi.service.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//    private final MyUserDetailsService userDetailsService;
//
//
//    /**
//         * Аутентификация пользователя
//         *
//         * @param request данные пользователя
//         * @return токен
//         */
//        public JwtAuthenticationResponse signIn(SignInRequest request) {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    request.getUsername(),
//                    request.getPassword()
//            ));
//
//            var user = userDetailsService.loadUserByUsername(request.getUsername());
//            var jwt = jwtService.generateToken(user);
//            return new JwtAuthenticationResponse(jwt);
//        }
//}
