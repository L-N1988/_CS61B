import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    // ascii codes is 256 characters totally
    private static final int R = 256;

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        int[] freq = new int[R];
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : inputSymbols) {
            freq[ch] += 1;
        }
        for (int i = 0; i < R; i++) {
            if (freq[i] > 0) {
                map.put((char) i, freq[i]);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("One file parameter is needed!!!");
            return;
        }
        char[] inputFile =  FileUtils.readFile(args[0]);
        String outputFile = args[0] + ".huf";
        BinaryTrie hufBinaryTrie = new BinaryTrie(buildFrequencyTable(inputFile));
        Map<Character, BitSequence> encoderTable = hufBinaryTrie.buildLookupTable();
        List<BitSequence> L = new LinkedList<>();
        ObjectWriter ow = new ObjectWriter(outputFile);
        for (char ch : inputFile) {
            L.add(encoderTable.get(ch));
        }
        // write decoding trie to the huf file
        ow.writeObject(hufBinaryTrie);
        // write bit sequence of input file to the huf file
        ow.writeObject(BitSequence.assemble(L));
    }
}
