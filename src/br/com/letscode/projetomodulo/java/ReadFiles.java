package br.com.letscode.projetomodulo.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFiles {

    private final List<PersonItem> personItemListMale;
    private final List<PersonItem> personItemListFemale;

    public ReadFiles(String filename, String filename2) {
        this.personItemListMale = oscarMale(filename);
        this.personItemListFemale = oscarFemale(filename2);

        youngActorWinner(); // 1ª Questão
        femaleMoreWinner(); // 2ª Questão
        ageGroupFemale(); // 3ª Questão
        moreOneOscar(); // 4ª Questão
        printFive();
        searchActor(); // 5ª Questão
    }

    private List<PersonItem> oscarMale(String filename) {
        try (Stream<String> fileLines = Files.lines(Paths.get(filename))) {
            return fileLines
                    .skip(1)
                    .map(PersonItem::fromLine)
                    .sorted(Comparator.comparingInt(PersonItem::getAge))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("\nArquivo " + filename + " não encontrado dentro da pasta raiz!");
            return Collections.emptyList();
        }
    }

    public List<PersonItem> oscarFemale(String filename2) {
        try (Stream<String> fileLines = Files.lines(Paths.get(filename2))) {
            return fileLines
                    .skip(1)
                    .map(PersonItem::fromLine)
                    .sorted(Comparator.comparingInt(PersonItem::getAge))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Arquivo " + filename2 + " não encontrado dentro da pasta raiz!");
            return Collections.emptyList();
        }
    }

    private void youngActorWinner() {
        System.out.println("\n" + "1- Quem foi o ator mais jovem a ganhar um Oscar?");
        getPersonItemListMale().stream()
                .min(Comparator.comparingInt(PersonItem::getAge))
                .ifPresent(youngWinner -> System.out.println("R: O ator mais jovem a ganhar o oscar foi "
                        + youngWinner.getName() + " com "
                        + youngWinner.getAge() + " anos."));
    }

    private void femaleMoreWinner() {
        System.out.println("\n" + "2- Quem foi a atriz que mais vezes foi premiada?");
            Map<String, Long> repeatPrize = getPersonItemListFemale().stream()
                    .map(PersonItem::getName)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            repeatPrize.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(femaleWinner -> System.out.println("R: A atriz que mais foi premiada é "
                            + femaleWinner.getKey() + " com " + femaleWinner.getValue() + " premiações."));
    }
        
    private void ageGroupFemale() {
        System.out.println("\n" + "3- Quais as atrizes entre 20 e 30 anos que mais vezes foi vencedora?");
            Map<String, Long> actress = this.personItemListFemale.stream()
                    .filter(actressAge -> actressAge.getAge() >= 20 && actressAge.getAge() <= 30)
                    .map(PersonItem::getName)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Long oscar = actress.entrySet().stream()
                    .max(Comparator.comparingLong(Map.Entry::getValue)).get().getValue();

            actress.entrySet().stream()
                    .filter(prize -> prize.getValue().equals(oscar))
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(prize -> System.out.println(prize.getKey()
                            + " com " + prize.getValue()
                            + " premiações."));
    }

    private void moreOneOscar() {
        System.out.println("\n" + "4- Quais atores ou atrizes receberam mais de um Oscar?");
        System.out.println("Atores com mais de um Oscar: ");

        Map<String, Long> actors = this.personItemListMale.stream()
                .map(PersonItem::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        actors.entrySet().stream()
                .filter(prize -> prize.getValue() >= 2)
                .sorted(Map.Entry.comparingByKey())
                .forEach(prize -> System.out.println(prize.getKey()
                        + " com " + prize.getValue()
                        + " premiações."));

        System.out.println("\nAtrizes com mais de um Oscar: ");
        Map<String, Long> actress = this.personItemListFemale.stream()
                .map(PersonItem::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        actress.entrySet().stream()
                .filter(prize -> prize.getValue() >= 2)
                .sorted(Map.Entry.comparingByKey())
                .forEach(prize -> System.out.println(prize.getKey()
                        + " com " + prize.getValue()
                        + " premiações."));
    }

    private void printFive() {
        System.out.println("""
                \n5- Quando informado o nome de um ator ou atriz, dê um resumo de quantos prêmios ele/ela recebeu e liste ano,
                   idade e nome de cada filme pelo qual foi premiado(a).""" + "\n");
    }

    private void searchActor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do ator ou atriz ou 0 para sair: ");
        String nameActor = scanner.nextLine();

        this.personItemListFemale.stream()
                .filter(name -> name.getName().contains(nameActor))
                .forEach(pesquisa -> System.out.println(
                        "Detalhes das premiações:"
                                + "\nName: " + pesquisa.getName()
                                + "\nAge: " + pesquisa.getAge()
                                + "\nYear: " + pesquisa.getYear()
                                + "\nMovie: " + pesquisa.getMovie()
                                + "\n"));

        if (this.personItemListFemale.contains(nameActor) && !this.personItemListMale.contains(nameActor)) {
            System.out.println("Essas informações foram encontradas!\n");
            searchActor();
        }

        this.personItemListMale.stream()
                .filter(name -> name.getName().contains(nameActor))
                .forEach(pesquisa -> System.out.println(
                        "Detalhes das premiações:"
                        + "\nName: " + pesquisa.getName()
                        + "\nAge: " + pesquisa.getAge()
                        + "\nYear: " + pesquisa.getYear()
                        + "\nMovie: " + pesquisa.getMovie()
                        + "\n"));

        if(nameActor.equals("0")) {
            System.exit(0);
        }

        if (!this.personItemListMale.contains(nameActor) && !this.personItemListFemale.contains(nameActor)) {
            System.out.println("Se não apareceram informações, digite o nome do ator/atriz corretamente!\n");
            searchActor();
        }

    }

    private List<PersonItem> getPersonItemListMale() {
        return personItemListMale;
    }

    private List<PersonItem> getPersonItemListFemale() {
        return personItemListFemale;
    }

}

