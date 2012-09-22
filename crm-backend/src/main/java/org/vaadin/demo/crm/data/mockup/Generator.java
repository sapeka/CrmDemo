package org.vaadin.demo.crm.data.mockup;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.vaadin.demo.crm.Backend;
import org.vaadin.demo.crm.data.Account;
import org.vaadin.demo.crm.data.AccountManager;
import org.vaadin.demo.crm.data.Opportunity;

public class Generator {

	public static void ensureAvailabilityOfMockupData() {
		EntityManager em = Persistence.createEntityManagerFactory(
				Backend.PERSISTENCE_UNIT).createEntityManager();

		long size = (Long) em.createQuery("SELECT COUNT(p) FROM Account p")
				.getSingleResult();
		if (size == 0) {

			em.getTransaction().begin();

			ArrayList<AccountManager> accountManagers = new ArrayList<AccountManager>();
			generateAccountManagers(em, accountManagers);
			ArrayList<Account> accounts = new ArrayList<Account>();
			generateAccounts(em, accounts, accountManagers);
			ArrayList<Opportunity> opportunities = new ArrayList<Opportunity>();
			generateOpportunities(em, opportunities, accounts);

			em.getTransaction().commit();
		}
	}

	private static void generateAccounts(EntityManager em,
			ArrayList<Account> accounts,
			ArrayList<AccountManager> accountManagers) {
		for (AccountManager am : accountManagers) {
			int numberOfAccounts = (int) (Math.random() * 30);
			for (int i = 0; i < numberOfAccounts; i++) {
				Account a = new Account();
				a.setAccountManager(am);
				a.setName(randomCompanyName());
				a.setSales((int) (Math.random() * 10000000));
				accounts.add(a);
				em.persist(a);
			}
		}
	}

	private static void generateOpportunities(EntityManager em,
			ArrayList<Opportunity> opportunities, ArrayList<Account> accounts) {
		for (Account a : accounts) {
			int numberOfOpportunities = (int) (Math.random() * 6);
			for (int i = 0; i < numberOfOpportunities; i++) {
				Opportunity o = new Opportunity();
				o.setAccount(a);
				o.setName(randomText((int)(Math.random()*15)+4));
				opportunities.add(o);
				em.persist(o);
			}
		}
	}

	private static void generateAccountManagers(EntityManager em,
			ArrayList<AccountManager> accountManagers) {
		for (int i = 0; i < 7; i++) {
			AccountManager am = new AccountManager();
			accountManagers.add(am);
			am.setFirstName(randomFirstName());
			am.setLastName(randomLastName());
			em.persist(am);
		}
	}

	private static String randomFirstName() {
		String[] names = { "Dave", "Mike", "Katherine", "Jonas", "Linus",
				"Bob", "Anne", "Minna", "Elisa", "George", "Mathias", "Pekka",
				"Fredrik", "Kate", "Teppo", "Kim", "Samatha", "Sam", "Linda",
				"Jo", "Sarah", "Ray", "Michael", "Steve" };
		return names[(int) (Math.random() * names.length)];
	}

	private static String randomLastName() {
		String[] names = { "Smith", "Lehtinen", "Chandler", "Hewlett",
				"Packard", "Jobs", "Buffet", "Reagan", "Carthy", "Wu",
				"Johnson", "Williams", "Jones", "Brown", "Davis", "Moore",
				"Wilson", "Taylor", "Anderson", "Jackson", "White", "Harris",
				"Martin", "King", "Lee", "Walker", "Wright", "Clark",
				"Robinson", "Garcia", "Thomas", "Hall", "Lopez", "Scott",
				"Adams", "Barker", "Morris", "Cook", "Rogers", "Rivera",
				"Gray", "Price", "Perry", "Powell", "Russell", "Diaz" };
		return names[(int) (Math.random() * names.length)];
	}

	private static String randomCompanyName() {

		String name = randomName();
		if (Math.random() < 0.03)
			name += " Technologies";
		else if (Math.random() < 0.02)
			name += " Investment";
		if (Math.random() < 0.3)
			name += " Inc";
		else if (Math.random() < 0.2)
			name += " Ltd.";

		return name;
	}

	private static String randomWord(int len, boolean capitalized) {
		String[] part = { "ger", "ma", "isa", "app", "le", "ni", "ke", "mic",
				"ro", "soft", "wa", "re", "lo", "gi", "is", "acc", "el", "tes",
				"la", "ko", "ni", "ka", "so", "ny", "mi", "nol", "ta", "pa",
				"na", "so", "nic", "sa", "les", "for", "ce" };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			String p = part[(int) (Math.random() * part.length)];
			if (i == 0 && capitalized)
				p = Character.toUpperCase(p.charAt(0)) + p.substring(1);
			sb.append(p);
		}
		return sb.toString();

	}

	private static String randomText(int words) {
		StringBuffer sb = new StringBuffer();
		int sentenceWordsLeft = 0;
		while (words-- > 0) {
			if (sb.length() > 0)
				sb.append(' ');
			if (sentenceWordsLeft == 0 && words > 0) {
				sentenceWordsLeft = (int) (Math.random() * 15);
				sb.append(randomWord(1 + (int) (Math.random() * 3), true));
			} else {
				sentenceWordsLeft--;
				sb.append(randomWord(1 + (int) (Math.random() * 3), false));
				if (words > 0 && sentenceWordsLeft > 3 && Math.random() < 0.15)
					sb.append(',');
				else if (sentenceWordsLeft == 0 || words == 0)
					sb.append('.');
			}
		}
		return sb.toString();
	}

	private static String randomName() {
		int len = (int) (Math.random() * 4) + 1;
		return randomWord(len, true);
	}

}
