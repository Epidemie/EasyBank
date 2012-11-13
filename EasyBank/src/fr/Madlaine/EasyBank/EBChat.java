package fr.Madlaine.EasyBank;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import net.milkbowl.vault.economy.Economy;

public class EBChat {

	private Economy eco;
	private EasyBank plugin;
    private static Logger logger = Logger.getLogger("Minecraft");
	String prefix = ChatColor.GRAY + "[" + ChatColor.WHITE + "Easy" + ChatColor.GOLD + "Bank" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    private static String logTag = Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "[" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString() + "Easy" + Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString() + "Bank" + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "] " + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString();
	private Boolean LogtoConsol;
	public double InitialHoldings;
	public double CreateCost;
    
	public EBChat(EasyBank plugin) {
		this.plugin = plugin;
		this.LogtoConsol = plugin.LogtoConsol;
	}

	public boolean setupEconomy(Economy economy) {
		eco = economy;
		return true;
	}
	
	public void localSet() {
		FileConfiguration local = plugin.local;
		File localFile = plugin.localFile;
    	
    	try {
    		local.options().header("Local file regroup all EasyBank Player Message");
        	local.set("ConnectMessage", "Welcome &a<Player>&f ! You've &a<Amount>&f on your bank account.");
        	local.set("NotAllowed", "&cYou're not allowed to do that!");
        	local.set("BankAcntAmnt", "You've now &a<BankAmount>&f on your Bank account.");
        	local.set("InvalidAmnt", "&cPlease enter a valid amount!");
        	local.set("BankerAdminPlayerNoBA", "&a<Player>&f hasn't bank account");
        	
        	local.set("Bank.HelpMenu1", "&aEasyBank's commands:");
        	local.set("Bank.HelpMenu2", "&a<Command>&f - deposits money on your bank account");
        	local.set("Bank.HelpMenu3", "&a<Command>&f - debits money from your bank account");
        	local.set("Bank.HelpMenu4", "&a<Command>&f - look into your bank account");
        	local.set("Bank.HelpMenu6", "&a<Command>&f - Pay another player");
        	local.set("Bank.HelpMenu5", "&a<Command>&f - Display help menu");
        	local.set("Bank.look", "You've &a<BankAmount>&f on your Bank account.");
        	local.set("Bank.Depo", "You've successfully deposited &a<Depo>&f on your bank account.");
        	local.set("Bank.Debit", "You've successfully debited &a<Debit>&f from your bank account.");
        	local.set("Bank.DepoNotEnough", "&cYou don't have enough money to do that!");
        	local.set("Bank.DebitNotEnough", "&cYou don't have enough money on your bank account!");
        	local.set("Bank.CreateCost", "You've pay &a<Cost>&f of cost.");
        	local.set("Bank.NoBankAccount", "You don't have a bank account, you've to deposit any amount to create one.");
        	local.set("Bank.CreatedBA", "You've successfully created and deposited &a<Depo>&f on your bank account.");
        	local.set("Bank.Pay", "You've successfully deposited &a<Pay>&f from your bank account to the &a<Player>&f bank account.");
        	local.set("Bank.Payed", "&a<Player>&f has deposited &a<Pay>&f on your bank account.");
        	local.set("Bank.paynoBa", "You, or &a<Player>&f don't have Bank account !");
        	local.set("Bank.payHiself", "&4You can't pay yourself !");
        	
        	local.set("Banker.HelpMenu1", "&aBanker's commands:");
        	local.set("Banker.HelpMenu2", "&a<Command>&f - deposits money on the player bank account");
        	local.set("Banker.HelpMenu3", "&a<Command>&f - debits money from the player bank account");
        	local.set("Banker.HelpMenu4", "&a<Command>&f - look into a bank account");
        	local.set("Banker.Look", "The player &a<Player> &fhas &a<BankAmount>&f in his bank account.");
        	local.set("Banker.MustBeOnline", "The Player &a<Player> &fmust be online.");
        	local.set("Banker.Debited1", "The Banker &a<Banker>&f has debited &a<Debit>&f from your bank account.");
        	local.set("Banker.Debited2", "Successfully debited &a<Debit>&f from the &a<Player>&f Bank account.");
        	local.set("Banker.DebitNotEnough", "&a<Player>&f hasn't enough money on his Bank account.");
        	local.set("Banker.DepoNotEnough", "&a<Player>&f doesn't have enough money");
        	local.set("Banker.Deposited1", "The Banker &a<Banker>&f has deposited &a<Depo>&f of your money on your Bank account.");
        	local.set("Banker.Deposited2", "Successfully deposited &a<Depo>&f on the &a<Player>&f Bank account.");
        	local.set("Banker.CreatedBA", "You've successfully created and deposited &a<Depo>&f on the &a<Player>&f bank account.");
        	local.set("Banker.PlayerNotAllowed", "&c<Player> is not allowed to do that");
        	
        	local.set("Sign.SuccessCreated", "Successfuly created sign.");
        	local.set("Sign.IncorrectSyntax", "&cIncorrect syntax!");
        	local.set("Sign.Deleted", "Successfully deleted sign!");
        	
        	local.set("Admin.HelpMenu1", "&aAdmin's commands:");
        	local.set("Admin.HelpMenu2", "&a<Command>&f - Set the player bank amount");
        	local.set("Admin.HelpMenu3", "&a<Command>&f - Give amount on the Player Bank account");
        	local.set("Admin.HelpMenu4", "&a<Command>&f - Take amount on the Player Bank account");
        	local.set("Admin.Set", "Successfully set the &a<Player>&f account to &a<Amount>&f.");
        	local.set("Admin.Give", "Successfully give &a<Amount>&f to the &a<Player>&f bank account.");
        	local.set("Admin.Take", "Successfully grant &a<Amount>&f to the &a<Player>&f bank account.");
        	local.set("Admin.TakeNotEnough", "&a<Player>&f hasn't enough money on his Bank account.");
        	local.set("Admin.CreatedBA",  "You've successfully created and give &a<Amount>&f to the &a<Player>&f bank account.");
			local.save(localFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String replaceMsg(String msg){
		try {
			msg = msg.replaceAll("&0", ChatColor.BLACK.toString());
			msg = msg.replaceAll("&1", ChatColor.DARK_BLUE.toString());
			msg = msg.replaceAll("&2", ChatColor.DARK_GREEN.toString());
			msg = msg.replaceAll("&3", ChatColor.DARK_AQUA.toString());
			msg = msg.replaceAll("&4", ChatColor.DARK_RED.toString());
			msg = msg.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
			msg = msg.replaceAll("&6", ChatColor.GOLD.toString());
			msg = msg.replaceAll("&7", ChatColor.GRAY.toString());
			msg = msg.replaceAll("&8", ChatColor.DARK_GRAY.toString());
			msg = msg.replaceAll("&9", ChatColor.BLUE.toString());
			msg = msg.replaceAll("&a", ChatColor.GREEN.toString());
			msg = msg.replaceAll("&b", ChatColor.AQUA.toString());
			msg = msg.replaceAll("&c", ChatColor.RED.toString());
			msg = msg.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
			msg = msg.replaceAll("&e", ChatColor.YELLOW.toString());
			msg = msg.replaceAll("&f", ChatColor.WHITE.toString());
			msg = msg.replaceAll("&A", ChatColor.GREEN.toString());
			msg = msg.replaceAll("&B", ChatColor.AQUA.toString());
			msg = msg.replaceAll("&C", ChatColor.RED.toString());
			msg = msg.replaceAll("&D", ChatColor.LIGHT_PURPLE.toString());
			msg = msg.replaceAll("&E", ChatColor.YELLOW.toString());
			msg = msg.replaceAll("&F", ChatColor.WHITE.toString());
			msg = msg.replaceAll("&black", ChatColor.BLACK.toString());
			msg = msg.replaceAll("&blue", ChatColor.BLUE.toString());
			msg = msg.replaceAll("&darkblue", ChatColor.DARK_BLUE.toString());
			msg = msg.replaceAll("&darkaqua", ChatColor.DARK_AQUA.toString());
			msg = msg.replaceAll("&darkred", ChatColor.DARK_RED.toString());
			msg = msg.replaceAll("&darkpurple", ChatColor.DARK_PURPLE.toString());
			msg = msg.replaceAll("&gray", ChatColor.GRAY.toString());
			msg = msg.replaceAll("&darkgray", ChatColor.DARK_GRAY.toString());
			msg = msg.replaceAll("&grey", ChatColor.GRAY.toString());
			msg = msg.replaceAll("&darkgrey", ChatColor.DARK_GRAY.toString());
			msg = msg.replaceAll("&lightpurple", ChatColor.LIGHT_PURPLE.toString());
			msg = msg.replaceAll("&white", ChatColor.WHITE.toString());
			msg = msg.replaceAll("&red", ChatColor.RED.toString());
			msg = msg.replaceAll("&yellow", ChatColor.YELLOW.toString());
			msg = msg.replaceAll("&green", ChatColor.GREEN.toString());
			msg = msg.replaceAll("&aqua", ChatColor.AQUA.toString());
			msg = msg.replaceAll("&pink", ChatColor.LIGHT_PURPLE.toString());
			msg = msg.replaceAll("&purple", ChatColor.DARK_PURPLE.toString());
			msg = msg.replaceAll("&gold", ChatColor.GOLD.toString());
			msg = prefix + msg;
			return msg;
		} catch (NullPointerException e) {
			FileConfiguration local = plugin.local;
			File localFile = plugin.localFile;
			logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "---------------------------------------------------" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
			logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "Unfoundable message." + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
			logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "The plugin will send an Error message and created a New local.yml." + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
			logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "After that, the plugin should be work." + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
			logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "---------------------------------------------------" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
			e.printStackTrace();
			localSet();
			try {
				local.save(localFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	//Display EasyBank HelpMenu
	public void helpMenu(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.HelpMenu1"));
		String msg2 = replaceMsg(local.getString("Bank.HelpMenu2"));
		String msg3 = replaceMsg(local.getString("Bank.HelpMenu3"));
		String msg4 = replaceMsg(local.getString("Bank.HelpMenu4"));
		String msg5 = replaceMsg(local.getString("Bank.HelpMenu5"));
		String msg6 = replaceMsg(local.getString("Bank.HelpMenu6"));
		
		Player p = Bukkit.getPlayer(player);
		
		msg2 = msg2.replaceAll("<Command>", "/bank depo <amount>");
		msg3 = msg3.replaceAll("<Command>", "/bank debit <amount>");
		msg4 = msg4.replaceAll("<Command>", "/bank");
		msg5 = msg5.replaceAll("<Command>", "/bank <?/help>");
		msg6 = msg6.replaceAll("<Command>",	"/bank pay <amount> <Player>");
		
		p.sendMessage(msg1);
		p.sendMessage(msg2);
		p.sendMessage(msg3);
		p.sendMessage(msg6);
		p.sendMessage(msg4);
		p.sendMessage(msg5);
	}

	//Display the not Allowed Message
	public void notAllowed(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("NotAllowed"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display that the amount is invalid one.
	public void invalidAmount(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("InvalidAmnt"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display that the player have to be online
	public void mustBeOnline(String Banker, String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.MustBeOnline"));
		Player p = Bukkit.getPlayer(Banker);
		
		msg1.replaceAll("<Player>", player);
		
		p.sendMessage(msg1);
	}

	//Display the Banker HelpMenu
	public void BankerHelpMenu(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.HelpMenu1"));
		String msg2 = replaceMsg(local.getString("Banker.HelpMenu2"));
		String msg3 = replaceMsg(local.getString("Banker.HelpMenu3"));
		String msg4 = replaceMsg(local.getString("Banker.HelpMenu4"));
		Player p = Bukkit.getPlayer(player);
		
		msg2 = msg2.replaceAll("<Command>", "/banker depo <amount> <Player>");
		msg3 = msg3.replaceAll("<Command>", "/banker debit <amount> <Player>");
		msg4 = msg4.replaceAll("<Command>", "/banker look <Player>");
		
		p.sendMessage(msg1);
		p.sendMessage(msg2);
		p.sendMessage(msg3);
		p.sendMessage(msg4);
	}

	//Display Admin HelpMenu
	public void AdminHelpMenu(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.HelpMenu1"));
		String msg2 = replaceMsg(local.getString("Admin.HelpMenu2"));
		String msg3 = replaceMsg(local.getString("Admin.HelpMenu3"));
		String msg4 = replaceMsg(local.getString("Admin.HelpMenu4"));
		Player p = Bukkit.getPlayer(player);
		
		msg2 = msg2.replaceAll("<Command>", "/bankadmin set <amount> <Player>");
		msg3 = msg3.replaceAll("<Command>", "/bankadmin give <amount> <Player>");
		msg4 = msg4.replaceAll("<Command>", "/bankadmin take <amount> <Player>");
		
		p.sendMessage(msg1);
		p.sendMessage(msg2);
		p.sendMessage(msg3);
		p.sendMessage(msg4);
	}

	//Display the Player bank account amount
	public void onPlayerLook(String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg = replaceMsg(local.getString("Bank.look"));
		msg = msg.replaceAll("<BankAmount>", eco.format(amount));
	    Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg);
	}

	//Display that the player hasn't bank account
	public void PlayerHasNotBankAccount(String player) {
		FileConfiguration local = plugin.local;
		String msg = replaceMsg(local.getString("Bank.NoBankAccount"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg);
	}

	//Display that the player hasn't enough money
	public void PlayerDebitNotEnough(String player) {
		FileConfiguration local = plugin.local;
		String msg = replaceMsg(local.getString("Bank.DebitNotEnough"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg);
	}

	//Display that the player successfully debit amount from his bank account
	public void PlayerDebit(String player, double amount, double newbankamnt) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.Debit"));
		String msg2 = replaceMsg(local.getString("BankAcntAmnt"));
		msg1 = msg1.replaceAll("<Debit>", eco.format(amount));
		msg2 = msg2.replaceAll("<BankAmount>", eco.format(newbankamnt));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
		p.sendMessage(msg2);
		
		if (LogtoConsol) {
			logger.info(logTag + player + " has debited " + eco.format(amount) + " from his bank account");
		}
	}

	//Display that the player hasn't enough money
	public void PlayerDepoNotEnough(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.DepoNotEnough"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display that the player successfully deposit amount from his bank account
	public void PlayerDepo(String player, double amount, double newbankamnt) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.Depo"));
		String msg2 = replaceMsg(local.getString("BankAcntAmnt"));
		msg1 = msg1.replaceAll("<Depo>", eco.format(amount));
		msg2 = msg2.replaceAll("<BankAmount>", eco.format(newbankamnt));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
		p.sendMessage(msg2);
		
		if (LogtoConsol) {
			logger.info(logTag + player + " has deposited " + eco.format(amount) + " on his bank account");
		}
	}

	//Display that the player has successfully created Bank Account
	public void PlayerCreateBA(String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.CreatedBA"));
		msg1 = msg1.replaceAll("<Depo>", eco.format(amount));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + player + " has created and deposited " + eco.format(amount) + " on his bank account");
		}
	}

	//Display that the player hasn't enough money to the banker
	public void BankerDepoNotEnough(String Banker, String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.DepoNotEnough"));
		msg1 = msg1.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(Banker);
		
		p.sendMessage(msg1);
	}

	//Display that the banker successfully deposited amount to the player bank account
	public void BankerDepo(String banker, String player, double amount, double newbankamnt) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.Deposited2"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Depo>", eco.format(amount));
		Player p = Bukkit.getPlayer(banker);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + banker + " has deposited " + eco.format(amount) + " on the " + player + " bank account");
		}
		
		try{
			Player p2 = Bukkit.getPlayer(player);
			String msg2 = replaceMsg(local.getString("Banker.Deposited1"));
			String msg3 = replaceMsg(local.getString("BankAcntAmnt"));
			msg2 = msg2.replaceAll("<Banker>", banker);
			msg2 = msg2.replaceAll("<Depo>", eco.format(amount));
			msg3 = msg3.replaceAll("<BankAmount>", eco.format(newbankamnt));
			
			p2.sendMessage(msg2);
			p2.sendMessage(msg3);
		} catch (NullPointerException e) {
			
		}
	}

	//Display that the player hasn't enough money to the banker
	public void BankerDebitNotEnough(String banker, String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.DebitNotEnough"));
		msg1 = msg1.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(banker);
		
		p.sendMessage(msg1);
	}

	//Display that the banker has successfully created the player bank account and deposited amount on it
	public void BankerPlayerCreateBA(String Banker, String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.CreatedBA"));
		msg1 = msg1.replaceAll("<Depo>", eco.format(amount));
		msg1 = msg1.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(Banker);
		
		p.sendMessage(msg1);
	}

	//Display that the player are not allowed to the banker
	public void BankernotAllowed(String banker, String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.PlayerNotAllowed"));
		msg1 = msg1.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(banker);
		
		p.sendMessage(msg1);
		}

	//Display that the banker successfully debit amount from the player bank account
	public void BankerDebit(String banker, String player, double amount, double newbankamnt) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Banker.Debited2"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Debit>", eco.format(amount));
		Player p = Bukkit.getPlayer(banker);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + banker + " has debited " + eco.format(amount) + " from the " + player + " bank account");
		}
		
		try{
			Player p2 = Bukkit.getPlayer(player);
			String msg2 = replaceMsg(local.getString("Banker.Debited1"));
			String msg3 = replaceMsg(local.getString("BankAcntAmnt"));
			msg2 = msg2.replaceAll("<Banker>", banker);
			msg2 = msg2.replaceAll("<Debit>", eco.format(amount));
			msg3 = msg3.replaceAll("<BankAmount>", eco.format(newbankamnt));
			
			p2.sendMessage(msg2);
			p2.sendMessage(msg3);
		} catch (NullPointerException e) {
		}
	}

	//Display to the banker/admin that the player hasn't bank account
	public void BankerAdminPlayerHasnotBA(String AdminBanker, String player) {
		FileConfiguration local = plugin.local;
		String msg = replaceMsg(local.getString("BankerAdminPlayerNoBA"));
		msg = msg.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(AdminBanker);
		
		p.sendMessage(msg);
	}

	//Display to the banker the player bank account amount
	public void BankerLook(String banker, String player, double bankamnt) {
		FileConfiguration local = plugin.local;
		String msg = replaceMsg(local.getString("Banker.Look"));
		msg = msg.replaceAll("<BankAmount>", eco.format(bankamnt));
		msg = msg.replaceAll("<Player>", player);
	    Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg);
		
	}

