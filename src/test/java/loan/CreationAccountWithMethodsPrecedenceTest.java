package loan;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.core.model.Account;
import org.junit.jupiter.api.Test;

import static org.instancio.Select.field;

@Slf4j
class CreationAccountWithMethodsPrecedenceTest {

    @Test
    void testCreationAccountWithMethodsPrecedenceChange() {
        Model<Account> accountModel = Instancio.of(Account.class)
                .set(field(Account::getNumber), "001")
                .set(field(Account::getOrder), 1)
                .toModel();
        Account account = Instancio.of(accountModel)
                .supply(field(Account::getNumber), random -> random.digits(10))
                .assign(Assign.valueOf(Account::getNumber).generate(
                        gen -> gen.string().digits().length(3)))
                .generate(field(Account::getOrder), gen -> gen.ints().min(2))
                .create();
        log.info("request with single account: {}", account);
        Assertions.assertThat(account.getNumber()).as("account number").isNotEqualTo("001")
                .hasSize(3);
        Assertions.assertThat(account.getOrder()).as("account order").isOne();
    }
}
