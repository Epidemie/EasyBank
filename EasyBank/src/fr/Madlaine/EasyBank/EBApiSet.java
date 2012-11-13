package fr.Madlaine.EasyBank;

import java.util.List;

public class EBApiSet implements EBApi{
	
	private EBBankAdmin AdminControl;
	private EBBanker BankerControl;
	private EBPlayer PlayerControl;
	private EBStorage storage;

	public EBApiSet(EBPlayer playerControl, EBBanker bankerControl,
			EBBankAdmin adminControl, EBStorage storage) {
		this.AdminControl = adminControl;
		this.BankerControl = bankerControl;
		this.PlayerControl = playerControl;
		this.storage = storage;
	}
	
	@Override
	public void onPlayerLook(String Player) {
		PlayerControl.onLook(Player);
	}

	@Override
	public void onPlayerDepo(String Player, double amount) {
		PlayerControl.onDepo(Player, amount);
	}
	
	@Override
	public void onPlayerDebit(String Player, double amount) {
		PlayerControl.onDebit(Player, amount);
	}
	
	@Override
	public void onBankerLook(String Banker, String Player) {
		BankerControl.onBankerLook(Banker, Player);
	}
	
	@Override
	public void onBankerDepo(String Banker, String Player, double amount) {
		BankerControl.onBankerDepo(Player, Banker, amount);
	}
	
	@Override
	public void onBankerDebit(String Banker, String Player, double amount) {
		BankerControl.onBankerDebit(Player, Banker, amount);
	}
	
	@Override
	public void onAdminSet(String Admin, String Player, double amount) {
		AdminControl.onAdminSet(Player, Admin, amount);
	}
	
	@Override
	public void onAdminTake(String Admin, String Player, double amount) {
		AdminControl.onAdminTake(Player, Admin, amount);
	}
	
	@Override
	public void onAdminGive(String Admin, String Player, double amount) {
		AdminControl.onAdminGive(Player, Admin, amount);
	}
	
	@Override
	public void AddMoney(String Player, double amount) {
		try {
			Double bankamnt = storage.getData(Player);
			double newbankamnt = bankamnt + amount;
			storage.addData(Player, newbankamnt);
		} catch (NullPointerException e) {
			storage.addData(Player, amount);
		}
	}
	
	@Override
	public void WithdrawMoney(String Player, double amount) {
		try {
			Double bankamnt = storage.getData(Player);
			double newbankamnt = bankamnt - amount;
			storage.addData(Player, newbankamnt);
		} catch (NullPointerException e) {
			
		}
	}
	
	@Override
	public void setMoney(String Player, double amount) {
		storage.addData(Player, amount);
	}
	
	@Override
	public void CreateBA(String Player, double amount) {
		storage.addNewData(Player, amount);
	}
	
	@Override
	public List<String> getSignLocation(String player) {
		List<String> SignLocation = storage.getSignLocation(player);
		return SignLocation;
	}
	
	@Override
	public void AddSign(String player, double x, double y, double z, String world) {
		storage.addNewSign(player, x, y, z, world);
	}
	
	@Override
	public void RemSign(String player, double x, double y, double z, String world) {
		storage.removeSign(player, x, y, z, world);
	}
}
