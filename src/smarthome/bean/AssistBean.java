package smarthome.bean;

import java.io.Serializable;

/**
 * @author benjaminwan
 *用于保存界面数据
 */
public class AssistBean implements Serializable{
	private static final long serialVersionUID = -5620661009186692227L;
	private boolean isTxt=true;
	private String SendTxtA="COMA";
	private String SendHexA="AA";
	public String sTimeA="60000";
	
	
	public boolean isTxt()
	{
		return isTxt;
	}
	public void setTxtMode(boolean isTxt)
	{
		this.isTxt = isTxt;
	}
	
	public String getSendA()
	{
		if (isTxt)
		{
			return SendTxtA;
		} else
		{
			return SendHexA;
		}
	}
	

	
	public void setSendA(String sendA)
	{
		if (isTxt)
		{
			SendTxtA = sendA;
		} else
		{
			SendHexA = sendA;
		}
	}
	


}