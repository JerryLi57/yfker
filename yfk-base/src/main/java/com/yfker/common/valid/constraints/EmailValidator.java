package com.yfker.common.valid.constraints;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 邮箱的验证器
 * @author: jerry
 * @date: 2020-06-08 11:31
 **/
public class EmailValidator implements ConstraintValidator<Email, String> {

    private boolean required;

    @Override
    public void initialize(Email constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            // 必输项为空时校验失败
            return !required;
        }
        boolean isValid;
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        isValid = m.matches();
        return isValid;
    }

}