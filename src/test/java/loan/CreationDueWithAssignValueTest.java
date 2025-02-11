package loan;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.core.model.Due;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.instancio.Select.field;

@Slf4j
class CreationDueWithAssignValueTest {

    private static Model<Due> dueModel;

    @BeforeAll
    static void setup() {
        dueModel = Instancio.of(Due.class)
                .generate(field(Due::getAmount), gen -> gen.math().bigDecimal())
                .assign(Assign.valueOf(Due::getAmount)
                                .to(field(Due::getAmountLocal)))
                .setBlank(field(Due::getDate))
                .toModel();
    }

    @Test
    @Order(1)
    void testCreationAccountWithAssignValueToOtherLocalObject() {
        Due due1 = Instancio.of(dueModel)
                .assign(Assign.valueOf(Due::getAmount)
                                .to(field(Due::getAmountLocal))
                                .as((BigDecimal amount) -> amount.subtract(BigDecimal.ONE)))
                .create();
        log.info("due1: {}", due1);
        Assertions.assertThat(due1.getAmountLocal()).as("due amountLocal1").isEqualTo(due1.getAmount().subtract(BigDecimal.ONE));

        Due due2 = Instancio.of(dueModel).create();
        log.info("due2: {}", due2);
        Assertions.assertThat(due2.getAmountLocal()).as("due amountLocal2").isEqualTo(due2.getAmount());
    }

    @Test
    @Order(2)
    void testCreationAccountWithAssignValueToObjectFromAnotherMethod() {
        Due due3 = Instancio.of(dueModel).create();
        log.info("due3: {}", due3);
        Assertions.assertThat(due3.getAmountLocal()).as("due amountLocal3").isEqualTo(due3.getAmount());
    }
}
