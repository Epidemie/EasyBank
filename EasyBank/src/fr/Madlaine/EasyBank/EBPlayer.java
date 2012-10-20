package fr.Madlaine.EasyBank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class EBPlayer {
	
	private Economy eco;
	private EBStorage storage;
	private EBChat EBChat;
	private EasyBank plugin;

	public EBPlayer(EBChat eBChat2, EBStorage storage2, EasyBank easyBank) {
		this.EBChat = eBChat2;
		this.storage = storage2;
		this.plugin = easyBank;
	}

	public boolean setupEconomy(Economy economy) {
		eco = economy;
		return true;
	}

	//Get the player Bank account amount
	public void onLook(String player) {
		try {
			double amount = storage.getData(player);
			EBChat.onPlayerLook(player, amount);
		} catch (NullPointerException e) {
			EBChat.PlayerHasNotBankAccount(player);
		}
	}

	//Debit amount on the player bank account
	public void onDebit(String player, double amount) {
		if (amount > 0) {
			try {
				double bankamnt = storage.getData(player);
				if (bankamnt < amount) {
					EBChat.PlayerDebitNotEnough(player);
				} else {
					double newbankamnt = bankamnt - amount;
					eco.depositPlayer(player, amount);
					storage.addData(player, newbankamnt);
					EBChat.PlayerDebit(player, amount, newbankamnt);
				}
			} catch (NullPointerException e) {
				EBChat.PlayerHasNotBankAccount(player);
			}
		} else {
			EBChat.invalidAmount(player);
		}
	}

	//Depo amount
	public void onDepo(String player, double amount) {
		double InitialHoldings = plugin.InitialHoldings;
		double CreateCost = plugin.CreateCost;
		if (amount > 0) {
			try {
				double bankamnt = storage.getData(player);
				if(!eco.has(player, amount)) {
					EBChat.PlayerDepoNotEnough(player);
				} else {
					double newbankamnt = bankamnt + amount;
					eco.withdrawPlayer(player, amount);
					storage.addData(player, newbankamnt);
					EBChat.PlayerDepo(player, amount, newbankamnt);
				}
			} catch (NullPointerException e) {
				Player p = Bukkit.getPlayer(player);
				if (p.hasPermission("EasyBank.createBA")) {
					if (CreateCost > 0) {
						amount = amount + CreateCost;
						EBChat.CreateCost(player, CreateCost);
						EBChat.PlayerCreateBA(player, amount);
					}
					if(!eco.has(player, amount)) {
						EBChat.PlayerDepoNotEnough(player);
					} else {
						eco.withdrawPlayer(player, amount);
						if(InitialHoldings > 0) {
							storage.addData(player, amount + InitialHoldings);
							EBChat.PlayerCreateBA(player, amount);
						} else {
							storage.addNewData(player, amount);
							EBChat.PlayerCreateBA(player, amount);
						}
						
					}
				} else {
					EBChat.notAllowed(player);
				}
			}
		} else {
			EBChat.invalidAmount(player);
		}
	}
}
