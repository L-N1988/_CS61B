public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque d = new ArrayDeque();
        int i;
        for (i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public boolean isPalindrome(String word) {
        int i;
        int j;
        for (i = 0; i < word.length() / 2; i++) {
            j = word.length() - 1 - i;
            if (word.charAt(j) != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        int i, j;
        for (i = 0; i < word.length() / 2; i++) {
            j = word.length() - 1 - i;
            if (!cc.equalChars(word.charAt(i), word.charAt(j)) && i != j) {
                return false;
            }
        }
        return true;
    }
}
