package com.example.springboot_quartz.annotataion;

import com.example.springboot_quartz.mapper.customer.QrtzJobDetailsSelfMapper;
import com.example.springboot_quartz.model.dto.QrtzJobDetails;
import com.example.springboot_quartz.model.vo.JobDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;

/**
 * created by ${user} on 2019/7/18
 */
@Documented
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JobDtoContraint.JobDtoValator.class)
public @interface JobDtoContraint {
    String message() default "数据库中已经存在相同jobName+jobGroup的job";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    class JobDtoValator implements ConstraintValidator<JobDtoContraint, JobDto> {

        @Autowired
        QrtzJobDetailsSelfMapper qrtzJobDetailsSelfMapper;

        @Override
        public void initialize(JobDtoContraint constraintAnnotation) {

        }

        @Override
        public boolean isValid(JobDto jobDto, ConstraintValidatorContext constraintValidatorContext) {
            String jobName = jobDto.getJobName();
            String jobGroup = jobDto.getJobGroupName();
            QrtzJobDetails jobDetails = qrtzJobDetailsSelfMapper.getQrtzJobDetailsMapperByNameAndGroup(jobName, jobGroup);
            if (Objects.nonNull(jobDetails)){
                return false;
            }
            return true;
        }
    }

}
