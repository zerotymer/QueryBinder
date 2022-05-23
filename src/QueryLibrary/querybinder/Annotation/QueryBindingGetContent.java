package querylibrary.querybinder.Annotation;

import java.lang.annotation.*;

/**
 * 쿼리 정의시 data 값이 있는 곳을 지정하는 어노테이션
 * byte[]를 받는다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface QueryBindingGetContent {
}
