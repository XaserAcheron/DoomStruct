package net.mtrop.doom.map.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.blackrook.commons.Common;
import com.blackrook.io.SuperReader;
import com.blackrook.io.SuperWriter;

import net.mtrop.doom.exception.DataExportException;
import net.mtrop.doom.map.BinaryMapObject;
import net.mtrop.doom.util.RangeUtils;

/**
 * Doom/Boom 10-byte format implementation of Thing.
 * @author Matthew Tropiano
 */
public class DoomThing extends CommonThing implements BinaryMapObject
{
	/** Flag: Thing is not in Cooperative. */
	protected boolean notCooperative;
	/** Flag: Thing is not in Deathmatch. */
	protected boolean notDeathmatch;
	/** Flag: Thing is friendly. */
	protected boolean friendly;
	
	/**
	 * Creates a new thing.
	 */
	public DoomThing()
	{
	}

	/**
	 * Reads and creates a new DoomThing from an array of bytes.
	 * This reads from the first 10 bytes of the stream.
	 * The stream is NOT closed at the end.
	 * @param bytes the byte array to read.
	 * @return a new DoomThing with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomThing create(byte[] bytes) throws IOException
	{
		DoomThing out = new DoomThing();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomThing from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomThing} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomThing with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomThing read(InputStream in) throws IOException
	{
		DoomThing out = new DoomThing();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * @return true if this does NOT appear on cooperative, false if not.
	 */
	public boolean isNotCooperative()
	{
		return notCooperative;
	}

	/**
	 * Sets if this does NOT appear on cooperative.
	 */
	public void setNotCooperative(boolean notCooperative)
	{
		this.notCooperative = notCooperative;
	}

	/**
	 * @return true if this does NOT appear on deathmatch, false if not.
	 */
	public boolean isNotDeathmatch()
	{
		return notDeathmatch;
	}

	/**
	 * Sets if this does NOT appear on deathmatch.
	 */
	public void setNotDeathmatch(boolean notDeathmatch)
	{
		this.notDeathmatch = notDeathmatch;
	}

	/**
	 * @return true if this is flagged as friendly, false if not.
	 */
	public boolean isFriendly()
	{
		return friendly;
	}

	/**
	 * Sets if this is flagged as friendly.
	 */
	public void setFriendly(boolean friendly)
	{
		this.friendly = friendly;
	}

	@Override
	public byte[] toBytes() throws DataExportException
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
		x = sr.readShort();
		y = sr.readShort();
		angle = sr.readUnsignedShort();
		type = sr.readUnsignedShort();
		
		// bitflags
		int flags = sr.readUnsignedShort();
		easy = Common.bitIsSet(flags, (1 << 0));
		medium = Common.bitIsSet(flags, (1 << 1));
		hard = Common.bitIsSet(flags, (1 << 2));
		ambush = Common.bitIsSet(flags, (1 << 3));
		notSinglePlayer = Common.bitIsSet(flags, (1 << 4));
		notDeathmatch = Common.bitIsSet(flags, (1 << 5));
		notCooperative = Common.bitIsSet(flags, (1 << 6));
		friendly = Common.bitIsSet(flags, (1 << 7));
	}

	@Override
	public void writeBytes(OutputStream out) throws DataExportException, IOException
	{
		RangeUtils.checkShort("X-coordinate", x);
		RangeUtils.checkShort("Y-coordinate", y);
		RangeUtils.checkShort("Angle", angle);
		RangeUtils.checkShort("Type", type);
		
		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeShort((short)x);
		sw.writeShort((short)y);
		sw.writeShort((short)angle);
		sw.writeShort((short)type);
		
		sw.writeUnsignedShort(Common.booleansToInt(
			easy,
			medium,
			hard,
			ambush,
			notSinglePlayer,
			notDeathmatch,
			notCooperative,
			friendly
		));		
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Thing");
		sb.append(" (").append(x).append(", ").append(y).append(")");
		sb.append(" Type:").append(type);
		sb.append(" Angle:").append(angle);

		if (easy) sb.append(' ').append("EASY");
		if (medium) sb.append(' ').append("MEDIUM");
		if (hard) sb.append(' ').append("HARD");
		if (ambush) sb.append(' ').append("AMBUSH");
		if (notSinglePlayer) sb.append(' ').append("NOTSINGLEPLAYER");
		if (notDeathmatch) sb.append(' ').append("NOTDEATHMATCH");
		if (notCooperative) sb.append(' ').append("NOTCOOPERATIVE");
		if (friendly) sb.append(' ').append("FRIENDLY");
		
		return sb.toString();
	}

}
