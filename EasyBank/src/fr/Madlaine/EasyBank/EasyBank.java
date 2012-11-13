package fr.Madlaine.EasyBank;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;

public class EasyBank extends JavaPlugin{
	
	private static Logger logger = Logger.getLogger("Minecraft");
	private static String logTag = Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "[" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString() + "Easy" + Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString() + "Bank" + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString() + "] " + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString();
	private EBBankAdmin AdminControl;
	private EBBanker BankerControl;
	private EBPlayer PlayerControl;
	private EBChat EBChat;
	private CommandExec myExecutor;
	private EasyBankListener listener;
	private EBStorage storage;
	@SuppressWarnings("unused")
	private EBApi Api;
	@SuppressWarnings("unused")
	private EBApiSet ApiSet;
	
	public Plugin test;
	public LWC lwc;
	public String Version;
	public String DB;
	public boolean MysqlIsSet = false;
	public boolean LogtoConsol = true;
	public double InitialHoldings;
	public double CreateCost;
	public Connection conn;
	public static Economy economy = null;
	public File configFile;
	public FileConfiguration config;
	public File signFile;
	public FileConfiguration sign;
	public File localFile;
	public FileConfiguration local;
	
	public void onEnable(){
		super.onEnable();
		
		EBChat = new EBChat(this);
		storage = new EBStorage(this);
		PlayerControl = new EBPlayer(EBChat, storage, this);
		listener = new EasyBankListener(PlayerControl, storage, EBChat, this);
		AdminControl = new EBBankAdmin(EBChat, storage);
		BankerControl = new EBBanker(EBChat, storage);
		ApiSet = new EBApiSet(PlayerControl, BankerControl, AdminControl, storage);
        myExecutor = new CommandExec(AdminControl, BankerControl, PlayerControl, EBChat);
        
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		try {
			test = pm.getPlugin("LWC");
        
	        if (test != null || test.isEnabled()) {
	        	lwc = ((LWCPlugin)test).getLWC();
	        	logger.info(logTag + "Successfully hooked into LWC !");
	        }
		} catch (NullPointerException e) {
		}
		
		if (!setupEconomy()) {
            logger.severe(logTag + "Vault wasn't found, the plugin is going to disable itself!");
            pm.disablePlugin(this);
        } else {
            pm.registerEvents(listener, this);
        }
		
		try {
			new AutoUpdate(this);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		configFile = new File(this.getDataFolder(),"config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        
        signFile = new File(this.getDataFolder(), "sign.yml");
        sign = YamlConfiguration.loadConfiguration(signFile);
        
        localFile = new File(this.getDataFolder(), "local.yml");
        local = YamlConfiguration.loadConfiguration(localFile);

        try {
	        if(!config.getString("Version").equalsIgnoreCase("0.7a")){
	        	try {
	        		configSet();
	        		config.save(configFile);
	        		logger.info(logTag + "Successfully updated config.yml");
	        	} catch (Exception e) {
	        		logger.severe(logTag + "IOException when creating config.yml in Main.");
	            	e.printStackTrace();
	        	}
	        } else {
	        	logger.info(logTag + "Core Config file loaded.");
	        }
        } catch (NullPointerException e) {
        	try {
        		configSet();
        		config.save(configFile);
        		logger.info(logTag + "Successfully created config.yml");
        	} catch (Exception e1) {
        		logger.severe(logTag + "IOException when creating config.yml in Main.");
            	e1.printStackTrace();
        	}
        }
        
        LogtoConsol = config.getBoolean("logger");
        InitialHoldings = config.getDouble("Initial_Holding", 0D);
        CreateCost = config.getDouble("Create_Account_Cost", 0D);
        
        if (signFile.exists()) {
        	logger.info(logTag + "Core Sign file loaded.");
        } else {
        	try {
        		sign.options().header("This file regroup all the EasyBank sign.");
            	sign.save(signFile);
            	logger.info(logTag + "Successfully created sign.yml");
            } catch (Exception e) {
            	logger.severe(logTag + "IOException when creating sign.yml in Main.");
            	e.printStackTrace();
            }
        }
        
        try {
	        if (local.contains("Bank.Pay")) {
	        	logger.info(logTag + "Core local file loaded.");
	        } else {
	        	try {
	        		EBChat.localSet();
	        		logger.info(logTag + "Successfully updated local.yml");
	        	} catch (Exception e) {
	        		logger.severe(logTag + "IOException when creating local.yml in Main.");
	     			e.printStackTrace();
	        	}
	        }
        } catch (NullPointerException e) {
        	try {
        		EBChat.localSet();
        		logger.info(logTag + "Successfully created local.yml");
        	} catch (Exception e1) {
        		logger.severe(logTag + "IOException when creating local.yml in Main.");
     			e1.printStackTrace();
        	}
        }
        
        if (BankerControl.setupEconomy(economy) && EBChat.setupEconomy(economy) && PlayerControl.setupEconomy(economy)) {
        	logger.info(logTag + "Economy set");
        } else {
        	logger.severe(logTag + "Internal Error when setting Economy");
        	pm.disablePlugin(this);
        }
        
        getCommand("bank").setExecutor(myExecutor);
		getCommand("banker").setExecutor(myExecutor);
		getCommand("bankadmin").setExecutor(myExecutor);
		
		if (config.getBoolean("DataBase.Use_MySql") == true) {
			connect();
		}
		
		if (config.getBoolean("Interest.Use_Interest") == true) {
			long repeat = config.getLong("Interest.Interest_Time") * 20;
			final Object[] ps;
			if (config.getBoolean("Interest.Only_Connected")) {
				ps = Bukkit.getOnlinePlayers();
			} else {
				ps = config.getConfigurationSection("account").getKeys(false).toArray();
			}
			
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				   public void run() {
				       if (config.getString("Interest.Interest_Type").equalsIgnoreCase("percentage")) {
				    	   double interest = config.getDouble("Interest.Interest_Amount", 0D);
				    	   try {
				    		   for (int i = 0; i < ps.length; i++) {
				    			   double bankamnt = storage.getData(ps[i].toString());
				    			   double newba = ((interest/100) * bankamnt) + bankamnt;
				    			   storage.addData(ps[i].toString(), newba);
				    		   }
				    	   } catch (NullPointerException e) {
				    		   //Nothing Loool !!
				    	   }
				       }
				       if (config.getString("Interest.Interest_Type").equalsIgnoreCase("fixed")) {
				    	   double interest = config.getDouble("Interest.Interest_Amount", 0D);
				    	   try {
				    		   for (int i = 0; i < ps.length; i++) {
				    			   double bankamnt = storage.getData(ps[i].toString());
				    			   storage.addData(ps[i].toString(), bankamnt + interest);
				    		   }
				    	   } catch (NullPointerException e) {
				    		   //Nothing Loool !!
				    	   }
				       }
				   }
				}, 1L, repeat);
		}
		
		logger.info(logTag + Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString() + "Enable." + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
	}
	
	public void onDisable(){
		
	}
	
	private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	private void configSet() {
		config.options().header("Welcome to the core of the plugins, please be sure before any edit of this file. To apply any edit, you need to reload your server.");
    	config.set("Version", "0.7a");
    	if (!config.contains("logger")) {
    		config.set("logger", true);
    	}
    	if (!config.contains("Create_Account_Cost")) {
    		config.set("Create_Account_Cost", 0);
    	}
    	if (!config.contains("Initial_Holding")) {
    		config.set("Initial_Holding", 0);
    	}
    	if (!config.contains("DataBase.Use_MySql")) {
    		config.set("DataBase.Use_MySql", false);
    	}
    	if (!config.contains("DataBase.MySql_URL")) {
    		config.set("DataBase.MySql_URL", "jdbc:mysql://localhost:3306/");
    	}
    	if (!config.contains("DataBase.MySql_DB")) {
    		config.set("DataBase.MySql_DB", "EasyBank");
    	}
    	if (!config.contains("DataBase.MySql_UserName")) {
    		config.set("DataBase.MySql_UserName", "root");
    	}
    	if (!config.contains("DataBase.MySql_Password")) {
    		config.set("DataBase.MySql_Password", "");
    	}
    	if (!config.contains("Interest.Use_Interest")) {
    		config.set("Interest.Use_Interest", false);
    	}
    	if (!config.contains("Interest.Only_Connected")) {
    		config.set("Interest.Only_Connected", true);
    	}
    	if (!config.contains("Interest.Interest_Time")) {
    		config.set("Interest.Interest_Time", 60);
    	}
    	if (!config.contains("Interest.Interest_Type")) {
    		config.set("Interest.Interest_Type", "percentage");
    	}
    	if (!config.contains("Interest.Interest_Amount")) {
    		config.set("Interest.Interest_Amount", 10);
    	}
	}
	
	private void connect() {
		String url = config.getString("DataBase.MySql_URL");
		DB = config.getString("DataBase.MySql_DB");
		String user = config.getString("DataBase.MySql_UserName");
		String passwd = config.getString("DataBase.MySql_Password");
		
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
 
        try {
            conn = DriverManager.getConnection(url+DB, user, passwd);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS BankAccount("
            		+ "Player        VARCHAR(254),"
            		+ "Amount        DOUBLE PRECISION)";
            
            String sql2 = "CREATE TABLE IF NOT EXISTS Sign("
            		+ "Player        VARCHAR(254),"
            		+ "X       DOUBLE PRECISION,"
            		+ "Y       DOUBLE PRECISION,"
            		+ "Z       DOUBLE PRECISION,"
            		+ "world        VARCHAR(254))";
            stmt.execute(sql);
            stmt.execute(sql2);
            MysqlIsSet = true;
    		logger.info(logTag + "Successfully connected to the MySql database");
        } catch (SQLException e) {
        	MysqlIsSet = false;
            logger.severe(logTag + Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "Can't connect to the MySql DataBase" + Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
            e.printStackTrace();
        }
	}
}
