package QueryBinder.Annotation;

import QueryBinder.Request.HttpRequestMethods;

import java.lang.annotation.*;

/*
 * This annotation is used to bind a url to a method.
 * URL 값을 지정하기 위한 어노테이션.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.TYPE)
public @interface QueryBindingUrl {
    /*
     * URL 값을 지정한다.
     */
    String value() default "";
}
