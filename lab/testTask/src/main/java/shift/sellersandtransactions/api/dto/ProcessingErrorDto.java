package shift.sellersandtransactions.api.dto;

import shift.sellersandtransactions.api.ProcessingErrorCode;

public record ProcessingErrorDto(int lineNumber,
                                 ProcessingErrorCode errorCode,
                                 String errorMessage) {
}
