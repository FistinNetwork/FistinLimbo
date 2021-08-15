package fr.fistin.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class CompoundTag extends NamedTag
{
    public static final byte ID = 10;
    private final Map<String, NamedTag> tags;

    CompoundTag()
    {
        this.tags = new HashMap<>();
    }

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        NBTTag tag;
        while (!((tag = NBTTag.readTag(inputStream)) instanceof EndTag))
            put((NamedTag)tag);
    }

    public NamedTag get(String name)
    {
        return this.tags.get(name);
    }

    private void put(NamedTag tag)
    {
        if (tag != null)
            this.tags.put(tag.getName(), tag);
    }

    public byte getId()
    {
        return 10;
    }

    public String toString()
    {
        return this.tags.toString();
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        for (NamedTag namedTag : this.tags.values())
            namedTag.write(outputStream, writeName);
    }
}
