package fr.Madlaine.EasyBank;

import java.util.List;

public interface EBApi {

	/** 
	 * Display to the player him bank account amount
	 * @param player
	 */
	public void onPlayerLook(String player);
	
	/** 
	 * Debit amount from the player bank account
	 * @param player
	 * @param amount 
	 */
	public void onPlayerDebit(String player, double amount);
	
	/**
	 * Depo amount on the player bank account
	 * @param player
	 * @param amount
	 */
	public void onPlayerDepo(String player, double amount);
	
	/**
	 * Display to the banker the player bank account amount
	 * @param Banker
	 * @param player
	 */
	public void onBankerLook(String Banker, String player);
	
	/**
	 * Banker debit amount from the player bank account
	 * @param player
	 * @param Banker
	 * @param amount
	 */
	public void onBankerDebit(String player, String Banker, double amount);
	
	/**
	 * Banker depo amount on the player bank account
	 * @param player
	 * @param Banker
	 * @param amount
	 */
	public void onBankerDepo(String player, String Banker, double amount);

	/**
	 * Take, without earning money, amount to the player bank account
	 * @param player
	 * @param admin
	 * @param amount
	 */
	public void onAdminTake(String player, String admin, double amount);

	/**
	 * Give, without losing money, amount to the player bank account
	 * @param player
	 * @param admin
	 * @param amount
	 */
	public void onAdminGive(String player, String admin, double amount);

	/**
	 * Set the player bank account amount to <amount>
	 * @param player
	 * @param admin
	 * @param amount
	 */
	public void onAdminSet(String player, String admin, double amount);
	
	/**
	 * Adding money to the player bank account amount.
	 * <amount> isn't the new bank amount but the amount of the depo
	 * @param Player
	 * @param amount
	 */
	public void AddMoney(String Player, double amount);
	
	/**
     *  Withdraw money to the player bank account amount.
	 * <amount> isn't the new bank amount but the amount of the debit
	 * @param Player
	 * @param amount
	 */
	public void WithdrawMoney(String Player, double amount);

	/**
	 * Set the player bank account amount to <amount>
	 * You've to be sure the player hasn't already a bank account if you use this
	 * @param Player
	 * @param amount
	 */
	public void setMoney(String Player, double amount);

	/**
	 * Create the Player Bank Account and depo <amount> on it
	 * @param Player
	 * @param amount
	 */
	public void CreateBA(String Player, double amount);

	/**
	 * Return the list of the player sign location
	 * The location has this form: "X,Y,Z,World"
	 * @param player
	 * @return List of sing location
	 */
	public List<String> getSignLocation(String player);

	/**
	 * Adding sign on the sign player list
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @param world
	 */
	public void AddSign(String player, double x, double y, double z, String world);

	/**
	 * Removing sign from the sign player list
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @param world
	 */
	public void RemSign(String player, double x, double y, double z, String world);
	
	
}