package loan;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.core.model.Due;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.instancio.Select.field;

@Slf4j
class CreationDueWithAssignValueWithFunctionTest {

    private static Model<Due> dueModel;

    @BeforeAll
    static void setup() {
        dueModel = Instancio.of(Due.class)
                .generate(field(Due::getAmount), gen -> gen.math().bigDecimal())
                .assign(Assign.valueOf(Due::getAmount)
                                .to(field(Due::getAmountLocal))
                                .as((BigDecimal amount) -> amount.add(BigDecimal.TEN)))
                .setBlank(field(Due::getDate))
                .toModel();
    }

    @Test
    void testCreationDueWithAssignValueToOtherLocalObject() {
        Due due1 = Instancio.of(dueModel)
                .assign(Assign.valueOf(Due::getAmount)
                                .to(field(Due::getAmountLocal))
                                .as((BigDecimal amount) -> amount.subtract(BigDecimal.ONE)))
                .create();
        log.info("due1: {}", due1);
        BigDecimal expectedAmountLocal1 = due1.getAmount().subtract(BigDecimal.ONE);
        Assertions.assertThat(due1.getAmountLocal()).as("due amountLocal1").isEqualTo(expectedAmountLocal1);

        Due due2 = Instancio.of(dueModel).create();
        log.info("due2: {}", due2);
        BigDecimal expectedAmountLocal2 = due2.getAmount().add(BigDecimal.TEN);
        Assertions.assertThat(due2.getAmountLocal()).as("due amountLocal2").isEqualTo(expectedAmountLocal2);
    }
}
