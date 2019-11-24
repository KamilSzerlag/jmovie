package co.brick.kszerlag.jmovie.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObjectIdValidator implements ConstraintValidator<MongoId, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return org.bson.types.ObjectId.isValid(value);
        } catch (Exception e) {
            return false;
        }
    }
}
