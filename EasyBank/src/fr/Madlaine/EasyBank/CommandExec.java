package fr.Madlaine.EasyBank;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

public class CommandExec implements CommandExecutor {
	
	private EBBankAdmin AdminControl;
	private EBBanker BankerControl;
	private EBPlayer PlayerControl;
	private EBChat EBChat;
	private static Logger logger = Logger.getLogger("Minecraft");
	private static String logTag = Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "[" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString() + "Easy" + Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString() + "Bank" + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "] " + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString();

	public CommandExec(EBBankAdmin adminControl2, EBBanker bankerControl2, EBPlayer playerControl2, EBChat eBChat2) {
		this.AdminControl = adminControl2;
		this.BankerControl = bankerControl2;
		this.PlayerControl = playerControl2;
		this.EBChat = eBChat2;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		String player = sender.getName();
		
		if(cmd.getName().equalsIgnoreCase("bank")){
			if(!(sender instanceof Player)){
				logger.warning(logTag + "EasyBank only accept player command!");
			} else {
				if (args.length == 2 && args[0].equalsIgnoreCase("depo")) {
					if (sender.hasPermission("EasyBank.depo") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							PlayerControl.onDepo(player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 2 && args[0].equalsIgnoreCase("debit")) {
					if (sender.hasPermission("EasyBank.debit") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							PlayerControl.onDebit(player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 3 && args[0].equalsIgnoreCase("pay")) {
					if (sender.hasPermission("EasyBank.pay") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							PlayerControl.onPlayerPay(player, amount, args[2]);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 1 && args[0].equalsIgnoreCase("?") || args.length == 1 && args[0].equalsIgnoreCase("help")) {
					if (sender.hasPermission("EasyBank.helpmenu") || sender.hasPermission("EasyBank.*")) {
						EBChat.helpMenu(player);
					} else
						EBChat.notAllowed(player);
				} else {
					if (sender.hasPermission("EasyBank.look") || sender.hasPermission("EasyBank.*")) {
						PlayerControl.onLook(player);
					} else
						EBChat.notAllowed(player);
				}
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("banker")){
			if(!(sender instanceof Player)){
				logger.warning(logTag + "EasyBank only accept player command!");
			} else {
				if (args.length == 3 && args[0].equalsIgnoreCase("depo")) {
					if (sender.hasPermission("EasyBank.banker.depo") || sender.hasPermission("EasyBank.banker.*") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							BankerControl.onBankerDepo(args[2], player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 3 && args[0].equalsIgnoreCase("debit")) {
					if (sender.hasPermission("EasyBank.banker.debit") || sender.hasPermission("EasyBank.banker.*") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							BankerControl.onBankerDebit(args[2], player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 2 && args[0].equalsIgnoreCase("look")) {
					if (sender.hasPermission("EasyBank.banker.look") || sender.hasPermission("EasyBank.banker.*") || sender.hasPermission("EasyBank.*")) {
						try {
							BankerControl.onBankerLook(player, args[1]);
						} catch (NullPointerException e) {
							EBChat.mustBeOnline(player, args[1]);
						}	
					} else
						EBChat.notAllowed(player);
				} else {
					if (sender.hasPermission("EasyBank.banker.helpmenu") || sender.hasPermission("EasyBank.banker.*") || sender.hasPermission("EasyBank.*")) {
						EBChat.BankerHelpMenu(player);
					} else
						EBChat.notAllowed(player);
				}
					
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("bankadmin")) {
			if(!(sender instanceof Player)){
				logger.warning(logTag + "EasyBank only accept player command!");
			} else {
				if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
					if (sender.hasPermission("EasyBank.admin.set") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							AdminControl.onAdminSet(args[2], player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("EasyBank.admin.give") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							AdminControl.onAdminGive(args[2], player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else if (args.length == 3 && args[0].equalsIgnoreCase("take")) {
					if (sender.hasPermission("EasyBank.admin.helpmenu") || sender.hasPermission("EasyBank.*")) {
						try {
							final NumberFormat formatter = NumberFormat.getInstance();
							final double amount = formatter.parse(args[1]).doubleValue();
							AdminControl.onAdminTake(args[2], player, amount);
						} catch (final ParseException e) {
							EBChat.invalidAmount(player);
						}
					} else
						EBChat.notAllowed(player);
				} else {
					if (sender.hasPermission("EasyBank.admin.helpmenu") || sender.hasPermission("EasyBank.*")) {
						EBChat.AdminHelpMenu(player);
					} else
						EBChat.notAllowed(player);
				}
			}
			return true;
		}
		
		return false;
	}
}
