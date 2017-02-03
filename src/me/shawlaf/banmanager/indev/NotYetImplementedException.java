package me.shawlaf.banmanager.indev;

/**
 * Created by Florian on 03.01.2017.
 */
public class NotYetImplementedException extends RuntimeException {
    
    @Override
    public String getMessage() {
        Throwable rootCause = this;
    
        while(rootCause.getCause() != null &&  rootCause.getCause() != rootCause)
            rootCause = rootCause.getCause();
        
        StackTraceElement rootTraceElement = rootCause.getStackTrace()[0];
        
        return "Method " + rootTraceElement.getMethodName() + " in " + rootTraceElement.getClassName() + " is not yet implemented";
    }
}
