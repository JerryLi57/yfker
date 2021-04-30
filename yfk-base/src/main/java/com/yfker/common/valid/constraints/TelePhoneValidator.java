package com.yfker.common.valid.constraints;


import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 手机号的验证器
 * @author: lijiayu
 * @date: 2020-04-09 11:31
 **/
public class TelePhoneValidator implements ConstraintValidator<TelePhone, String> {

    private boolean required;

    @Override
    public void initialize(TelePhone constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            // 必输项为空时校验失败
            return !required;
        }
        boolean isValid;
        String regex = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        isValid = m.matches();
        return isValid;
    }

}