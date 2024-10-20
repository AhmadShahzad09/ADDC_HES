package com.minsait.oum.mdc.coap.commands.bean;

public enum CommandsEnum {

	OnDemandLoadProfile("LoadProfile"), OnDemandConnect("ReconnectProfile"), OnDemandDisconnect("ReconnectProfile"),
	OnDemandSwitchStatus("ReconnectProfile"), OnDemandSynchronization("SynchronizationProfile"),
	OnDemandEnergyProfile("EnergyProfile"), OnDemandAbsoluteProfile("EnergyProfile"),
	OnDemandBillingDataProfile("BillingProfileProcess"), OnDemandClearAlarms("ClearAlarmsProfileProcess");

	private String commandName;

	public String getCommandName() {
		return commandName;
	}

	private CommandsEnum(String name) {
		this.commandName = name;
	}

	public static String getCommand(String key) {

		switch (key) {
		case "OnDemandLoadProfile":
			return OnDemandLoadProfile.getCommandName();
		case "OnDemandEnergyProfile":
			return OnDemandEnergyProfile.getCommandName();
		case "OnDemandDisconnect":
			return OnDemandDisconnect.getCommandName();
		case "OnDemandConnect":
			return OnDemandConnect.getCommandName();
		case "OnDemandSwitchStatus":
			return OnDemandSwitchStatus.getCommandName();
		case "OnDemandSynchronization":
			return OnDemandSynchronization.getCommandName();
		case "OnDemandAbsoluteProfile":
			return OnDemandAbsoluteProfile.getCommandName();
		case "OnDemandBillingDataProfile":
			return OnDemandBillingDataProfile.getCommandName();
		case "OnDemandClearAlarms":
			return OnDemandClearAlarms.getCommandName();
		default:
			break;
		}
		return null;
	}
}
