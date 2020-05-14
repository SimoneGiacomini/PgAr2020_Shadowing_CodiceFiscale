package tax_code_calculator;

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

	private static boolean isAllCharacter(String string) {
		String regex = "[a-zA-Z]+";
		return Pattern.compile(regex).matcher(string).matches();
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
		end.append(X_CONSTANT);
		return end.toString();
	}

}
