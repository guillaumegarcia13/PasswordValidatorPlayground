import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;

public class PasswordValidatorPlayground {
	private static DictionaryRule    dictionnaryRule;
	private static PasswordValidator instance;
	
	public static void main(String[] args) {
		String password = "123456";
		
		System.out.println("Playground");
		System.out.println("----------");

		if (args.length == 0) {
			Console c = System.console();
			password = c.readLine("Enter proposed password: ");
		}
		
		try {
			dictionnaryRule = new DictionaryRule(
				new WordListDictionary(WordLists.createFromReader(
					  // Reader around the word list file
				  	new FileReader[] {new FileReader("assets/forbidden_passwords.txt")},
				  	false,
				  	new ArraysSort())
				)
			);
		} 
		catch (FileNotFoundException e) { dictionnaryRule = null; }
		catch (IOException e)           { dictionnaryRule = null; }
		
		System.out.println("Dictionnary has been initialized");
		
		instance = new PasswordValidator(Arrays.asList(
				new LengthRule(6, 20),
				new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1),
				new CharacterRule(EnglishCharacterData.Digit,     1),
				new CharacterRule(EnglishCharacterData.Special,   1),
				new WhitespaceRule(),
				dictionnaryRule
			)
		);

		System.out.println("Password validator has been initialized");
		
		RuleResult result = instance.validate(new PasswordData(new String(password)));
		System.out.println(result.getDetails().toString());
		System.out.print(" --> password compliance: " + ((Boolean) result.isValid()).toString().toUpperCase());
	}
	
}
