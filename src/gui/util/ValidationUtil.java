package gui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

public  class ValidationUtil {

	public static boolean validacaoTamanho(String text) {
		return text.length() >= 2;
	}
	
	public static boolean validacaoEmail(String text) {
		
		return EmailValidator.getInstance().isValid(text);
		
	}
	
	public static boolean validacaoSenha(String text) {
		
		String regex = "^(?=.*[a-z])(?=."
                + "*[A-Z])(?=.*\\d)"
                + "(?=.*[-+_!@#$%^&*., ?]).+$";
		
//		^ representa o início da string.
//		(? =. * [az]) representam pelo menos um caractere minúsculo.
//		(? =. * [AZ]) representa pelo menos um caractere maiúsculo.
//		(? =. * \\ d) representa pelo menos um valor numérico.
//		(? =. * [- + _! @ # $% ^ & *.,?]) representa pelo menos um caractere especial.
//		. representa qualquer caractere, exceto quebra de linha.
//		+ representa uma ou mais vezes.
		
		 Pattern p = Pattern.compile(regex);
		 
		 Matcher m = p.matcher(text);
		 
		 return m.matches();
		
	}

}
