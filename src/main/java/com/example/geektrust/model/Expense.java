package com.example.geektrust.model;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Expense {
    private String id;
    private int amount;
    private Map<String, Contribution> contributions;
    private Map<String, Split> splits;
    private List<ExpenseDue> dues;

    private Expense(ExpenseBuilder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.contributions = builder.contributions;
        this.splits = builder.splits;
        this.dues = builder.dues;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Map<String, Contribution> getContributions() {
        return contributions;
    }

    public List<ExpenseDue> getDues() {
        return dues;
    }

    public Map<String, Split> getSplits() {
        return splits;
    }

    public static class ExpenseBuilder {

        private final String id;
        private final int amount;
        private Map<String, Contribution> contributions;
        private Map<String, Split> splits;
        private List<ExpenseDue> dues;

        public ExpenseBuilder(int amount) {
            this.id = UUID.randomUUID().toString();
            this.amount = amount;
        }

        public ExpenseBuilder contributor(User contributor) {
            Contribution contribution = new Contribution(contributor, amount);
            this.contributions = Map.of(contributor.getUserName(), contribution);
            return this;
        }

        public ExpenseBuilder generateSplits(User[] users) {
            int splitAmount = Math.round(amount / users.length);
            Map<String, Split> splits = new HashMap<>();
            for(User user: users){
                Split split = new Split(user, splitAmount);
                splits.put(user.getUserName(), split);
            }
            this.splits = splits;
            return this;
        }

        public ExpenseBuilder generateDues() {
            List<Balance> positiveBalances = new ArrayList();
            List<Balance> negativeBalances = new ArrayList();
            for (Map.Entry<String, Split> entry : splits.entrySet()) {
                Contribution contribution = contributions.get(entry.getKey());
                if(contribution == null){
                    Balance balance = new Balance(entry.getKey(), entry.getValue().getAmount(), entry.getValue().getUser());
                    negativeBalances.add(balance);
                }
                else{
                    int amount = contribution.getAmount() - entry.getValue().getAmount();
                    if(amount > 0){
                        Balance balance = new Balance(entry.getKey(), amount, entry.getValue().getUser());
                        positiveBalances.add(balance);
                    }
                    else{
                        Balance balance = new Balance(entry.getKey(), Math.abs(amount), entry.getValue().getUser());
                        negativeBalances.add(balance);
                    }
                }
            }
            List<ExpenseDue> dues = calculateDues(positiveBalances, negativeBalances);
            this.dues = dues;
            System.out.println("SUCCESS");
            return this;
        }

        public Expense build() {
            return new Expense(this);
        }

        private List<ExpenseDue> calculateDues(List<Balance> positiveBalances, List<Balance> negativeBalances){
            List<ExpenseDue> dues = new ArrayList();
            int p = 0;
            int n = 0;
            while(p < positiveBalances.size() && n < negativeBalances.size()){
                Balance positiveUser = positiveBalances.get(p);
                int positiveAmount = positiveUser.getAmount();
                Balance negativeUser = negativeBalances.get(n);
                int negativeAmount = negativeUser.getAmount();
                if(positiveAmount == 0){
                    p++;
                }
                else if(negativeAmount == 0){
                    n++;
                }
                else{
                    int balance = positiveAmount - negativeAmount;
                    if(balance >= 0){
                        ExpenseDue expenseDue = new ExpenseDue(negativeUser.getUser(), positiveUser.getUser(), negativeAmount);
                        positiveUser.setAmount(balance);
                        n++;
                        dues.add(expenseDue);
                    }
                    else if(balance < 0){
                        ExpenseDue expenseDue = new ExpenseDue(negativeUser.getUser(), positiveUser.getUser(), positiveAmount);
                        negativeUser.setAmount(Math.abs(balance));
                        p++;
                        dues.add(expenseDue);
                    }
                }
            }
            return dues;
        }

    }
}
