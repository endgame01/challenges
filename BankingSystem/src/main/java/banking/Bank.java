package banking;

import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * Private Variables:<br>
 * {@link #accounts}: List&lt;Long, Account&gt;
 */
public class Bank implements BankInterface {
	private LinkedHashMap<Long, Account> accounts;

	public Bank() {
		this.accounts = new LinkedHashMap<>();
	}

	private Account getAccount(Long accountNumber) {
		if(!this.accounts.containsKey(accountNumber)){
			throw new RuntimeException("Not valid account");
		}
		return accounts.get(accountNumber);
	}

	public Long openCommercialAccount(Company company, int pin, double startingDeposit) {
		Long nextAccount = this.accounts.keySet().stream().max(Comparator.naturalOrder()).orElse(0L) + 1;
		accounts.put(nextAccount, new CommercialAccount(company, nextAccount, pin, startingDeposit));
		return nextAccount;
	}

	public Long openConsumerAccount(Person person, int pin, double startingDeposit) {
		Long nextAccount = this.accounts.keySet().stream().max(Comparator.naturalOrder()).orElse(0L) + 1;
		accounts.put(nextAccount, new ConsumerAccount(person, nextAccount, pin, startingDeposit));
		return nextAccount;
	}

	public boolean authenticateUser(Long accountNumber, int pin) {
		return getAccount(accountNumber).validatePin(pin);
	}

	public double getBalance(Long accountNumber) {
		return getAccount(accountNumber).getBalance();
	}

	public void credit(Long accountNumber, double amount) {
		getAccount(accountNumber).creditAccount(amount);
	}

	public boolean debit(Long accountNumber, double amount) {
		return getAccount(accountNumber).debitAccount(amount);
	}
}
