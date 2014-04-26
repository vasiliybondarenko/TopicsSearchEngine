package infrascructure.data.stripping;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/29/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 * implementation is based on http://tartarus.org/martin/PorterStemmer/def.txt algorithm
 */
public class EnglishSuffixStripper implements Stemmer {
    @Override
    public String getCanonicalForm(String word) {

        String canonicalWord = word;
        //1a:
        String sses = "sses";
        String ies = "ies";
        String ss = "ss";
        String s = "s";

        if(word.endsWith(sses)){
            canonicalWord = word.substring(0, word.length() - 2);
        }else{
            if(word.endsWith(ies)){
                canonicalWord = word.substring(0, word.length() - 2);
            }else{
                if(word.endsWith(ss)){
                    //do nothing
                }else{
                    if(word.endsWith(s)){
                        canonicalWord = word.substring(0, word.length() - 1);
                    }
                }
            }
        }

        //1b:
        String eed = "eed";
        String ed = "ed";
        String ing = "ing";
        boolean performNextStep = false;
        if(word.endsWith(eed)){
            if(getMeasure(word.substring(0, word.length() - 3)) > 0){
                canonicalWord = word.substring(0, word.length() - 1);
            }
        }else{
            if(word.endsWith(ed)){
                if(hasVowel(word, word.length() - 3)){
                    performNextStep = true;
                    canonicalWord = word.substring(0, word.length() - 2);
                }

            }else if(word.endsWith(ing)){
                if(hasVowel(word, word.length() - 4)){
                    performNextStep = true;
                    canonicalWord = word.substring(0, word.length() - 3);
                }

            }
        }

        if(performNextStep){
             String at = "at";
             String bl = "bl";
             String iz = "iz";
             if(canonicalWord.endsWith(at)
                     || canonicalWord.endsWith(bl)
                     || canonicalWord.endsWith(iz)){
                 canonicalWord = canonicalWord.concat("e");
             }else {
                 if(endsWithDoubleConsonant(canonicalWord)
                         && !canonicalWord.endsWith("l")
                         && !canonicalWord.endsWith("s")
                         && !canonicalWord.endsWith("z")){
                     canonicalWord = canonicalWord.substring(0, canonicalWord.length() - 1);
                 }else{
                     if(endsWithCVCExceptWXY(canonicalWord) && getMeasure(canonicalWord) == 1){
                         canonicalWord = canonicalWord.concat("e");
                     }
                 }
             }
        }

        //1c
        //(*v*) Y -> I
        if(word.endsWith("y")){
            if(hasVowel(word, word.length() - 2)){
                canonicalWord = replaceSuffix(word, "y", "i");
            }
        }

        //Step 2
        String beforeStep = canonicalWord;
        performNextStep = true;
        if(canonicalWord.equals(word)){
            canonicalWord = performStep2(canonicalWord);
            performNextStep = canonicalWord.equals(beforeStep);
        }

        //Step 3
        if(performNextStep){
            beforeStep = canonicalWord;
            canonicalWord = performStep3(canonicalWord);
            performNextStep = canonicalWord.equals(beforeStep);
        }


        //Step 4
        if(performNextStep){
            canonicalWord = performStep4(canonicalWord);
        }

        //
        if(canonicalWord.equals(word)){
            canonicalWord = performStep5(canonicalWord);
        }


        return canonicalWord;
    }

    public int getMeasure(String word){
        return getMeasure(word, word.length() - 1);
    }

    public int getMeasure(String word, int lastIndex){
        int measure = 0;
        int vcStartPos = 0;
        int consonantPos = 0;

        while(vcStartPos < lastIndex + 1){
            vcStartPos = getStartPosForNextConsonant(word, vcStartPos, false);
            if(vcStartPos != -1){
                consonantPos = getStartPosForNextConsonant(word, vcStartPos, true);
                if(consonantPos != -1){
                    measure ++;
                }else{
                    break;
                }
            }else{
                break;
            }
            vcStartPos = consonantPos;

        }

        return measure;
    }

    private String replaceSuffixes(String word, String[][] rules, int measure){
        if(word.equals("homologou")){
            int a = 0;
        }
        for(int i = 0; i < rules.length; i ++){
            String pattern = rules[i][0];
            String patternToRemove = rules[i].length == 3 ? rules[i][1] : pattern;
            String newSuffix = rules[i].length == 3 ? rules[i][2] : rules[i][1];
            if(word.endsWith(pattern)){
                int measureOfBase = getMeasure(word.substring(0, word.lastIndexOf(pattern)));
                if(measureOfBase > measure){
                    return replaceSuffix(word, pattern, patternToRemove, newSuffix);
                }
            }
        }
        return word;
    }

