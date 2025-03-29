package com.example.pracainzynierska;

import java.io.Serializable;

public class SimpleKanjiObject implements Serializable {
    String kanji = "", on_yomi = "", kun_yomi = "";

    public SimpleKanjiObject(String kanji, String on_yomi, String kun_yomi) {
        this.kanji = kanji;
        this.on_yomi = on_yomi;
        this.kun_yomi = kun_yomi;
    }

    public String getKanji() {
        return kanji;
    }

    public String getOn_yomi() {
        return on_yomi;
    }

    public String getKun_yomi() {
        return kun_yomi;
    }
}
