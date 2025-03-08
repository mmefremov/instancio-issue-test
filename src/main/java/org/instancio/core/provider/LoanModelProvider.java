package org.instancio.core.provider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.core.model.Account;
import org.instancio.core.model.Due;
import org.instancio.core.model.LoanRequest;
import org.instancio.generators.Generators;

import java.util.List;
import java.util.ListIterator;

import static org.instancio.Select.field;
import static org.instancio.Select.root;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanModelProvider {

    public static Model<LoanRequest> createLoanRequestModelWithoutRootSelector(List<String> numbers) {
        ListIterator<String> iterator = numbers.listIterator();
        return Instancio.of(LoanRequest.class)
                .generate(field(LoanRequest::getAccounts), gen -> gen.collection().size(numbers.size()))
                .generate(field(Account::getOrder), Generators::intSeq)
                .supply(field(Account::getNumber), iterator::next)
                .setModel(field(LoanRequest::getSchedule), createDueListModel())
                .toModel();
    }

    public static Model<LoanRequest> createLoanRequestModelWithCollectionModels(List<String> numbers) {
        return Instancio.of(LoanRequest.class)
                .setModel(field(LoanRequest::getAccounts), createAccountListModelWithRootSelector(numbers))
                .setModel(field(LoanRequest::getSchedule), createDueListModel())
                .toModel();
    }

    private static Model<List<Account>> createAccountListModelWithRootSelector(List<String> numbers) {
        ListIterator<String> iterator = numbers.listIterator();
        return Instancio.ofList(Account.class)
                .generate(root(), gen -> gen.collection().size(numbers.size()))
                .generate(field(Account::getOrder), Generators::intSeq)
                .supply(field(Account::getNumber), iterator::next)
                .toModel();
    }

    private static Model<List<Due>> createDueListModel() {
        return Instancio.ofList(Due.class)
                .generate(field(Due::getAmount), gen -> gen.math().bigDecimal().scale(0))
                .setBlank(field(Due::getAmountLocal))
                .generate(field(Due::getDate), gen -> gen.temporal().localDate().future())
                .toModel();
    }

    public static Model<LoanRequest> createLoanRequestModelWithTwoRootSelectors(List<String> numbers) {
        return Instancio.of(LoanRequest.class)
                .setModel(field(LoanRequest::getAccounts), createAccountListModelWithRootSelector(numbers))
                .setModel(field(LoanRequest::getSchedule), createDueListModelWithRootSelector())
                .toModel();
    }

    private static Model<List<Due>> createDueListModelWithRootSelector() {
        return Instancio.ofList(Due.class)
                .generate(root(), gen -> gen.collection().minSize(2))
                .generate(field(Due::getAmount), gen -> gen.math().bigDecimal().scale(0))
                .setBlank(field(Due::getAmountLocal))
                .generate(field(Due::getDate), gen -> gen.temporal().localDate().future())
                .toModel();
    }
}
