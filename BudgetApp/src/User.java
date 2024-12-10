import java.util.ArrayList;

public class User {
	User(String fn, String ln){
		Panel1.userList.add(this);
		this.firstName = fn;
		this.lastName = ln;
	}
	
	double incomePerWeek = 0;
	double outcomePerWeek = 0;
	String firstName;
	String lastName;
	
	ArrayList<Amount> outcome = new ArrayList<>();
	ArrayList<Amount> income = new ArrayList<>();


	public void calcIncome(){
		double tempAmount = 0;
		for(Amount incomeAmount : income){
			tempAmount += incomeAmount.amount;
			System.out.println(incomeAmount.name + ": " + incomeAmount.amount);
		}
		incomePerWeek = tempAmount;
	}

	public void calcOutcome(){
		double tempAmount = 0;
		for(Amount outcomeAmount : outcome){
			tempAmount += outcomeAmount.amount;
			System.out.println(outcomeAmount.name + ": " + outcomeAmount.amount);
		}
		outcomePerWeek = tempAmount;
		
		double userShareInHouse =  Panel1.householdOutcomePerWeek/ Panel1.userList.size();
		System.out.println(userShareInHouse);
		outcomePerWeek += userShareInHouse;
	}
	public void newIncome(String name, int amount, String discip) {
		income.add(new Amount(name, amount, discip));
		calcIncome();
		System.out.println("Income Total: " + incomePerWeek);
	}
	
	public void removeIncome(Amount amount) {
		income.remove(amount);
		calcIncome();
	}
	
	public void clearIncome() {
		income.clear();
		calcIncome();
	}
	
	public void viewAllIncome() {
		for(Amount income: income) {
			System.out.println(income.name + ": " +income.amount);
		}
	}
	
	public void addOutcome(String name, double amount, String discip) {
		outcome.add(new Amount(name, amount, discip));
		calcOutcome();
		System.out.println("Outcome Total: " + outcomePerWeek);
	}
	
	public void removeOutcome(Amount amount){
		outcome.remove(amount);
		calcOutcome();
	}
	public void viewOutcome() {
		double totalOutcome= 0;
		for(Amount expenses: outcome) {
			System.out.println(expenses.name + ": " +expenses.amount);
			totalOutcome += expenses.amount;
		}
		System.out.println("Total: " + totalOutcome);
	}

	public void clearOutcome(){
		outcome.clear();
		calcOutcome();
	}
	
	public void changeName(String fn, String ln) {
		this.firstName = fn;
		this.lastName = ln;
	}
	
}
