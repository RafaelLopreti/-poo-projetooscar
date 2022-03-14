package br.com.letscode.projetomodulo.java;

import static java.lang.Integer.parseInt;

public record PersonItem(String year, Integer age, String name, String movie) {

    public static PersonItem fromLine(String line) {
        String[] fields = line.split("; ");
        return new PersonItem(
                fields[1],
                parseInt(fields[2]),
                fields[3],
                fields[4]
        );
    }

    public String getYear() {
        return year;
    }

    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "" +
                "Year: " + year +
                ", Age: " + age +
                ", Name: " + name +
                ", Movie: " + movie +
                "";
    }
}
