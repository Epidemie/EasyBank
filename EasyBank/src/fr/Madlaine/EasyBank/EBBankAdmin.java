package fr.Madlaine.EasyBank;

public class EBBankAdmin {
	
	private EBChat EBChat;
	private EBStorage storage;

	public EBBankAdmin(EBChat eBChat2, EBStorage storage2) {
		this.EBChat = eBChat2;
		this.storage = storage2;
	}

	public void onAdminSet(String player, String admin, double amount) {
		try {
			@SuppressWarnings("unused")
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
