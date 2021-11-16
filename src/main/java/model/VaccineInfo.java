package model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineInfo {
    private String vaccineEndDayComplete;
    private String vaccineStartDayComplete;
    private String vaccineEndDayNotComplete;
    private String vaccineStartDayNotComplete;


}
