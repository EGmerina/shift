package shift.sellersandtransactions.api.dto;

import java.util.List;

public record DetailedFileStatisticDto(int insertedLinesCount,
                                    int updatedLinesCount,
                                    List<ProcessingErrorDto> errors) {
}
