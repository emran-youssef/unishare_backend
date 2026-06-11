package com.unishare.unishare.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EduEmailValidator.class)        //connects the annotation to the validator
@Target(ElementType.FIELD)                               //specify where we want to apply the annotation
@Retention(RetentionPolicy.RUNTIME)                     //when this annotation apply -runtime
public @interface EduEmail {

    String message() default "Only ZUJ student emails are allowed (@std-zuj.edu.jo)";
    Class<?> [] groups() default {};
    Class<?extends Payload>[] payload() default {   };
}
