package com.vishaln.domain;

import com.vishaln.util.Constant;
import lombok.*;

import java.time.Duration;
import java.time.Instant;

/**
 * Transfer object representing Transaction.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {

    private String amount;
    private String timestamp;

    /**
     * Determines if the current transaction has expired based on the time configured in Constants.
     * TODO Need to take the time externally from properties file.
     *
     * @return
     */
    public boolean isExpired() {

        return (Instant.parse(timestamp).isBefore(Instant.now().minus(Duration.ofMinutes(Constant.Transaction.SCHEDULE_TIME)))) ? true : false;
    }
}
