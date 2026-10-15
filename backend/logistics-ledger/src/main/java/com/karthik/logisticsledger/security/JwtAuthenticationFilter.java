package com.karthik.logisticsledger.security;

import com.karthik.logisticsledger.context.TenantContext;
import com.karthik.logisticsledger.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        try {

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                Claims claims = jwtService.extractAllClaims(token);

                Long userId = Long.valueOf(claims.getSubject());

                Long tenantId = claims.get("tenantId", Long.class);

                String role = claims.get("role", String.class);

                TenantContext.setTenantId(tenantId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );

                //this tells ss this req is authenticated
                SecurityContextHolder.getContext().setAuthentication(authentication);


            }
            filterChain.doFilter(request,response);
        }finally {
            TenantContext.clear();
        }



    }
}
