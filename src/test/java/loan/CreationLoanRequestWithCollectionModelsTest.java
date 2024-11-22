package loan;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.instancio.core.model.LoanRequest;
import org.instancio.core.provider.LoanModelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class CreationLoanRequestWithCollectionModelsTest {

    private static final List<String> singleAccountList = List.of("123");

    private static final List<String> groupAccountList = List.of("123", "456", "789");

    private static Model<LoanRequest> modelWithSingleAccount;

    private static Model<LoanRequest> modelWithGroupAccounts;

    @BeforeAll
    static void setup() {
        modelWithSingleAccount = LoanModelProvider.createLoanRequestModelWithCollectionModels(singleAccountList);
        modelWithGroupAccounts = LoanModelProvider.createLoanRequestModelWithCollectionModels(groupAccountList);
    }

    @Test
    void testCreationLoanRequestWithSingleAccount() {
        LoanRequest loanRequestWithoutRootSelector = Instancio.of(modelWithSingleAccount).create();
        log.info("request with single account: {}", loanRequestWithoutRootSelector);
        Assertions.assertThat(loanRequestWithoutRootSelector.getAccounts())
                .as("account list size should be %d", singleAccountList.size())
                .hasSameSizeAs(singleAccountList);

        Assertions.assertThat(loanRequestWithoutRootSelector.getSchedule())
                .as("schedule list size should be from 2 to 6")
                .hasSizeGreaterThanOrEqualTo(2)
                .hasSizeLessThanOrEqualTo(6);
    }

    @Test
    void testCreationLoanRequestWithGroupAccounts() {
        LoanRequest loanRequestWithoutRootSelector = Instancio.of(modelWithGroupAccounts)
                .generate(Select.field(LoanRequest::getSchedule), gen -> gen.collection().size(1))
                .create();
        log.info("request with group account: {}", loanRequestWithoutRootSelector);
        Assertions.assertThat(loanRequestWithoutRootSelector.getAccounts())
                .as("account list size should be %d", groupAccountList.size())
                .hasSameSizeAs(groupAccountList);

        Assertions.assertThat(loanRequestWithoutRootSelector.getSchedule())
                .as("schedule list size should be 1")
                .hasSize(1);
    }
}
