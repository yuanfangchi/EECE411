package Interface;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import Exception.InexistentKeyException;
import Exception.InternalKVStoreFailureException;
import Exception.InvalidKeyException;
import Exception.OutOfSpaceException;

public interface ConsistentHashInterface {

	public byte[] put(Integer key, byte[] value) throws InexistentKeyException,
			InternalKVStoreFailureException, InvalidKeyException,
			OutOfSpaceException;

	public byte[] get(Integer key) throws InexistentKeyException,
			InternalKVStoreFailureException, InvalidKeyException;

	public byte[] remove(Integer key) throws InexistentKeyException,
			InternalKVStoreFailureException, InvalidKeyException;

	public byte[] handleAnnouncedFailure()
			throws InternalKVStoreFailureException;

	public void handleNeighbourAnnouncedFailure(Integer key, byte[] value)
			throws InexistentKeyException, InternalKVStoreFailureException,
			InvalidKeyException, OutOfSpaceException;

	public void execInternal(Selector selector, SelectionKey handle,
			int command, byte[] key, byte[] value);

	public void execHashOperation(Selector selector, SelectionKey handle,
			int command, byte[] key, byte[] value);

}
