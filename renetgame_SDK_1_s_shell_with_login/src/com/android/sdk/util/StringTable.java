package com.android.sdk.util;

public class StringTable {
	private static final char[] StringTable = {
		//	0		1		2		3		4		5		6		7		8		9
			'b',	'1',	'x',	'q',	't',	'n',	'\'',	'E',	'l',	'H',//0
			's',	'Y',	'{',	'+',	'>',	'h',	'Z',	'P',	'Q',	'w',//1
			'3',	'S',	')',	'D',	'U',	'`',	'G',	'y',	'~',	'T',//2
			'd',	'%',	'F',	'J',	'^',	'p',	'4',	'g',	'-',	'A',//3
			'e',	'_',	'*',	'?',	'L',	'&',	'K',	'=',	'R',	'"',//4
			'k',	'N',	'C',	';',	'<',	'8',	'r',	'$',	'v',	'a',//5
			'I',	'c',	'2',	'u',	'm',	'i',	'9',	'|',	'6',	',',//6
			'W',	'@',	'B',	'#',	'z',	'V',	']',	'.',	'M',	'0',//7
			'o',	'7',	'[',	'(',	'O',	'}',	'/',	'f',	'j',	'X',//8
			'\\',	'5',	':',	'!',	' '											//9
		};
	
	public static String getStringFromTable(int[] arr) {
		if (null == arr) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int temp : arr) {
			sb.append(StringTable[temp]);
		}
		return sb.toString();
	}
}
