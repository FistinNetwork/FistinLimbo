package fr.fistin.limbo.network.protocol;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketOutput;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 09:59
 */
public abstract class AbstractProtocol {

    private final Map<ProtocolState, Map<Integer, Class<? extends PacketInput>>> registryIn;
    private final Map<ProtocolState, Map<Integer, Class<? extends PacketOutput>>> registryOut;

    protected final NetworkManager networkManager;

    public AbstractProtocol(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.registryIn = new HashMap<>();
        this.registryOut = new HashMap<>();

        for (ProtocolState state : ProtocolState.values()) {
            this.registryIn.put(state, new HashMap<>());
        }

        for (ProtocolState state : ProtocolState.values()) {
            this.registryOut.put(state, new HashMap<>());
        }
    }

    protected void registerPacketIn(ProtocolState state, int packetId, Class<? extends PacketInput> packetClass) {
        if (packetClass == null) {
            this.registryIn.get(state).remove(packetId);
        } else {
            this.registryIn.get(state).put(packetId, packetClass);
        }
    }

    protected void registerPacketOut(ProtocolState state, int packetId, Class<? extends PacketOutput> packetClass) {
        if (packetClass == null) {
            this.registryOut.get(state).remove(packetId);
        }
        else {
            this.registryOut.get(state).put(packetId, packetClass);
        }
    }

    public abstract ProtocolVersion[] getVersions();

    public final Class<? extends PacketInput> getPacketInById(int packetId, ProtocolState state) {
        return this.registryIn.get(state).get(packetId);
    }

    public final int getPacketOutByClass(Class<? extends PacketOutput> packetClass, ProtocolState state) {
        for (Map.Entry<Integer, Class<? extends PacketOutput>> entry : this.registryOut.get(state).entrySet()) {
            if (entry.getValue().equals(packetClass)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
