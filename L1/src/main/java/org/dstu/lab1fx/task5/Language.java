package org.dstu.lab1fx.task5;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Language {
    private StringProperty language;
    private StringProperty author;
    private StringProperty age;
    public Language(String language, String author, String age) {
        this.language = new SimpleStringProperty(language);
        this.author = new SimpleStringProperty(author);
        this.age = new SimpleStringProperty(age);
    }

    public String getLanguage() {
        return language.get();
    }

    public StringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getAge() {
        return age.get();
    }

    public StringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }
}
