package fr.Madlaine.EasyBank;

import java.text.NumberFormat;
import java.text.ParseException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class EasyBankListener implements Listener{

	private Economy eco;
	private EasyBank plugin;
	private EBPlayer PlayerControl;
	private EBChat EBChat;
	private EBStorage storage;
	String signTag = ChatColor.WHITE + "Easy" + ChatColor.GOLD + "Bank";

	public EasyBankListener(EBPlayer playerControl2, EBStorage storage2, EBChat eBChat2, EasyBank easyBank) {
		this.PlayerControl = playerControl2;
		this.storage = storage2;
		this.EBChat = eBChat2;
		this.plugin = easyBank;
	}

	public boolean setupEconomy(Economy economy) {
        eco = economy;
        return true;
    }
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("EasyBank.connectmessage")) {
			try {
				String player = e.getPlayer().getDisplayName();
				double bankamnt = storage.getData(player);
				EBChat.PlayerConnect(player, bankamnt);
			} catch (NullPointerException e1) {
				//Nooooooootttthhhhhiiiinnngggg !!!! LLOOLL
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent e) {
		
		String[] line = e.getLines();
		
		if (line[0].equalsIgnoreCase("[EasyBank]")){
			
			Player p = e.getPlayer();
			String player = p.getDisplayName();
			Sign s = (Sign) e.getBlock().getState();
			double x = s.getLocation().getX();
			double y = s.getLocation().getY();
			double z = s.getLocation().getZ();
			String world = s.getLocation().getWorld().getName();
			
			if (line[1].isEmpty() && line[2].isEmpty() && line[3].isEmpty()) {
				if (p.hasPermission("EasyBank.sign.create.disp") || p.hasPermission("EasyBank.*")) {
					try {
						String bankamnt = storage.getData(player).toString();
						
						e.setLine(0, signTag);
						e.setLine(1, player);
						e.setLine(2, bankamnt);
						
						storage.addNewSign(player, x, y, z, world);
						
						EBChat.PlayerCreatedSign(player);
					} catch (NullPointerException e1) {
						e.setLine(0, signTag);
						e.setLine(1, player);
						e.setLine(2, "NoBankAccount");
						
						storage.addNewSign(player, x, y, z, world);
						
						EBChat.PlayerCreatedSign(player);
					}
				} else {
					EBChat.notAllowed(player);
				}
			} else if (line[1].equalsIgnoreCase("depo")) {
				if (p.hasPermission("EasyBank.sign.create.depo") || p.hasPermission("EasyBank.*")) {
					try {
						final NumberFormat formatter = NumberFormat.getInstance();
						final double amount = formatter.parse(line[2]).doubleValue();
						if(amount > 0) {
							e.setLine(0, signTag);
							e.setLine(1, ChatColor.GREEN + "Deposit");
		            		e.setLine(3, "");
		            		EBChat.PlayerCreatedSign(player);
						} else {
							e.setCancelled(true);
							EBChat.invalidAmount(player);
							s.setTypeId(0);
							p.getInventory().addItem(new ItemStack[] { new ItemStack(323, 1) });
						}
					} catch (ParseException e1) {
						e.setCancelled(true);
						EBChat.invalidAmount(player);
						s.setTypeId(0);
						p.getInventory().addItem(new ItemStack[] { new ItemStack(323, 1) });
					}
				} else {
					EBChat.notAllowed(player);
				}
			} else if (line[1].equalsIgnoreCase("debit")) {
				if (p.hasPermission("EasyBank.sign.create.debit") || p.hasPermission("EasyBank.*")) {
					try {
						final NumberFormat formatter = NumberFormat.getInstance();
						final double amount = formatter.parse(line[2]).doubleValue();
						if(amount > 0) {
							e.setLine(0, signTag);
							e.setLine(1, ChatColor.GREEN + "Debit");
		            		e.setLine(3, "");
		            		EBChat.PlayerCreatedSign(player);
						} else {
							e.setCancelled(true);
							EBChat.invalidAmount(player);
							s.setTypeId(0);
							p.getInventory().addItem(new ItemStack[] { new ItemStack(323, 1) });
						}
					} catch (ParseException e1) {
						e.setCancelled(true);
						EBChat.invalidAmount(player);
						s.setTypeId(0);
						p.getInventory().addItem(new ItemStack[] { new ItemStack(323, 1) });
					}
				} else {
					EBChat.notAllowed(player);
				}
			} else {
				e.setCancelled(true);
				EBChat.invalidAmount(player);
				s.setTypeId(0);
				p.getInventory().addItem(new ItemStack[] { new ItemStack(323, 1) });
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInterract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign && ((Sign) e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase(signTag)) {
			Sign s = (Sign) e.getClickedBlock().getState();
			Player p = e.getPlayer();
			if (s.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Deposit")) {
				if (p.hasPermission("EasyBank.sign.use.debit") || p.hasPermission("EasyBank.*")) {
					try {
						final NumberFormat formatter = NumberFormat.getInstance();
						final double amount = formatter.parse(s.getLine(2)).doubleValue();
						PlayerControl.onDepo(p.getDisplayName(), amount);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				} else {
					e.setCancelled(true);
					EBChat.notAllowed(p.getDisplayName());
				}
			}
			if (s.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Debit")) {
				if (p.hasPermission("EasyBank.sign.use.depo") || p.hasPermission("EasyBank.*")) {
					try {
						final NumberFormat formatter = NumberFormat.getInstance();
						final double amount = formatter.parse(s.getLine(2)).doubleValue();
						PlayerControl.onDebit(p.getDisplayName(), amount);
					} catch (ParseException e1) {
						e.setCancelled(true);
						e1.printStackTrace();
					}
				} else {
					e.setCancelled(true);
					EBChat.notAllowed(p.getDisplayName());
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerBreak(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof Sign && ((Sign) e.getBlock().getState()).getLine(0).equalsIgnoreCase(signTag)) {
			String[] line = ((Sign) e.getBlock().getState()).getLines();
			Player p = e.getPlayer();
			
			if (line[1].equalsIgnoreCase(ChatColor.GREEN + "Deposit")) {
				if (p.hasPermission("EasyBank.sign.break.depo") || p.hasPermission("EasyBank.*")) {
					EBChat.PlayerBreakSign(p.getDisplayName());
				} else {
					EBChat.notAllowed(p.getDisplayName());
					e.setCancelled(true);
				}
			} else if (line[1].equalsIgnoreCase(ChatColor.GREEN + "Debit")) {
				if (p.hasPermission("EasyBank.sign.break.debit") || p.hasPermission("EasyBank.*")) {
					EBChat.PlayerBreakSign(p.getDisplayName());
				} else {
					EBChat.notAllowed(p.getDisplayName());
					e.setCancelled(true);
				}
			} else {
				
				double x = e.getBlock().getLocation().getX();
				double y = e.getBlock().getLocation().getY();
				double z = e.getBlock().getLocation().getZ();
				String world = e.getBlock().getWorld().getName();
				
				if (line[1].equalsIgnoreCase(p.getDisplayName())) {
					if (p.hasPermission("EasyBank.sign.break.disp.my") || p.hasPermission("EasyBank.sign.break.disp.other")) {
						storage.removeSign(p.getDisplayName(), x, y, z, world);
						EBChat.PlayerBreakSign(p.getDisplayName());
					} else {
						EBChat.notAllowed(p.getDisplayName());
						e.setCancelled(true);
					}
				} else {
					if (p.hasPermission("EasyBank.sign.break.disp.other") || p.hasPermission("EasyBank.sign.break.disp.other")) {
						storage.removeSign(p.getDisplayName(), x, y, z, world);
						EBChat.PlayerBreakSign(p.getDisplayName());
					} else {
						EBChat.notAllowed(p.getDisplayName());
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
