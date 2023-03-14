package ee.coar.passwordgen;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;

import java.util.Random;


public class application {
    @FXML
    public Spinner<Integer> numbersLengthSpinner;
    @FXML
    public Spinner<Integer> symbolsLengthSpinner;
    @FXML
    private TextField passwordField;
    @FXML
    private Spinner<Integer> passwordLengthSpinner;
    @FXML
    private CheckBox includeNumbersCheckBox;
    @FXML
    private CheckBox includeSymbolsCheckBox;
    @FXML
    private Label strengthMessage;

    @FXML
    private void generatePassword() {
        if (passwordLengthSpinner == null) {
            return;
        }
        boolean includeNumbers = includeNumbersCheckBox.isSelected();
        boolean includeSymbols = includeSymbolsCheckBox.isSelected();
        int numNumbers = includeNumbers ? numbersLengthSpinner.getValue() : 0;
        int numSymbols = includeSymbols ? symbolsLengthSpinner.getValue() : 0;

        String password = generateRandomPassword(passwordLengthSpinner.getValue(), numNumbers, numSymbols);
        int strength = getPasswordStrength(password);

        String strengthMessageText = "";
        Color color;
        if (strength < 2) {
            strengthMessageText += "Very weak";
            color = Color.RED;
        } else if (strength < 4) {
            strengthMessageText += "Weak";
            color = Color.ORANGE;
        } else if (strength < 6) {
            strengthMessageText += "Fair";
            color = Color.YELLOWGREEN;
        } else if (strength < 8) {
            strengthMessageText += "Good";
            color = Color.GREEN;
        } else if (strength < 10) {
            strengthMessageText += "Strong";
            color = Color.BLUE;
        } else {
            strengthMessageText += "Very strong";
            color = Color.PURPLE;
        }

        if (includeNumbers && numNumbers > 0) {
            strengthMessageText += " ⟩» № " + numNumbers;
        }

        if (includeSymbols && numSymbols > 0) {
            strengthMessageText += " «‽» " + numSymbols;
        }


        strengthMessage.setText(strengthMessageText);
        strengthMessage.setTextFill(color);
        passwordField.setText(password);
    }

    @FXML
    private void generatePin() {
        if (passwordLengthSpinner == null) {
            return;
        }
        int length = passwordLengthSpinner.getValue();
        StringBuilder pin = new StringBuilder();
        Random random = new Random();
        while (pin.length() < length) {
            int digit = random.nextInt(10);
            pin.append(digit);
        }
        for (int i = pin.length() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = pin.charAt(i);
            pin.setCharAt(i, pin.charAt(j));
            pin.setCharAt(j, temp);
        }
        passwordField.setText(pin.toString());
    }

    public int getPasswordStrength(String password) {
        int score = 0;
        if (password.length() < 12) {
            score -= 1;
        } else {
            score += 1;
        }
        int numUppercase = 0;
        int numLowercase = 0;
        int numDigits = 0;
        int numSpecialChars = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                numUppercase++;
            } else if (Character.isLowerCase(c)) {
                numLowercase++;
            } else if (Character.isDigit(c)) {
                numDigits++;
            } else {
                numSpecialChars++;
            }
        }
        if (numUppercase >= password.length() * 0.15) {
            score += 1;
        }
        if (numLowercase >= password.length() * 0.15) {
            score += 1;
        }
        if (numDigits >= password.length() * 0.15) {
            score += 1;
        }
        if (numSpecialChars >= password.length() * 0.10) {
            score += 2;
        }
        if (password.matches("(?=.*[0-9]).*")) {
            score += 2;
        }
        if (password.matches("(?=.*[~!@#$%^&*()_+\\-={}\\[\\]|;:'\"<>,./?]).*")) {
            score += 2;
        }
        if (password.matches(".*[\\p{Punct}&&[^~!@#$%^&*()_+\\-={}\\[\\]|;:'\"<>,./?]].*")) {
            score += 2;
        }
        // Check for rare combinations of letters and numbers
        String[] rareCombinations = {"qaz", "wsx", "edc", "rfv", "tgb", "yhn", "ujm", "ikl", "123", "456", "789", "0", "plo", "kmn", "qwe", "asd", "zxc", "plm", "okn", "ijb", "uhv", "ygt", "rfc", "edv", "swx", "aqz", "lkj", "hgf", "dsq", "wry", "bgt", "fgh", "vbn", "xcv", "nmk", "poi", "987", "654", "321", "098", "765", "432", "246", "135", "579", "048", "1234", "5678", "9012", "abcd", "efgh", "ijkl", "mnop", "qrst", "uvwx", "yz01", "2345", "6789", "0abc", "defg", "hijk", "lmno", "pqrs", "tuvw", "xyz1", "!@#", "$%^", "&*(", "()-", "_+=", "{}|", ":;'", "<,.", ">/?", "[\\]", "~`"};
        for (String combination : rareCombinations) {
            if (password.contains(combination)) {
                score -= 1;
            }
        }

        return score;
    }

    private String generateRandomPassword(int length, int numNumbers, int numSymbols) {
        StringBuilder password = new StringBuilder();

        // Create character sets with all required characters
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "^~!@#$%^&*()_+\\-={}\\[\\]|;:'\"<>,./?";

        // Calculate maximum number of numbers and symbols that can be generated
        int maxNumbers = Math.min(numNumbers, length);
        int maxSymbols = Math.min(numSymbols, length - maxNumbers);

        // Calculate the remaining number of letters
        int remainingLetters = length - maxNumbers - maxSymbols;

        // Add remaining letters, numbers, and symbols
        Random random = new Random();
        for (int i = 0; i < remainingLetters; i++) {
            int index = random.nextInt(letters.length());
            char c = letters.charAt(index);
            password.append(c);
        }
        for (int i = 0; i < maxNumbers; i++) {
            int index = random.nextInt(password.length() + 1);
            char c = numbers.charAt(random.nextInt(numbers.length()));
            password.insert(index, c);
        }
        for (int i = 0; i < maxSymbols; i++) {
            int index = random.nextInt(password.length() + 1);
            char c = symbols.charAt(random.nextInt(symbols.length()));
            password.insert(index, c);
        }

        // Shuffle the password characters
        for (int i = password.length() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(j));
            password.setCharAt(j, temp);
        }

        return password.toString();
    }

    @FXML
    private void initialize() {
        passwordLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 25, 4));
        numbersLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        symbolsLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));


    }

    @FXML
    private void copyButton() {
        String password = passwordField.getText();
        if (password != null && !password.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(password);
            clipboard.setContent(content);
        }
    }

}