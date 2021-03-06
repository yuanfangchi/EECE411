package com.fangchi.eece411.A2.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.fangchi.eece411.A2.Interface.ChatUserInterface;
import com.fangchi.eece411.A2.Utilities.MessageQueue;

public class ChatUserImpl extends UnicastRemoteObject implements
		ChatUserInterface {

	MessageQueue queue;
	private String userName;

	public ChatUserImpl(String userName) throws RemoteException {
		super();
	    queue = new MessageQueue();
	    this.userName = userName;
	}

	@Override
	public boolean broadCastMessage(String message) {
		if (message == null )
			return false;

		System.out.println("Message from Server : " + message);
		queue.enqueue(message);
		return true;
	}
	

	@Override
	public String getUserName() throws RemoteException {
		return userName;
	}
	
	public MessageQueue getMessageQueue(){
		return queue;
	}

}
