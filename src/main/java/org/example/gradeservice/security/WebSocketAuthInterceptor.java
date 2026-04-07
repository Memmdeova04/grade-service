
package org.example.gradeservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Key key = Keys.hmacShaKeyFor(secret.getBytes());
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key).build()
                            .parseClaimsJws(token).getBody();

                    Long userId = Long.valueOf(claims.getSubject());
                    String role = claims.get("role", String.class);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userId, null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                    accessor.setUser(auth);
                    log.info("WebSocket auth uğurlu - userId: {}", userId);
                } catch (Exception e) {
                    log.warn("WebSocket auth uğursuz: {}", e.getMessage());

                }
            }
        }
        return message;
    }
}