package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类AutoFillAspect，统一拦截加入了AutoFill注解的方法，通过反射为公共字段赋值
 */

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")

    public void autoFillPointer(){

    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointer()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始公共字段的自动填充");
        //获取当前拦截的方法上的数据库操作类型
        MethodSignature signature=(MethodSignature) joinPoint.getSignature(); //方法签名对象
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType=autoFill.value();//获得数据库操作类型
        /**
         * 通过这一系列操作，获得了@AutoFill(value = OperationType.INSERT)
         */
        //获取当前被拦截的方法的参数 实体参数
        Object[] args = joinPoint.getArgs();
        if (args==null||args.length==0){
            return;
        }
        Object arg = args[0];
        //准备赋值的数据
        LocalDateTime now=LocalDateTime.now();
        Long currentID= BaseContext.getCurrentId();

        //根据当前不同的数据类型，为对应的属性通过反射进行赋值
        if(operationType==OperationType.INSERT){
            //为四个公共字段赋值
            try {
                Method setCreateTime=arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(arg,now);
                setCreateUser.invoke(arg,currentID);
                setUpdateTime.invoke(arg,now);
                setUpdateUser.invoke(arg,currentID);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(operationType==OperationType.UPDATE){
            try {
                Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值

                setUpdateTime.invoke(arg,now);
                setUpdateUser.invoke(arg,currentID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
