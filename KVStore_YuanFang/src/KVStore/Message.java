package KVStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

	public static byte[] formateRequestMessage(Integer command, byte[] key,
			byte[] value) {
		List<Byte> message = new ArrayList<Byte>();
		message.add(command.byteValue());

		if (key != null)
			message.addAll(standarizeMessage(key, 32));
		if (value != null)
			message.addAll(standarizeMessage(value, 1024));

		byte[] request = new byte[message.size()];
		for (int i = 0; i < message.size(); i++) {
			request[i] = (Byte) message.get(i);
		}
		return request;
	}

	public static byte[] formateReplyMessage(Integer errorCode, byte[] value) {
		List<Byte> message = new ArrayList<Byte>();
		message.add(errorCode.byteValue());
		if (value != null && value.length > 0) {
			for (int i = 0; i < value.length; i++) {
				message.add(value[i]);
			}
		}

		byte[] reply = new byte[message.size()];
		for (int i = 0; i < message.size(); i++) {
			reply[i] = message.get(i);
		}
		return reply;
	}

	private static List<Byte> standarizeMessage(byte[] cmd, int size) {
		List<Byte> message = new ArrayList<Byte>();
		if (cmd.length != size) {
			byte[] temp = new byte[size - cmd.length];
			for (int i = 0; i < temp.length; i++) {
				message.add(temp[i]);
			}
		}

		for (int i = 0; i < cmd.length; i++) {
			message.add(cmd[i]);
		}
		return message;
	}

	public static byte[] checkReplyValue(int command, InputStream in) {
		int errorCode = -2;
		try {
			errorCode = in.read();
			System.out.println("command : " + command);
			System.out.println("error code : " + errorCode);
			if (errorCode == 0 && Message.isCheckReplyValue(command)) {
				System.out.println("Checking reply value.. ");
				byte[] reply = new byte[1024];
				int bytesRcvd;
				int totalBytesRcvd = 0;
				while (totalBytesRcvd < reply.length) {
					if ((bytesRcvd = in.read(reply, totalBytesRcvd,
							reply.length - totalBytesRcvd)) == -1)
						throw new SocketException(
								"connection close prematurely.");
					totalBytesRcvd += bytesRcvd;
				}
				System.out.println("reply : " + Arrays.toString(reply));
				return Message.formateReplyMessage(errorCode, reply);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Message.formateReplyMessage(errorCode, null);
	}

	public static byte[] checkRequestValue(int command, InputStream in) {
		try {
			System.out.println("command " + command);
			if (Message.isCheckRequestValue(command)) {
				byte[] value = new byte[1024];
				int bytesRcvd = 0;
				int totalBytesRcvd = 0;
				while (totalBytesRcvd < value.length) {
					if ((bytesRcvd = in.read(value, totalBytesRcvd,
							value.length - totalBytesRcvd)) == -1)
						throw new SocketException(
								"connection close prematurely.");
					totalBytesRcvd += bytesRcvd;
				}
				return value;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isCheckReplyValue(int command) {
		if (command == 2)
			return true;
		else
			return false;
	}

	public static boolean isCheckRequestValue(int command) {
		if (command == 1)
			return true;
		else
			return false;
	}
}