    private String performStep2(String w){
        String[][] rules = new String[][]{
                {"ational", "ate"},
                {"tional", "tion"},
                {"enci", "ence"},
                {"anci", "ance"},
                {"izer", "ize"},
                {"abli", "able"},
                {"alli", "al"},
                {"entli", "ent"},
                {"eli", "e"},
                {"OUSLI", "OUS"},
                {"ization", "ize"},
                {"ation", "ate"},
                {"ator", "ate"},
                {"alism", "al"},
                {"iveness", "ive"},
                {"fulness", "ful"},
                {"ousness", "ous"},
                {"aliti", "al"},
                {"iviti", "ive"},
                {"biliti", "ble"},

        };

        return replaceSuffixes(w, rules, 0);
    }

    private String performStep3(String w){
        String[][] rules = new String[][]{
                {"icate", "ic"},
                {"ative", ""},
                {"alize", "al"},
                {"iciti", "ic"},
                {"ical", "ic"},
                {"ful", ""},
                {"ness", ""}
        };
        return replaceSuffixes(w, rules, 0);
    }

    private String performStep4(String w){

        String[][] rules = new String[][]{
                {"al", ""},
                {"ance", ""},
                {"ence", ""},
                {"er", ""},
                {"ic", ""},
                {"able", ""},
                {"ible", ""},
                {"ant", ""},
                {"ement", ""},
                {"ment", ""},
                {"ent", ""},
                {"sion", "ion", ""},
                {"tion", "ion", ""},
                {"ou", ""},
                {"ism", ""},
                {"ate", ""},
                {"iti", ""},
                {"ous", ""},
                {"ive", ""},
                {"ize", ""}


        };
        return replaceSuffixes(w, rules, 1);

    }

    private String performStep5(String w){
        String result = w.endsWith("e") ? w.substring(0, w.length() - 1) : w;
        int measure = getMeasure(result);
        if(!result.equals(w)){
            if(measure > 1){
                return result;
            }
            if(measure == 1 && !endsWithCVCExceptWXY(result)){
                return result;
            }
        }
        if(measure > 1 && endsWithDoubleConsonant(w) && w.endsWith("l")){
            return w.substring(0, w.length() - 1);
        }
        return w;
    }

    private boolean endsWithDoubleConsonant(String word){
        int lastIndex = word.length() - 1;
        if(lastIndex < 1){
            return false;
        }
        if((word.charAt(lastIndex) == word.charAt(lastIndex - 1)) && isConsonant(word, lastIndex)){
            return true;
        }
        return false;
    }

    private boolean endsWithCVCExceptWXY(String word){
        int lastIndex = word.length() - 1;
        if(lastIndex < 2){
            return false;
        }
        if(!word.endsWith("w") && !word.endsWith("x") && !word.endsWith("y")
                && isConsonant(word, lastIndex)
                && !isConsonant(word, lastIndex - 1)
                && isConsonant(word, lastIndex - 2)){
            return true;
        }
        return false;
    }

    private String trimSuffix(String word, String suffix){
        if(word.endsWith(suffix)){
            return word.substring(0, word.length() - suffix.length());
        }
        return word;
    }

    private String trimSuffix(String word, String endWithPattern, String suffixToRemove){
        if(word.endsWith(endWithPattern)){
            return word.substring(0, word.length() - suffixToRemove.length());
        }
        return word;
    }

    private String replaceSuffix(String word, String suffix, String newSuffix){
        return trimSuffix(word, suffix).concat(newSuffix);
    }

    private String replaceSuffix(String word, String endWithPattern, String suffixToRemove, String newSuffix){
        return trimSuffix(word, endWithPattern, suffixToRemove).concat(newSuffix);
    }

    private boolean hasVowel(String w, int endPos){
        for(int i = 0; i <= endPos; i ++){
            if(!isConsonant(w, i)){
                return true;
            }
        }
        return false;
    }

    private int getStartPosForNextConsonant(String w, int pos, boolean consonant){
        int nextStartPos = -1;
        while(pos < w.length()){
            boolean isDesiredLetter = consonant ? isConsonant(w, pos) : !isConsonant(w, pos);
            if(isDesiredLetter){
                nextStartPos = pos;
                break;
            }
            pos ++;
        }
        return nextStartPos;
    }

    public boolean isConsonant(String word, int pos){
        word = word.toLowerCase();
        Character ch = word.charAt(pos);
        if(VOWELS.contains(ch)){
            return false;
        }
        if(ch == 'y'){
            if(pos == word.length() - 1 || isConsonant(word, pos + 1)){
                return false;
            }
        }
        return true;
    }

    private static final HashSet<Character> VOWELS = new HashSet<>();
    static {
        VOWELS.add('a');
        VOWELS.add('e');
        VOWELS.add('i');
        VOWELS.add('o');
        VOWELS.add('u');
    }
}
