package com.parse.html;

import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class MAIN
{
    public static void main(String arg[]) throws IOException {
        DateSource dateSource = new DateSource("C:\\Users\\Roman\\svn_and_git\\nc-training-center\\curators\\rybak\\hack4good\\ParserNasa\\src\\main\\resources\\inputDoc\\'Bathurst Inlet' Rock on Curiosity's Sol 54, Context View4801.html");
        ParseHtmlPage parseHtmlPage = new ParseHtmlPage(dateSource);

        System.out.print(parseHtmlPage.parse());






    }
}
