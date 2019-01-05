package com.vishaln.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@EqualsAndHashCode
@Builder
public class Statistic {

    @JsonSerialize(using = QuotesSerializer.class)
    private volatile BigDecimal sum = BigDecimal.ZERO;

    @JsonSerialize(using = QuotesSerializer.class)
    private volatile BigDecimal avg = BigDecimal.ZERO;

    @JsonSerialize(using = QuotesSerializer.class)
    private volatile BigDecimal max = BigDecimal.ZERO;

    @JsonSerialize(using = QuotesSerializer.class)
    private volatile BigDecimal min = BigDecimal.ZERO;

    private volatile long count;

    public Statistic compute(Queue<Transaction> transactions) {

        if (transactions.isEmpty()) return this;

        setCount(transactions.size());
        final List<BigDecimal> amountList = transactions
                .stream()
                .map(Transaction::getAmount)
                .map(BigDecimal::new)
                .collect(Collectors.toList());

        setSum(amountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        setAvg(getSum().divide(new BigDecimal(getCount()), RoundingMode.HALF_UP));
        setMax(amountList.stream().max(Comparator.naturalOrder()).get());
        setMin(amountList.stream().min(Comparator.naturalOrder()).get());

        return this;
    }

    public Statistic update(Transaction transaction) {

        final BigDecimal amt = new BigDecimal(transaction.getAmount());

        incrementCount();
        updateMax(amt);
        updateMin(amt);
        updateSum(amt);
        updateAvg(amt);

        return this;
    }

    public void reset() {
        sum = BigDecimal.ZERO;
        avg = BigDecimal.ZERO;
        max = BigDecimal.ZERO;
        min = BigDecimal.ZERO;
        count = 0;
    }

    private void incrementCount() {
        count++;
    }

    private void updateMax(final BigDecimal amount) {
        max = max.max(amount);
    }

    private void updateMin(final BigDecimal amount) {

        if (count == 1)
            min = amount;
        else
            min = amount.min(min);
    }

    private void updateAvg(final BigDecimal amount) {

        final BigDecimal divisor = BigDecimal.valueOf(count);
        avg = sum.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private void updateSum(BigDecimal amount) {
        sum = sum.add(amount);
    }
}

class QuotesSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }
}
