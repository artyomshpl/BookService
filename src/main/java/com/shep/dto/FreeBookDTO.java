package com.shep.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FreeBookDTO {
    private Long id;
    private Long bookId;
    private LocalDateTime borrowedTime;
    private LocalDateTime returnTime;
}
