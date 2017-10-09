package oi.digital.chatbot.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oi.digital.chatbot.client.billing.Billing;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 09/10/17.
 */
@Aspect
@Component
@Slf4j
public class SampleClassAspect {

    @Pointcut("execution(* oi.digital.chatbot.service.ChatBotService.msgBot(..))")
    public void servicePointCut(){

    }

    @Pointcut("execution(* oi.digital.chatbot.controller.ChatBotController.chatbot(..))")
    public void controllerPointCut(){

    }

    //Method 1 to use aspect class intercepting a method called.
    @Around("servicePointCut() || controllerPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        Billing billing = new Billing();
        billing.setCodigoDeBarras("After");
        logDispatch(billing);
        return proceed;
    }

    //Method 2 to use aspect class intercepting a method called.
    @AfterReturning("execution(* oi.digital.chatbot.controller.ChatBotController.chatbot(..))")
    public void controllerPointCut(JoinPoint joinPoint) throws JsonProcessingException {
        Billing billing = new Billing();
        billing.setCodigoDeBarras("afterReturning");
        logDispatch(billing);
    }

    @Async
    private void logDispatch(Billing billing) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.debug(objectMapper.writeValueAsString(billing));
    }


}
