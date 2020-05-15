package tax_code_calculator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import person.Person;
import xml.XmlManager;

public abstract class TaxCodeCalculator {
    /** a number to add for create a tax code */
    private static final int NUMBER_FOR_FEMALE_TAXCODE = 30;
    /** the character to put when the letters isn' t enought to create a tax code */
    private static final char X_CONSTANT = 'X';
    /**
     * THIS CONSTANT INDICATES how many characters extracted are useful for the
     * creation of the tax code {@value}
     * 
     * @see #createTaxCodeSurName(String) , {@link #createTaxCodeName(String)},
     *      {@link #extractVocalConsonant(String, List, List)}
     * @author Simone
     */
    private static final int USEFUL_CHARACTER = 3;
    /**
     * a {@linkplain Map} that is used to create the letter of the corresponding
     * month in the tax code
     * 
     * @author Simone
     */
    private static final Map<Integer, String> MONTH = initMonth();
    /**
     * a {@linkplain Set} with Italian vowels inside
     * 
     * @see <a href=
     *      "https://it.wikipedia.org/wiki/Fonologia_della_lingua_italiana">Fonologia
     *      in italiano</a>
     * @author Simone
     */
    private static final Set<Character> VOLWES = initVocal();

    /**
     * method to inizilize the {@linkplain #MONTH} {@linkplain Map}<br>
     * this method create an unmodifiable Map
     * ({@linkplain Collections#unmodifiableMap(Map)})
     * 
     * @author Simone
     */
    private static final Map<Integer, String> initMonth() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "A");
        map.put(2, "B");
        map.put(3, "C");
        map.put(4, "D");
        map.put(5, "E");
        map.put(6, "H");
        map.put(7, "L");
        map.put(8, "M");
        map.put(9, "P");
        map.put(10, "R");
        map.put(11, "S");
        map.put(12, "T");
        map.put((int) 'A', "01");
        map.put((int) 'B', "02");
        map.put((int) 'C', "03");
        map.put((int) 'D', "04");
        map.put((int) 'E', "05");
        map.put((int) 'H', "06");
        map.put((int) 'L', "07");
        map.put((int) 'M', "08");
        map.put((int) 'P', "09");
        map.put((int) 'R', "10");
        map.put((int) 'S', "11");
        map.put((int) 'T', "12");
        return Collections.unmodifiableMap(map);
    }

    private static Set<Character> initVocal() {
        Set<Character> map = new HashSet<Character>();
        map.add('A');
        map.add('E');
        map.add('I');
        map.add('O');
        map.add('U');
        return Collections.unmodifiableSet(map);
    }

    private static final Map<Character, Integer> oddCharacterConverter;
    static {
        Map<Character, Integer> mapBuilder = new HashMap<Character, Integer>();
        mapBuilder.put('0', 1);
        mapBuilder.put('1', 0);
        mapBuilder.put('2', 5);
        mapBuilder.put('3', 7);
        mapBuilder.put('4', 9);
        mapBuilder.put('5', 13);
        mapBuilder.put('6', 15);
        mapBuilder.put('7', 17);
        mapBuilder.put('8', 19);
        mapBuilder.put('9', 21);
        mapBuilder.put('A', 1);
        mapBuilder.put('B', 0);
        mapBuilder.put('C', 5);
        mapBuilder.put('D', 7);
        mapBuilder.put('E', 9);
        mapBuilder.put('F', 13);
        mapBuilder.put('G', 15);
        mapBuilder.put('H', 17);
        mapBuilder.put('I', 19);
        mapBuilder.put('J', 21);
        mapBuilder.put('K', 2);
        mapBuilder.put('L', 4);
        mapBuilder.put('M', 18);
        mapBuilder.put('N', 20);
        mapBuilder.put('O', 11);
        mapBuilder.put('P', 3);
        mapBuilder.put('Q', 6);
        mapBuilder.put('R', 8);
        mapBuilder.put('S', 12);
        mapBuilder.put('T', 14);
        mapBuilder.put('U', 16);
        mapBuilder.put('V', 10);
        mapBuilder.put('W', 22);
        mapBuilder.put('X', 25);
        mapBuilder.put('Y', 24);
        mapBuilder.put('Z', 23);

        oddCharacterConverter = Collections.unmodifiableMap(mapBuilder);
    }

    private static boolean isAllCharacter(String string) {
        String regex = "[a-zA-Z]+";
        if (Pattern.compile(regex).matcher(string).matches())
            return true;
        return false;
    }

    private static boolean isDigit(String string) {
        String regex = "\\d+";
        return Pattern.compile(regex).matcher(string).matches();
    }

    private static boolean isLongEnough(String string, int minLongOfString) {
        if (string.length() < minLongOfString)
            throw new InputMismatchException("The String is too short");
        return true;
    }

    private static boolean extractVocalConsonant(String name, List<Character> vocal, List<Character> consonant) {
        for (int i = 0; i < name.length(); i++) {
            char seq = name.charAt(i);
            if (VOLWES.contains(seq)) {
                vocal.add(seq);
            } else {
                consonant.add(seq);
                if (consonant.size() > USEFUL_CHARACTER)
                    return true;
            }
        }
        return false;
    }

    private static String createTaxCodeName(String name) {
        if (isLongEnough(name, 2) && isAllCharacter(name)) {
            ArrayList<Character> consonant = new ArrayList<>();
            ArrayList<Character> vocal = new ArrayList<>();
            StringBuilder end = new StringBuilder();
            if (extractVocalConsonant(name, vocal, consonant)) {
                return end.append(consonant.get(0)).append(consonant.get(2)).append(consonant.get(3)).toString();
            } else {
                for (Character letter : consonant) {
                    end.append(letter);
                }
                if (end.length() > 2)
                    return end.substring(0, USEFUL_CHARACTER).toString();
                for (Character letter : vocal) {
                    end.append(letter);
                }
                if (end.length() < USEFUL_CHARACTER)
                    return end.append(X_CONSTANT).toString();

                return end.substring(0, USEFUL_CHARACTER).toString();
            }
        }
        return null;
    }

    private static String createTaxCodeSurName(String surname) {
        if (isLongEnough(surname, 1) && isAllCharacter(surname)) {
            ArrayList<Character> consonant = new ArrayList<>();
            ArrayList<Character> vocal = new ArrayList<>();
            StringBuilder end = new StringBuilder();
            extractVocalConsonant(surname, vocal, consonant);
            for (Character letter : consonant) {
                end.append(letter);
            }
            if (end.length() >= USEFUL_CHARACTER)
                return end.substring(0, USEFUL_CHARACTER).toString();
            for (Character letter : vocal) {
                end.append(letter);
            }
            if (end.length() < USEFUL_CHARACTER) {
                byte howMuchLeft = (byte) (USEFUL_CHARACTER - end.length());
                for (int i = 0; i < howMuchLeft; i++) {
                    end.append(X_CONSTANT);
                }
            }
            return end.substring(0, 3).toString();
        }
        return null;
    }

    private static char getControlChar(String code) {
        ArrayList<Character> evenChar = new ArrayList<Character>();
        ArrayList<Character> oddChar = new ArrayList<Character>();

        for (int i = 0; i < code.length(); i++) {
            if (i % 2 == 0) {
                oddChar.add(code.charAt(i));
            } else {
                evenChar.add(code.charAt(i));
            }
        }

        int total = 0;
        for (int i = 0; i < evenChar.size(); i++) {
            char ec = evenChar.get(i);

            if (ec >= '0' && ec <= '9') {
                total += Integer.parseInt(String.format("%c", ec));
            } else {
                total += (((int) ec) - 65);
            }
        }

        for (int i = 0; i < oddChar.size(); i++) {
            total += oddCharacterConverter.get(oddChar.get(i));
        }

        return ((char) ('A' + (total % 26)));
    }

    public final static String taxCodeCalculator(Person p) {
        StringBuilder end = new StringBuilder();
        LocalDate data = p.getBirth_date();// IS MORE EFFICIENT
        end.append(createTaxCodeSurName(p.getSurname()));// FROM THE PERSON SURNAME I TRASFORM INTO THE 3 CHAR THAT I
        end.append(createTaxCodeName(p.getName()));// FROM THE PERSON NAME I TRASFORM INTO THE 3 CHAR THAT I NEED
        end.append(data.format(DateTimeFormatter.ofPattern("uu")));// I ONLY WANT THE 2 LAST DIGIT
        end.append(MONTH.get(data.getMonthValue()));// TRASFORM THE MONTH INTO HIS LETTER
        int day = data.getDayOfMonth();
        day = p.isFemale() ? day + NUMBER_FOR_FEMALE_TAXCODE : day;
        end.append(String.format("%02d", day));
        end.append(p.getBirth_place().getCode_place());
        end.append(getControlChar(end.toString()));
        return end.toString();
    }

    public static boolean isValidTaxCode(String taxCode) {
        if (taxCode.length() != 16) {
            return false;
        }

        taxCode = taxCode.toUpperCase();

        String name = taxCode.substring(0, 3);
        String surname = taxCode.substring(3, 6);
        String birthYear = "19" + taxCode.substring(6, 8);
        String birthMonth = MONTH.get((int) taxCode.charAt(8));
        String birthDay = taxCode.substring(9, 11);

        int day = Integer.parseInt(birthDay);
        if (day >= 41) {
            day -= 40;
        }

        if (day < 1 || day > 31) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(String.format("%s-%s-%s", birthYear, birthMonth, birthDay));

        } catch (DateTimeException e) {
            return false;
        }

        String birthPlace = taxCode.substring(11, 15);
        char controlChar = taxCode.charAt(15);

        if (!name.equals(createTaxCodeName(name)) || !surname.equals(createTaxCodeName(surname))) {
            return false;
        }

        if (!XmlManager.isValidBirthPlaceCode(birthPlace)) {
            return false;
        }

        if (getControlChar(taxCode.substring(0, 15)) != controlChar) {
            return false;
        }

        return true;
    }
}