	//Display to the admin that the player hasn't enough money on his bank account
	public void AdminPlayerTakeNotEnough(String admin, String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.TakeNotEnough"));
		msg1 = msg1.replaceAll("<Player>", player);
		Player p = Bukkit.getPlayer(admin);
		
		p.sendMessage(msg1);
	}

	//Display that the admin successfully grant amount from the player bank account
	public void AdminPlayerTake(String admin, String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.Take"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Amount>", eco.format(amount));
		Player p = Bukkit.getPlayer(admin);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + admin + " has took " + eco.format(amount) + " from the " + player + " bank account");
		}
	}

	//Display that the admin successfully give amount on the player bank account
	public void AdminPlayerGive(String admin, String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.Give"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Amount>", eco.format(amount));
		Player p = Bukkit.getPlayer(admin);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + admin + " has given " + eco.format(amount) + " on the " + player + " bank account");
		}
	}

	//Display that the admin successfully created the player bank account and deposited amount on it
	public void AdminPlayerCreateBA(String admin, String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.CreatedBA"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Amount>", eco.format(amount));
		Player p = Bukkit.getPlayer(admin);
		
		p.sendMessage(msg1);
	}

	//Display that the admin successfully set the amount of player bank account to the writted one
	public void AdminPlayerSet(String admin, String player, double amount) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Admin.Set"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Amount>", eco.format(amount));
		Player p = Bukkit.getPlayer(admin);
		
		p.sendMessage(msg1);
		
		if (LogtoConsol) {
			logger.info(logTag + admin + " set to " + eco.format(amount) + " the " + player + " bank account");
		}
	}

	//Display the player bank account amount
	public void PlayerConnect(String player, double bankamnt) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("ConnectMessage"));
		msg1 = msg1.replaceAll("<Player>", player);
		msg1 = msg1.replaceAll("<Amount>", eco.format(bankamnt));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display that the player has successfully created his disp sign
	public void PlayerCreatedSign(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Sign.SuccessCreated"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display to the player that the syntax is incorrect
	public void IncorrectSyntax(String player) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Sign.IncorrectSyntax"));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	//Display that the player has successfully break this sign
	public void PlayerBreakSign(String name) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Sign.Deleted"));
		Player p = Bukkit.getPlayer(name);
		
		p.sendMessage(msg1);
	}

	//Display the cost
	public void CreateCost(String player, double createCost) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.CreateCost"));
		msg1 = msg1.replaceAll("<Cost>", eco.format(CreateCost));
		Player p = Bukkit.getPlayer(player);
		
		p.sendMessage(msg1);
	}

	public void PlayerPay(String p1, String p2, double amount, double newBAp2, double newBAp1) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.Pay"));
		String msg4 = replaceMsg(local.getString("BankAcntAmnt"));
		msg4 = msg4.replaceAll("<BankAmount>", eco.format(newBAp1));
		msg1 = msg1.replaceAll("<Player>", p2);
		msg1 = msg1.replaceAll("<Pay>", eco.format(amount));
		Player p = Bukkit.getPlayer(p1);
		
		p.sendMessage(msg1);
		p.sendMessage(msg4);
		
		try {
			String msg2 = replaceMsg(local.getString("Bank.Payed"));
			String msg3 = replaceMsg(local.getString("BankAcntAmnt"));
			msg3 = msg3.replaceAll("<BankAmount>", eco.format(newBAp2));
			msg2 = msg2.replaceAll("<Player>", p1);
			msg2 = msg2.replaceAll("<Pay>", eco.format(amount));
			Player pl = Bukkit.getPlayer(p2);
			
			pl.sendMessage(msg2);
			pl.sendMessage(msg3);
		} catch (NullPointerException e) {
			
		}
	}

	public void PlayerPayNoBA(String p1, String p2) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.paynoBa"));
		msg1 = msg1.replaceAll("<Player>", p2);
		Player p = Bukkit.getPlayer(p1);
		
		p.sendMessage(msg1);
	}

	public void PlayerCantPayHiself(String p1) {
		FileConfiguration local = plugin.local;
		String msg1 = replaceMsg(local.getString("Bank.payHiself"));
		Player p = Bukkit.getPlayer(p1);
		
		p.sendMessage(msg1);
	}
}
