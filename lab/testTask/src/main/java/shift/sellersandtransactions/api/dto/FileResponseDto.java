package shift.sellersandtransactions.api.dto;

import shift.sellersandtransactions.api.FileStatus;

public record FileResponseDto(String fileId,
                              FileStatus status,
                              FileStatisticDto statistic) {
}
