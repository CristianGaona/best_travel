package com.best.travel.best_travel.aspects;

import java.io.IOException;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.best.travel.best_travel.util.BestTravelUtil;
import com.best.travel.best_travel.util.anotation.Notify;

@Component
@Aspect
public class NotifyAspect {
    
    @After(value = "@annotation(com.best.travel.best_travel.util.anotation.Notify)")
    public void notifyInFile(JoinPoint joinPoint) throws IOException {
        var args = joinPoint.getArgs();
        var size = args[1];
        var order = args[2] == null ? "NONE" : args[2];
        
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        var anotation = method.getAnnotation(Notify.class);
        var text = String.format(LINE_FORMAT, LocalDateTime.now(), anotation.value(), size.toString(), order.toString());
        BestTravelUtil.writeNotification(text, PATH);
    }

    private static String LINE_FORMAT = "AT %s new request %s, with size page %s an order %s";
    private static String PATH = "files/notify.txt";
}
