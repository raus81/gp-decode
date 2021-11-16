package model;

import lombok.Data;

@Data
public class CheckResult {
    private boolean valid = false;
    private String message = "";
}
