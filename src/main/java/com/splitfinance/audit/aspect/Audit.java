package com.splitfinance.audit.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audit {
    // Describe the operation (e.g., "CREATE", "UPDATE", "DELETE")
    String operation() default "";
    
    // The resource being affected (e.g., "Expense", "User")
    String resource() default "";
    
    // Optional additional details (if left blank the aspect may fill in method info)
    String details() default "";
}
