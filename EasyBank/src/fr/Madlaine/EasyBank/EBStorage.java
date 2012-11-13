package fr.Madlaine.EasyBank;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

public class EBStorage{
	
	private EasyBank plugin;
	private  boolean MysqlIsSet;

	
	public EBStorage(EasyBank easyBank) {
		this.plugin = easyBank;
	}
	
	//Use it to get the amount of player bank account
	public Double getData(String player){
		MysqlIsSet = plugin.MysqlIsSet;
		try {
			if(MysqlIsSet == true) {
				try {
					Connection conn = plugin.conn;
					Statement state = conn.createStatement();
					ResultSet result = state.executeQuery("SELECT * FROM BankAccount WHERE Player='" + player + "'");
					while (result.next()) {
						final NumberFormat formatter = NumberFormat.getInstance();
						final double amount = formatter.parse(result.getString(2)).doubleValue();
						return amount;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				} catch (ParseException e1) {
					e1.printStackTrace();
					return null;
				}
			} else {
				FileConfiguration conf = plugin.getConfig();
				String path = "account." + player + ".ammount";
				
				double amount = conf.getDouble(path, -1);
				if (amount == -1) {
					return null;
				} else {
					return amount;
				}
				
			}
		} catch (NullPointerException e2) {
			e2.printStackTrace();
			return null;
		}		
		return null;
	}
	
	//Use it when you want to change a amount of a bank account which already exist
	public void addData(String player, Double amount) {
		MysqlIsSet = plugin.MysqlIsSet;
		if(MysqlIsSet == true) {
			try {
				Connection conn = plugin.conn;
				Statement state = conn.createStatement();
				state.executeUpdate("UPDATE BankAccount SET Player='" + player + "', Amount=" + amount + "");
				signUpdate(player);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			FileConfiguration conf = plugin.getConfig();
			String path = "account." + player + ".ammount";
			
			conf.set(path, amount);
			plugin.saveConfig();
			signUpdate(player);
		}
	}
	
	//Use it when you want to create a Bank account
	//Necessary with MySql
	public void addNewData(String player, Double amount) {
		MysqlIsSet = plugin.MysqlIsSet;
		if(MysqlIsSet == true) {
			try {
				Connection conn = plugin.conn;
				Statement state = conn.createStatement();
				state.executeUpdate("INSERT INTO BankAccount (Player, Amount) VALUES ('" + player + "', " + amount + ")");
				signUpdate(player);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			FileConfiguration conf = plugin.getConfig();
			String path = "account." + player + ".ammount";
			
			conf.set(path, amount);
			plugin.saveConfig();
			signUpdate(player);
		}
	}
	
	public ArrayList<String> getSignLocation(String player) {
		MysqlIsSet = plugin.MysqlIsSet;
		ArrayList<String> liste = new ArrayList<String>();
		if (MysqlIsSet == true) {
			try {
				Connection conn = plugin.conn;
				Statement state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT * FROM Sign WHERE Player='" + player + "'");
				while(result.next()) {
					final NumberFormat formatter = NumberFormat.getInstance();
					final double x = formatter.parse(result.getString(2)).doubleValue();
					final double y = formatter.parse(result.getString(3)).doubleValue();
					final double z = formatter.parse(result.getString(4)).doubleValue();
					World world = Bukkit.getWorld(result.getString(5));
					liste.add("" + x + "," + y + "," + z + "," + world.getName());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		} else {
			FileConfiguration sign = plugin.sign;
			liste = (ArrayList<String>) sign.getStringList("Sign." + player);
		}
		return liste;
	}
	
	public void addNewSign(String player, double x, double y, double z, String world) {
		MysqlIsSet = plugin.MysqlIsSet;
		if(MysqlIsSet == true) {
			try {
				Connection conn = plugin.conn;
				Statement state = conn.createStatement();
				state.executeUpdate("INSERT INTO Sign (Player, X, Y, Z, world) VALUES ('" + player + "', " + x + ", " + y + ", " + z + ", '" + world + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			FileConfiguration sign = plugin.sign;
			File signFile = plugin.signFile;
			ArrayList<String> liste = (ArrayList<String>) getSignLocation(player);
			liste.add("" + x + "," + y + "," + z + "," + world);
			sign.set("Sign." + player, liste);
			try {
				sign.save(signFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void removeSign(String player, double x, double y, double z, String world) {
		MysqlIsSet = plugin.MysqlIsSet;
		if(MysqlIsSet == true) {
			try {
				Connection conn = plugin.conn;
				Statement state = conn.createStatement();
				state.executeUpdate("DELETE FROM easybank.Sign WHERE sign.Player='" + player + "' AND CONCAT(sign.X)=" + x + " AND CONCAT(sign.Y)=" + y + " AND CONCAT(sign.Z)=" + z + " AND sign.world='" + world + "' LIMIT 1");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			FileConfiguration sign = plugin.sign;
			File signFile = plugin.signFile;
			ArrayList<String> liste = getSignLocation(player);
			for(int i = 0; i < liste.size(); i++) {
				if(liste.get(i).equalsIgnoreCase("" + x + "," + y + "," + z + "," + world)) {
					liste.remove(i);
					sign.set("Sign." + player, liste);
					try {
						sign.save(signFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void signUpdate(String p) {
		ArrayList<String> liste = getSignLocation(p);
		if (liste != null) {
			final NumberFormat formatter = NumberFormat.getInstance();
			String temp;
			int i = 0;
			try {
				for(i = 0; i < liste.size(); i++) {
					temp = liste.get(i);
					String[] temp2 = temp.split(",");
					final double x = formatter.parse(temp2[0]).doubleValue();
					final double y = formatter.parse(temp2[1]).doubleValue();
					final double z = formatter.parse(temp2[2]).doubleValue();
					World world = Bukkit.getWorld(temp2[3]);
					Location loc = new Location(world, x, y, z);
					Sign s = (Sign) loc.getBlock().getState();
					s.setLine(2, "" + getData(p));
					s.update();
				}
			} catch (NullPointerException e) {
				liste.remove(i);
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
	}
}