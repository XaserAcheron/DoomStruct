package net.mtrop.doom.map.bsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.RangeUtils;

import com.blackrook.commons.Common;
import com.blackrook.io.SuperReader;
import com.blackrook.io.SuperWriter;

/**
 * This contains the BSP tree information for a single 28-byte node in the tree.
 * @author Matthew Tropiano
 */
public class BSPNode implements BinaryObject
{
	/** Byte length of this object. */
	public static final int LENGTH = 28;

	/** Leaf node value. */
	public static final int LEAF_NODE_INDEX = 0xffff8000;
	
	/** This node's partition line's X-coordinate. */
	protected int partitionLineX;
	/** This node's partition line's Y-coordinate. */
	protected int partitionLineY;
	/** This node's partition line's change in X to the end of the line. */
	protected int partitionDeltaX;
	/** This node's partition line's change in Y to the end of the line. */
	protected int partitionDeltaY;
	/** This node's right bounding box coordinates. */
	protected int[] rightRect;
	/** This node's left bounding box coordinates. */
	protected int[] leftRect;

	/** This node's right child index or subsector index. */
	protected int rightSubsectorIndex;
	/** This node's left child index or subsector index. */
	protected int leftSubsectorIndex;

	/**
	 * Creates a new BSP Node.
	 */
	public BSPNode()
	{
		partitionLineX = 0;
		partitionLineY = 0;
		partitionDeltaX = 0;
		partitionDeltaY = 0;
		rightRect = new int[4];
		leftRect = new int[4];
		rightSubsectorIndex = 0;
		leftSubsectorIndex = 0;
	}

	/**
	 * Reads and creates a new BSPNode from an array of bytes.
	 * This reads from the first 28 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new BSPNode with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static BSPNode create(byte[] bytes) throws IOException
	{
		BSPNode out = new BSPNode();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new BSPNode from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link BSPNode} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new BSPNode with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static BSPNode read(InputStream in) throws IOException
	{
		BSPNode out = new BSPNode();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new BSPNodes from an array of bytes.
	 * This reads from the first 28 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of BSPNode objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static BSPNode[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new BSPNodes from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link BSPNode}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of BSPNode objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static BSPNode[] read(InputStream in, int count) throws IOException
	{
		BSPNode[] out = new BSPNode[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new BSPNode();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/** 
	 * Sets this node's partition line's X-coordinate. 
	 * @throws IllegalArgumentException if the value is outside the range -32768 to 32767. 
	 */
	public void setPartitionLineX(int val)
	{
		RangeUtils.checkShort("Partition Line X", partitionLineX);
		partitionLineX = val;
	}

	/** 
	 * Sets this node's partition line's Y-coordinate. 
	 * @throws IllegalArgumentException if the value is outside the range -32768 to 32767. 
	 */
	public void setPartitionLineY(int val)
	{
		RangeUtils.checkShort("Partition Line Y", partitionLineY);
		partitionLineY = val;
	}

	/** 
	 * Sets this node's partition line's change in X to the end of the line. 
	 * @throws IllegalArgumentException if the value is outside the range -32768 to 32767. 
	 */
	public void setPartitionDeltaX(int val)
	{
		RangeUtils.checkShort("Partition Delta X", partitionDeltaX);
		partitionDeltaX = val;
	}

	/** 
	 * Sets this node's partition line's change in Y to the end of the line. 
	 * @throws IllegalArgumentException if the value is outside the range -32768 to 32767. 
	 */
	public void setPartitionDeltaY(int val)
	{
		RangeUtils.checkShort("Partition Delta Y", partitionDeltaY);
		partitionDeltaY = val;
	}

	/**
	 * Sets this node's right bounding box coordinates (top, bottom, left, right).
	 * @throws IllegalArgumentException if any of the values are outside the range -32768 to 32767. 
	 */
	public void setRightRect(int top, int bottom, int left, int right)
	{
		RangeUtils.checkShort("Right Box Top", rightRect[0]);
		RangeUtils.checkShort("Right Box Bottom", rightRect[1]);
		RangeUtils.checkShort("Right Box Left", rightRect[2]);
		RangeUtils.checkShort("Right Box Right", rightRect[3]);
		rightRect[0] = top;
		rightRect[1] = bottom;
		rightRect[2] = left;
		rightRect[3] = right;
	}

