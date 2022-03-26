public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Two file parameters is needed!!!");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        ObjectReader or = new ObjectReader(inputFile);
        StringBuilder str = new StringBuilder();
        /* Read first object from the file. */
        BinaryTrie hufBinaryTrie = (BinaryTrie) or.readObject();
        /* Read second object from the file. */
        BitSequence input = (BitSequence) or.readObject();
        while (input.length() > 0) {
            Match m = hufBinaryTrie.longestPrefixMatch(input);
            str.append(m.getSymbol());
            input = input.allButFirstNBits(m.getSequence().length());
        }
        FileUtils.writeCharArray(outputFile, str.toString().toCharArray());
    }
}
