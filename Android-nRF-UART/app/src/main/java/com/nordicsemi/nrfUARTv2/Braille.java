package com.nordicsemi.nrfUARTv2;

import android.util.Log;

public class Braille {
    private final int ASCII_0 = 48;
    private final int ASCII_9 = 57;
    private final int ASCII_A = 65;
    private final int ASCII_Z = 90;
    private final int ASCII_a = 97;
    private final int ASCII_z = 122;

    private final int ASCII_space = 32;
    private final int ASCII_exclamation = 33;
    private final int ASCII_doublequote = 34;
    private final int ASCII_apostrophe = 39;
    private final int ASCII_openparenthesis = 40;
    private final int ASCII_closeparenthesis = 41;
    private final int ASCII_comma = 44;
    private final int ASCII_hyphen = 45;
    private final int ASCII_period = 46;
    private final int ASCII_colon = 58;
    private final int ASCII_semicolon = 59;
    private final int ASCII_question = 63;

    // 점자 규칙 - 1은 튀어나온 부분, 0은 들어간 부분
    private final static String[] BrailleData_alphabet = { // 알파벳
            "100000", /*a*/ "101000", /*b*/ "110000", /*c*/ "110100", /*d*/
            "100100", /*e*/ "111000", /*f*/ "111100", /*g*/ "101100", /*h*/
            "011000", /*i*/ "011100", /*j*/ "100010", /*k*/ "101010", /*l*/
            "110010", /*m*/ "110110", /*n*/ "100110", /*o*/ "111010", /*p*/
            "111110", /*q*/ "101110", /*r*/ "011010", /*s*/ "011110", /*t*/
            "100011", /*u*/ "101011", /*v*/ "011101", /*w*/ "110011", /*x*/
            "110111", /*y*/ "100111"  /*z*/
    };
    private final static String BrailleData_capital = "000001"; // 대문자
    private final static String BrailleData_doublecapital = "000001000001"; // 겹대문자

    private final static String[] BrailleData_number = { // 숫자
            "011100", /*0*/ "100000", /*1*/ "101000", /*2*/ "110000", /*3*/
            "110100", /*4*/ "100100", /*5*/ "111000", /*6*/ "111100", /*7*/
            "101100", /*8*/ "011000"  /*9*/
    };
    private final static String BrailleData_numbersign = "010111"; // 숫자 sign

    private final static String BrailleData_space = "000000"; // 스페이스
    private final static String BrailleData_exclamation = "001110"; // 느낌표
    private final static String BrailleData_opendoublequote = "001011"; // 여는 쌍따옴표
    private final static String BrailleData_closedoublequote = "000111"; // 닫는 쌍따옴표
    private final static String BrailleData_apostrophe = "000010"; // 어포스트로피
    private final static String BrailleData_parenthesis = "001111"; // 괄호
    private final static String BrailleData_comma = "001000"; // 쉼표
    private final static String BrailleData_hyphen = "000011"; // 하이픈
    private final static String BrailleData_period = "001101"; // 마침표
    private final static String BrailleData_colon = "001100"; // 콜론
    private final static String BrailleData_semicolon = "001010"; // 세미콜론
    private final static String BrailleData_question = "001011"; // 물음표

    String data;                              // 점자 데이터
    String bytedata;                         // byte string으로 변환

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Braille(){
        data = "";
        bytedata = "";
    }

    Braille(String s) {                       // constructor
        makeBraille(s);
    }

    String toBraille(String s) {
        makeBraille(s);
        return bytedata;
    }

