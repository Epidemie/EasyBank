package fr.Madlaine.EasyBank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class EBBanker{
	
	private Economy eco;
	private EBChat EBChat;
	private EBStorage storage;

	public EBBanker(EBChat eBChat2, EBStorage storage2) {
		this.EBChat = eBChat2;
		this.storage = storage2;
	}

	public boolean setupEconomy(Economy economy) {
		eco = economy;
		return true;
	}

	public void onBankerDepo(String player, String Banker, double amount) {
		try {
			double bankamnt = storage.getData(player);
			if(!eco.has(player, amount)) {
				EBChat.BankerDepoNotEnough(Banker ,player);
			} else {
				double newbankamnt = bankamnt + amount;
				eco.withdrawPlayer(player, amount);
				storage.addData(player, newbankamnt);
				EBChat.BankerDepo(Banker, player, amount, newbankamnt);
			}
		} catch (NullPointerException e) {
			Player p = Bukkit.getPlayer(player);
			if (p.hasPermission("EasyBank.createBA")) {
				if(!eco.has(player, amount)) {
					EBChat.BankerDepoNotEnough(Banker, player);
				} else {
					eco.withdrawPlayer(player, amount);
					storage.addNewData(player, amount);
					EBChat.BankerPlayerCreateBA(Banker, player, amount);
				}
			} else {
				EBChat.BankernotAllowed(Banker, player);
			}
		}
	}
	
	public void onBankerDebit(String player, String Banker, double amount) {
			try {
				double bankamnt = storage.getData(player);
				if(amount > bankamnt) {
					EBChat.BankerDebitNotEnough(Banker, player);
				} else {
					double newbankamnt = bankamnt - amount;
					eco.depositPlayer(player, amount);
					storage.addData(player, newbankamnt);
					EBChat.BankerDebit(Banker, player, amount, newbankamnt);
				}
			} catch (NullPointerException e) {
				EBChat.BankerAdminPlayerHasnotBA(Banker, player);
			}
	}

	public void onBankerLook(String Banker, String player) {
		try {
			double bankamnt = storage.getData(player);
			EBChat.BankerLook(Banker, player, bankamnt);
		} catch (NullPointerException e) {
			EBChat.BankerAdminPlayerHasnotBA(Banker, player);
		}
	}
}
