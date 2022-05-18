package querylibrary.querybinder.Annotation;

import java.lang.annotation.*;

/**
 * 쿼리 정의시 값을 가져오기 위해 사용되는 어노테이션
 * @author 신현진
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface QueryBindingGetParam {

    /**
     * The name of the parameter.
     * @return The name of the parameter.
     */
    String value() default "";

    /**
     * The default value of the parameter.
     * @return default string value.
     */
    String defaultValue() default "";

    /**
     * The type of the parameter.
     * @return parameter type.
     */
    Class type() default String.class;

    /**
     * Is the parameter required?
     * @return true if the parameter is required.
     */
    boolean isRequired() default false;

    /**
     * Need Encoding?
     * @return
     */
    boolean isEncode() default false;


    // TODO: 컨버터 정의
}
