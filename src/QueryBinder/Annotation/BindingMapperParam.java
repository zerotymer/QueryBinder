package QueryBinder.Annotation;

import java.lang.annotation.*;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({java.lang.annotation.ElementType.METHOD, ElementType.FIELD})
public @interface RequestMapperParam {
    String name() default "";
    String defaultValue() default "";
    boolean required() default false;
}
