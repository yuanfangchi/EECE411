package KVStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Utilities.ErrorEnum;
import Utilities.Message.Message;
import Utilities.Message.MessageUtilities;
import Exception.InexistentKeyException;
import Exception.OutOfSpaceException;

public class PlanetLabNode {

	private ConcurrentHashMap<Integer, byte[]> values = new ConcurrentHashMap<Integer, byte[]>();
	private ConcurrentHashMap<Integer, Integer> version = new ConcurrentHashMap<Integer, Integer>();

	public byte[] put(Integer key, byte[] value) throws InexistentKeyException,
			OutOfSpaceException {
		if (this.values.size() > 40000)
			throw new OutOfSpaceException();

		try {
			this.values.put(key, value);
			if (!this.version.contains(key))
				this.version.put(key, 0);
			else
				this.version.put(key, this.version.get(key) + 1);

		} catch (OutOfMemoryError e) {
			throw new OutOfSpaceException();
		}

		return MessageUtilities.formateReplyMessage(
				ErrorEnum.SUCCESS.getCode(), null, null);
	}

	public boolean put_Local(Integer key, byte[] value)
			throws InexistentKeyException, OutOfSpaceException {
		if (this.values.size() > 40000)
			throw new OutOfSpaceException();

		try {
			this.values.put(key, value);
			if (!this.version.contains(key))
				this.version.put(key, 0);
			else
				this.version.put(key, this.version.get(key) + 1);

		} catch (OutOfMemoryError e) {
			throw new OutOfSpaceException();
		}

		return true;
	}

	public byte[] get(Integer key) throws InexistentKeyException {
		if (this.isInexistentKey(key))
			throw new InexistentKeyException();

		System.out.println("get: "+ key);
		
		return MessageUtilities.formateReplyMessage(
				ErrorEnum.SUCCESS.getCode(), this.values.get(key));
	}

	public byte[] getReplica(Integer key) throws InexistentKeyException {
		if (this.isInexistentKey(key))
			throw new InexistentKeyException();
		
		System.out.println("get replica: "+ key);

		return MessageUtilities.formateReplyMessage(
				ErrorEnum.SUCCESS.getCode(), this.values.get(key),
				this.version.get(key));
	}

	public byte[] remove(Integer key) throws InexistentKeyException {
		if (this.isInexistentKey(key))
			throw new InexistentKeyException();

		this.values.remove(key);
		return MessageUtilities.formateReplyMessage(
				ErrorEnum.SUCCESS.getCode(), null, null);
	}

	public Map<Integer, byte[]> getKeys(int toKey) {
		Map<Integer, byte[]> keys = new HashMap<Integer, byte[]>();
		for (Integer key : this.values.keySet()) {
			if (key <= toKey)
				keys.put(key, this.values.get(key));
		}
		return keys;
	}

	public Map<Integer, byte[]> getKeys() {
		return this.values;
	}

	public void removeAll() {
		this.values.clear();
	}

	private boolean isInexistentKey(Integer key) {
		return (!this.values.containsKey(key));
	}

}
