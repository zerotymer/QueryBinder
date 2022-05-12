package QueryBinder.Annotation;

import java.lang.annotation.*;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({java.lang.annotation.ElementType.METHOD, ElementType.FIELD})
public @interface QueryBindingParam {

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


    // TODO: 컨버터 정의
}
