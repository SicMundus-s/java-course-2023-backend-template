package edu.java.scrapper.aop;

import edu.java.core.exception.BadRequestException;
import edu.java.scrapper.dao.jdbc.JdbcChatDao;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckRegistrationAspect {

    private final JdbcChatDao jdbcChatDao;

    @Pointcut("@annotation(edu.java.scrapper.aop.CheckRegistration)")
    public void checkRegistration() {
    }

    @Before("checkRegistration() && args(chatId,..)")
    public void beforeAdvice(JoinPoint joinPoint, Long chatId) {
        if (jdbcChatDao.isNotRegistered(chatId)) {
            throw new BadRequestException("User is not registered");
        }
    }
}
