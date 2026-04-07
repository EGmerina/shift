package shift.sellersandtransactions.api.dto;

public record FileStatisticDto(int insertedLinesCount,
                               int updatedLinesCount,
                               int errorProcessedLinesCount) {
}