	/**
	 * Sets this node's left bounding box coordinates (top, bottom, left, right).
	 * @throws IllegalArgumentException if any of the values are outside the range -32768 to 32767. 
	 */
	public void setLeftRect(int top, int bottom, int left, int right)
	{
		RangeUtils.checkShort("Left Box Top", leftRect[0]);
		RangeUtils.checkShort("Left Box Bottom", leftRect[1]);
		RangeUtils.checkShort("Left Box Left", leftRect[2]);
		RangeUtils.checkShort("Left Box Right", leftRect[3]);
		leftRect[0] = top;
		leftRect[1] = bottom;
		leftRect[2] = left;
		leftRect[3] = right;
	}

	/** 
	 * Sets this node's right subsector index. 
	 * @throws IllegalArgumentException if the value is outside the range 0 to 32767, or isn't {@link BSPNode#LEAF_NODE_INDEX}. 
	 */
	public void setRightSubsectorIndex(int val)
	{
		if (val == LEAF_NODE_INDEX)
			rightSubsectorIndex = val;
		else
		{
			RangeUtils.checkRange("Right Subsector Index", 0, 32767, rightSubsectorIndex);
			rightSubsectorIndex = val;
		}
	}

	/** 
	 * Sets this node's left subsector index. 
	 * @throws IllegalArgumentException if the value is outside the range 0 to 32767, or isn't {@link BSPNode#LEAF_NODE_INDEX}. 
	 */
	public void setLeftSubsectorIndex(int val)
	{
		if (val == LEAF_NODE_INDEX)
			leftSubsectorIndex = val;
		else
		{
			RangeUtils.checkRange("Left Subsector Index", 0, 32767, leftSubsectorIndex);
			leftSubsectorIndex = val;
		}
	}

	/** @return this node's partition line's X-coordinate. */
	public int getPartitionLineX()
	{
		return partitionLineX;
	}

	/** @return this node's partition line's Y-coordinate. */
	public int getPartitionLineY()
	{
		return partitionLineY;
	}

	/** @return this node's partition line's change in X to the end of the line. */
	public int getPartitionDeltaX()
	{
		return partitionDeltaX;
	}

	/** @return this node's partition line's change in Y to the end of the line. */
	public int getPartitionDeltaY()
	{
		return partitionDeltaY;
	}

	/**
	 * @return this node's right bounding box coordinates (top, bottom, left, right).
	 */
	public int[] getRightRect()
	{
		return rightRect;
	}

	/**
	 * @return this node's left bounding box coordinates (top, bottom, left, right).
	 */
	public int[] getLeftRect()
	{
		return leftRect;
	}

	/** 
	 * @return true, if this node's right node is a leaf, false if not. 
	 */
	public boolean getRightChildIsLeaf()
	{
		return rightSubsectorIndex == LEAF_NODE_INDEX;
	}

	/** 
	 * @return this node's right subsector index. 
	 */
	public int getRightSubsectorIndex()
	{
		return rightSubsectorIndex;
	}

	/** 
	 * @return true, if this node's left node is a leaf, false if not.  
	 */
	public boolean getLeftChildIsLeaf()
	{
		return leftSubsectorIndex == LEAF_NODE_INDEX;
	}

	/** 
	 * @return this node's left subsector index. 
	 */
	public int getLeftSubsectorIndex()
	{
		return leftSubsectorIndex;
	}

	@Override
	public byte[] toBytes()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try { writeBytes(bos); } catch (IOException e) { /* Shouldn't happen. */ }
		return bos.toByteArray();
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		readBytes(bin);
		Common.close(bin);
	}

	@Override
	public void readBytes(InputStream in) throws IOException
	{
		SuperReader sr = new SuperReader(in, SuperReader.LITTLE_ENDIAN);
		partitionLineX = sr.readShort();
		partitionLineY = sr.readShort();
		partitionDeltaX = sr.readShort();
		partitionDeltaY = sr.readShort();
		for (int i = 0; i < 4; i++)
			rightRect[i] = sr.readShort();
		for (int i = 0; i < 4; i++)
			leftRect[i] = sr.readShort();
		rightSubsectorIndex = sr.readShort();
		leftSubsectorIndex = sr.readShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeShort((short)partitionLineX);
		sw.writeShort((short)partitionLineY);
		sw.writeShort((short)partitionDeltaX);
		sw.writeShort((short)partitionDeltaY);
		for (int i = 0; i < 4; i++)
			sw.writeShort((short)rightRect[i]);
		for (int i = 0; i < 4; i++)
			sw.writeShort((short)leftRect[i]);
		sw.writeShort((short)rightSubsectorIndex);
		sw.writeShort((short)leftSubsectorIndex);
	}

}
