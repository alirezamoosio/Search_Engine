package ir.nimbo.searchengine.Listener;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CLI {
    String help() default "";
}
