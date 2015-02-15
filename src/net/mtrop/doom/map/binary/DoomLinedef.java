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
 * Doom/Boom 14-byte format implementation of Linedef.
 * @author Matthew Tropiano
 */
public class DoomLinedef extends CommonLinedef implements BinaryMapObject
{
	/** Flag: (Boom) Line passes activation through to other lines. */
	protected boolean passThru;
	/** Linedef special tag. */
	protected int tag;

	/**
	 * Creates a new linedef.
	 */
	public DoomLinedef()
	{
	}

	/**
	 * Reads and creates a new DoomLinedef from an array of bytes.
	 * This reads from the first 14 bytes of the stream.
	 * The stream is NOT closed at the end.
	 * @param bytes the byte array to read.
	 * @return a new DoomLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef create(byte[] bytes) throws IOException
	{
		DoomLinedef out = new DoomLinedef();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomLinedef from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomLinedef} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef read(InputStream in) throws IOException
	{
		DoomLinedef out = new DoomLinedef();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Sets this linedef's special tag.
	 */
	public void setTag(int tag)
	{
		this.tag = tag;
	}

	/**
	 * @return this linedef's special tag.
	 */
	public int getTag()
	{
		return tag;
	}

	/**
	 * Sets if this line's activated special does not block the activation search.
	 */
	public void setPassThru(boolean passThru)
	{
		this.passThru = passThru;
	}

	/**
	 * @return true if this line's activated special does not block the activation search, false if so.
	 */
	public boolean isPassThru()
	{
		return passThru;
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
		
		vertexStartIndex = sr.readUnsignedShort();
		vertexEndIndex = sr.readUnsignedShort();
		
		// bitflags
		int flags = sr.readUnsignedShort();
		impassable = Common.bitIsSet(flags, (1 << 0));
		monsterBlocking = Common.bitIsSet(flags, (1 << 1));
		twoSided = Common.bitIsSet(flags, (1 << 2));
		upperUnpegged = Common.bitIsSet(flags, (1 << 3));
		lowerUnpegged = Common.bitIsSet(flags, (1 << 4));
		secret = Common.bitIsSet(flags, (1 << 5));
		soundBlocking = Common.bitIsSet(flags, (1 << 6));
		notDrawn = Common.bitIsSet(flags, (1 << 7));
		mapped = Common.bitIsSet(flags, (1 << 8));
		passThru = Common.bitIsSet(flags, (1 << 9));
		
		special = sr.readUnsignedShort();
		tag = sr.readUnsignedShort();
		sidedefFrontIndex = sr.readShort();
		sidedefBackIndex = sr.readShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws DataExportException, IOException 
	{
		RangeUtils.checkShortUnsigned("Vertex Start Index", vertexStartIndex);
		RangeUtils.checkShortUnsigned("Vertex End Index", vertexEndIndex);
		RangeUtils.checkShortUnsigned("Special", special);
		RangeUtils.checkShortUnsigned("Tag", tag);
		RangeUtils.checkRange("Sidedef Front Index", -1, Short.MAX_VALUE, sidedefFrontIndex);
		RangeUtils.checkRange("Sidedef Back Index", -1, Short.MAX_VALUE, sidedefBackIndex);

		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeUnsignedShort(vertexStartIndex);
		sw.writeUnsignedShort(vertexEndIndex);
		
		sw.writeUnsignedShort(Common.booleansToInt(
			impassable,
			monsterBlocking,
			twoSided,
			upperUnpegged,
			lowerUnpegged,
			secret,
			soundBlocking,
			notDrawn,
			mapped,
			passThru
		));
		
		sw.writeUnsignedShort(special);
		sw.writeUnsignedShort(tag);
		sw.writeShort((short)sidedefFrontIndex);
		sw.writeShort((short)sidedefBackIndex);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Linedef");
		sb.append(' ').append(vertexStartIndex).append(" to ").append(vertexEndIndex);
		sb.append(' ').append("Sidedef ");
		sb.append(' ').append("Front ").append(sidedefFrontIndex);
		sb.append(' ').append("Back ").append(sidedefBackIndex);
		
		if (impassable) sb.append(' ').append("IMPASSABLE");
		if (monsterBlocking) sb.append(' ').append("MONSTERBLOCK");
		if (twoSided) sb.append(' ').append("TWOSIDED");
		if (upperUnpegged) sb.append(' ').append("UPPERUNPEGGED");
		if (lowerUnpegged) sb.append(' ').append("LOWERUNPEGGED");
		if (secret) sb.append(' ').append("SECRET");
		if (soundBlocking) sb.append(' ').append("SOUNDBLOCKING");
		if (notDrawn) sb.append(' ').append("NOTDRAWN");
		if (mapped) sb.append(' ').append("MAPPED");
		if (passThru) sb.append(' ').append("PASSTHRU");
		
		sb.append(' ').append("Special ").append(special);
		sb.append(' ').append("Tag ").append(tag);
		return sb.toString();
	}
	
	
	
}
