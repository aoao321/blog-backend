package com.aoao.blog.module.common.aspect;

import com.aoao.blog.module.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 自定义aop
 *
 * @author aoao
 * @create 2025-07-08-18:17
 */
@Aspect
@Slf4j
@Component
public class ApiOperationLogAspect {

    /** 以自定义 @ApiOperationLog 注解为切点，凡是添加 @ApiOperationLog 的方法，都会执行环绕中的代码 */
    @Pointcut("@annotation(com.aoao.blog.module.common.aspect.ApiOperationLog)")
    public void apiOperationLog() {}

    /** 打印请求相关参数
     log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} ====== ",
     description, argsJsonStr, className, methodName);

     log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== ",
     description, executionTime, JsonUtil.toJsonString(result));
    */
    @Around("apiOperationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 请求开始时间
            long startTime = System.currentTimeMillis();

            // MDC
            MDC.put("traceId", UUID.randomUUID().toString());

            // 获取被请求的类和方法
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // 获取入参
            Object[] args = joinPoint.getArgs();
            // 转换成json
            String arg = JsonUtil.toJson(args);

            // 获取描述信息
            String description = getDescription(joinPoint);

            // 执行切点方法
            Object result = joinPoint.proceed();
            log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} ====== ",
                    description, arg, className, methodName);

            // 结束计算消耗时间
            long endTime = System.currentTimeMillis();

            log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} ====== ",
                    description, (endTime-startTime), JsonUtil.toJson(result));

            return result;
        } finally {
            MDC.remove("traceId");
        }
    }



    /**
     * 获取自定义注解上的description属性
     */
    private String getDescription(ProceedingJoinPoint joinPoint) {
        // 1. 从 ProceedingJoinPoint 获取方法签名 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 2. 获取真正的Method
        Method method = signature.getMethod();
        // 3. 获取注解
        ApiOperationLog annotation = method.getAnnotation(ApiOperationLog.class);
        // 4. 获取description属性
        return annotation.description();
    }
}
