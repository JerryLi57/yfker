package com.yfker.common.valid.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: 用于校验电子邮箱的注解
 * @author: jerry
 * @date: 2020-06-08 11:27
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {EmailValidator.class})
@Documented
public @interface Email {

    String message() default "邮箱格式不正确！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}
