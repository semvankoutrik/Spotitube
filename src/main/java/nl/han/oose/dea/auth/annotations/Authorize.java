package nl.han.oose.dea.auth.annotations;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@NameBinding
public @interface Authorize {
}
