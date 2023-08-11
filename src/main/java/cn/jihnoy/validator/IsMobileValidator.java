package cn.jihnoy.validator;

import cn.jihnoy.util.ValidataUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;
    public void initialize(IsMobile constraintAnnotation){
        required = constraintAnnotation.required();
    }

    public boolean isValid(String value, ConstraintValidatorContext context){
        if(required){
            return ValidataUtil.isMobile(value);
        }else{
            if(StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidataUtil.isMobile(value);
            }
        }

    }

}
