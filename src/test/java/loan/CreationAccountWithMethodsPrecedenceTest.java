package loan;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.instancio.core.model.Account;
import org.junit.jupiter.api.Test;

@Slf4j
class CreationAccountWithMethodsPrecedenceTest {

    @Test
    void testCreationAccountWithMethodsPrecedenceChange() {
        Model<Account> accountModel = Instancio.of(Account.class)
                .set(Select.field(Account::getNumber), "001")
                .set(Select.field(Account::getOrder), 1)
                .toModel();
        Account account = Instancio.of(accountModel)
                .assign(Assign.valueOf(Account::getNumber).generate(
                        gen -> gen.string().digits().length(3)))
                .generate(Select.field(Account::getOrder), gen -> gen.ints().min(2))
                .create();
        log.info("request with single account: {}", account);
        Assertions.assertThat(account.getNumber()).as("account number").isNotEqualTo("001");
        Assertions.assertThat(account.getOrder()).as("account order").isOne();
    }
}
