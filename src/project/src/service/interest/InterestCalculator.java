package src.service.interest;

public class InterestCalculator {
    private final double[] monthlyRates = {0.25, 0.23, 0.15, 0.32, 0.31, 0.23, 0.19, 0.45, 0.51, 0.24, 0.25, 0.23};

    public long calculateCompoundInterest(long principal, int years) {
        long amount = principal;
        int totalMonths = years * 12;
        double monthlyRate;

        for (int i = 0; i < totalMonths; i++) {
            monthlyRate = monthlyRates[i % 12];
            amount += (long) (amount * (monthlyRate / 100));
        }

        return amount;
    }
}
