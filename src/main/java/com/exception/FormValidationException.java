package com.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FormValidationException extends ValidationException {
    Set<ConstraintViolation<Object>> violations = new HashSet<>();
    private String message;
    public FormValidationException(Set<ConstraintViolation<Object>> violations){
        super("error:" + violations.stream().map(v->{
            return "[" + v.getPropertyPath() + "]:" + v.getMessageTemplate();
        }).collect(Collectors.joining("\n"))
        );
        this.violations = violations;
    }

    @Override
    public String toString(){
        return super.getMessage();
    }
}
