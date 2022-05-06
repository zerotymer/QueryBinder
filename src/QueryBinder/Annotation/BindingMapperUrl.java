package QueryBinder.Annotation;

import QueryBinder.Request.HttpRequestMethods;

import java.lang.annotation.*;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.TYPE)
public @interface BindingMapperUrl {
    String value() default "";
    HttpRequestMethods method() default HttpRequestMethods.GET;
}
