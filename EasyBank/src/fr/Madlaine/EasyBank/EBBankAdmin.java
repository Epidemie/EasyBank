package fr.Madlaine.EasyBank;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

public class EBBankAdmin {
	
	private static Logger logger = Logger.getLogger("Minecraft");
	private static String logTag = Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "[" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString() + "Easy" + Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString() + "Bank" + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "] " + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString();
	private EasyBank plugin;
	private EBChat EBChat;
	private EBStorage storage;

	public EBBankAdmin(EBChat eBChat2, EBStorage storage2, EasyBank easyBank) {
		this.EBChat = eBChat2;
		this.storage = storage2;
		this.plugin = easyBank;
	}

	public void onAdminSet(String player, String admin, double amount) {
		try {
			double bankamnt = storage.getData(player);
			storage.addData(player, amount);
			EBChat.AdminPlayerSet(admin, player, amount);
		} catch (NullPointerException e) {
			storage.addNewData(player, amount);
			EBChat.AdminPlayerCreateBA(admin, player, amount);
		}
	}

	public void onAdminGive(String player, String admin, double amount) {
		try {
			double bankamnt = storage.getData(player);
			double newbankamnt = bankamnt + amount;
			storage.addData(player, newbankamnt);
			EBChat.AdminPlayerGive(admin, player, amount);
		} catch (NullPointerException e) {
			storage.addNewData(player, amount);
			EBChat.AdminPlayerCreateBA(admin, player, amount);
		}
	}

	public void onAdminTake(String player, String admin, double amount) {
		try {
			double bankamnt = storage.getData(player);
			if (amount > bankamnt) {
				EBChat.AdminPlayerTakeNotEnough(admin, player);
			} else {
				double newbankamnt = bankamnt - amount;
				storage.addData(player, newbankamnt);
				EBChat.AdminPlayerTake(admin, player, amount);
			}
		} catch (NullPointerException e) {
			EBChat.BankerAdminPlayerHasnotBA(admin, player);
		}
	}

}
