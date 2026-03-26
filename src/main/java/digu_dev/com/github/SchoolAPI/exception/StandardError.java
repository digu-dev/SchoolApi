package digu_dev.com.github.SchoolAPI.exception;

import java.time.Instant;

public record StandardError(Instant timestamp,
    Integer status,
    String error,
    String message,
    String path) {

        

}