    private void makeBraille (String s) {
        data=""; bytedata=""; // initialize
        int length = s.length();
        boolean number_reading = false; // 지금 숫자를 읽는 중
        boolean capital_continue = false; // 지금 대문자가 연속으로 등장하는 중
        boolean doublequote_using = false; // 지금 쌍따옴표 사용 중
        boolean singlequote_using = false; // 지금 따옴표 사용 중

        for (int i=0; i<length; i++) {
            int ascii = (int)(s.charAt(i));
            Log.d("Braille",Integer.toString(ascii));

            if (ascii >= ASCII_A && ascii <= ASCII_Z) {                                             // 대문자
                if (number_reading == true) data = data.concat(BrailleData_space); // 숫자가 끝남
                int index = ascii - ASCII_A;
                Log.d("Braille","index: "+Integer.toString(index));

                int next; // 다음 글자
                if (i+1<length) next = (int)(s.charAt(i+1));
                else next = -1;
                if (!capital_continue && (next<ASCII_A || next>ASCII_Z)) { // 겹대문자 아님
                    data = data.concat(BrailleData_capital);
                    capital_continue = false;
                } else if (!capital_continue) { // 겹대문자 시작, 처음 글자
                    data = data.concat(BrailleData_doublecapital);
                    capital_continue = true;
                } else if (next<ASCII_A || next>ASCII_Z) { // 겹대문자 끝
                    capital_continue = false;
                }
                data = data.concat(BrailleData_alphabet[index]);
                number_reading = false;
            }

            else if (ascii >= ASCII_a && ascii <= ASCII_z) {                                        // 소문자
                if (number_reading == true) data = data.concat(BrailleData_space); // 숫자가 끝남
                int index = ascii - ASCII_a;
                Log.d("Braille","index: "+Integer.toString(index));
                data = data.concat(BrailleData_alphabet[index]);
                number_reading = false;
                capital_continue = false;
            }

            else if (ascii >= ASCII_0 && ascii <= ASCII_9) {                                        // 숫자
                if (number_reading == false) { // 숫자가 처음 등장 시, 수표를 붙여야 함
                    number_reading = true;
                    data = data.concat(BrailleData_numbersign);
                }
                int index = ascii - ASCII_0;
                data = data.concat(BrailleData_number[index]);
                number_reading = true;
                capital_continue = false;
            }

            else if (ascii==ASCII_space) {                                                          // 스페이스
                data = data.concat(BrailleData_space);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii==ASCII_exclamation) {                                                   // 느낌표
                data = data.concat(BrailleData_exclamation);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii==ASCII_doublequote) {                                                   // 쌍따옴표
                if (doublequote_using) data = data.concat(BrailleData_closedoublequote);
                else data = data.concat(BrailleData_opendoublequote);
                doublequote_using = !doublequote_using;
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_apostrophe) {                                                  // 어포스트로피
                data = data.concat(BrailleData_apostrophe);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_openparenthesis || ascii == ASCII_closeparenthesis) {       // 괄호
                data = data.concat(BrailleData_parenthesis);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_comma) {                                                        // 쉼표
                data = data.concat(BrailleData_comma);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_hyphen) {                                                       // 하이픈
                data = data.concat(BrailleData_hyphen);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_period) {                                                       // 마침표
                data = data.concat(BrailleData_period);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_colon) {                                                        // 콜론
                data = data.concat(BrailleData_colon);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_semicolon) {                                                    // 세미콜론
                data = data.concat(BrailleData_semicolon);
                number_reading = false;
                capital_continue = false;
            }
            else if (ascii == ASCII_question) {                                                     // 물음표
                data = data.concat(BrailleData_question);
                number_reading = false;
                capital_continue = false;
            }

            else {                                                                                    // 데이터 없음 -> space로 대체
                data = data.concat(BrailleData_space);
                number_reading = false;
                capital_continue = false;
            }
        }

        Log.d("Braille", "data: "+data);

        length = data.length();
        byte temp;
        for (int i=0; i < (length/6); i++) {
            int j;
            temp = 0;
            for (j=0; j<6; j++) {
                temp <<= 1;
                if (data.charAt(i*6+j) == '1') temp++;
                Log.d("Braille", "temp: "+temp);
            }
            char chtem = (char)temp;
            Log.d("Braille", "chtem: "+chtem);
            bytedata = bytedata.concat(Character.toString(chtem));
        }
        Log.d("Braille", "bytedata: "+bytedata);
    }

    public String getString() {              // 점자 데이터를 binary string으로 변환
        return data;
    }
    public String getByteString() { return bytedata; }
}
