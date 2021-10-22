public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new ArrayDeque<>();
        int i;
        for (i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public boolean isPalindrome(String word) {
        Palindrome palindrome = new Palindrome();
        Deque<Character> d = new ArrayDeque<>();
        d = palindrome.wordToDeque(word);
        while (d.size() != 0 && d.size() != 1) {
            if (d.removeFirst() != d.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Palindrome palindrome = new Palindrome();
        Deque<Character> d = new ArrayDeque<>();
        d = palindrome.wordToDeque(word);
        while (d.size() != 0 && d.size() != 1) {
            if (!cc.equalChars(d.removeFirst(), d.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
