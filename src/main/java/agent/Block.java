package agent;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//@JsonPropertyOrder({ "index", "timestamp","hash","previousHash","data","nonce"})
public class Block implements Serializable {

    private static final long serialVersionUID = 2L;

    private int index;
    private Long timestamp;
    //private transient Blockhash hash;  // this field is not serialized 
    private Blockhash hash;
    private Blockhash previousHash;
    private String data;
    private int nonce;

    // for jackson
    public Block() {
    }

    @Override
    public String toString() {
        return "Block{"
                + "index=" + index
                + ", timestamp=" + timestamp
                + ", data=" + data
                + //                ", hash='" + hash + '\'' +
                //                ", previousHash='" + previousHash + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Block block = (Block) o;
        return index == block.index
                && timestamp.equals(block.timestamp)
                && hash.equals(block.hash)
                && previousHash.equals(block.previousHash)
                && data.equals(block.data);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + hash.hashCode();
        result = 31 * result + previousHash.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    public Block(int index, long timestamp, Blockhash preHash, String data) {
        this.index = index;
        this.previousHash = preHash;
        this.data = data;
        this.timestamp = timestamp;
        hash = calculateHash();
    }

    public String getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Blockhash getHash() {
        return hash;
    }

    public Blockhash getPreviousHash() {
        return previousHash;
    }

    public int getNonce() {
        return nonce;
    }

    public String str() {
        return index + timestamp + previousHash.toString() + data + nonce;
    }

    public byte[] toBytes() {
        ByteBuffer blockBytes = ByteBuffer.allocate(4 + 8 + 32 + data.length() + 4);
        blockBytes.put(new byte[]{
            (byte) (index >>> 24),
            (byte) (index >>> 16),
            (byte) (index >>> 8),
            (byte) index});
        blockBytes.put(new byte[]{
            (byte) (timestamp >>> 56),
            (byte) (timestamp >>> 48),
            (byte) (timestamp >>> 40),
            (byte) (timestamp >>> 32),
            (byte) (timestamp >>> 24),
            (byte) (timestamp >>> 16),
            (byte) (timestamp >>> 8),
            (byte) (timestamp & 0xff)});
        blockBytes.put((previousHash == null) ? new byte[32] : previousHash.getHash().getBytes());
        blockBytes.put(data.getBytes());
        blockBytes.put(new byte[]{
            (byte) (nonce >>> 24),
            (byte) (nonce >>> 16),
            (byte) (nonce >>> 8),
            (byte) nonce});
        return blockBytes.array();
    }

//    private void writeObject(java.io.ObjectOutputStream stream)
//            throws IOException {
//        stream.writeInt(index);
//        stream.writeLong(timestamp);
//        stream.writeObject(previousHash);
//        stream.writeBytes(data);
//        stream.writeInt(nonce);
//    }
//
//    private void readObject(java.io.ObjectInputStream stream)
//            throws IOException, ClassNotFoundException {
//        index = stream.readInt();
//        timestamp = stream.readLong();
//        previousHash = (Blockhash) stream.readObject();
//        data = (String) stream.readObject();
//        nonce = stream.readInt();
//    }

//    public static byte[] serializeObject(Block b) throws IOException {
//        ObjectOutputStream oos;
//        byte[] bytes;
//        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
//            oos = new ObjectOutputStream(bytesOut);
//            oos.writeObject(b);
//            oos.flush();
//            bytes = bytesOut.toByteArray();
//        }
//        oos.close();
//        return bytes;
//    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Block #").append(index).append(" [previousHash : ").append(previousHash).append(", ").
//                append("timestamp : ").append(new Date(timestamp)).append(", ").append("data : ").append(data).append(", ").
//                append("hash : ").append(hash).append("]");
//        return builder.toString();
//    }
//

    public final Blockhash calculateHash() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return new Blockhash(digest.digest(toBytes()));
    }

    public void mineBlock(int difficulty) {
        nonce = 0;
        Blockhash leadingZeros = new Blockhash(new byte[difficulty]);
        long startTime = System.nanoTime();
        while (!leadingZeros.rangedEquals(getHash(), 0, 0, difficulty)) {
            nonce++;
            hash = calculateHash();
        }
        long endTime = System.nanoTime();
        System.out.println("hash = " + hash + " Hash Rate = "
                + ((float) (nonce * 1.0e6) / (endTime - startTime)) + " kH/s");  //divide by 1000000 to get milliseconds.        
    }
}
