package querylibrary.querybinder.Annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to bind a url to a method.
 * URL 값을 지정하기 위한 어노테이션.
 * TYPE에서 값을 지정하여 넣는 경우, 값을 설정함.
 * Method나 Field에서 지정하여 넣는 경우, 값을 가져와서 넣는다.
 * @author 신현진
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface QueryBindingUrl {
    /*
     * URL 값을 지정한다.
     */
    String value() default "";
}
