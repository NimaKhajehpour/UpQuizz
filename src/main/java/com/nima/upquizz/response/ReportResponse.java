package com.nima.upquizz.response;

import java.util.Date;

public record ReportResponse(
        long id,
        String text,
        Date timestamp,
        GeneralQuizResponse quiz,
        UserResponse owner
) {
}
