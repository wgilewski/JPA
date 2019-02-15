package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.*;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public class MenuService
{

    private static CinemaService cinemaService = new CinemaService();
    private static MovieService movieService = new MovieService();
    private EmailService emailService = new EmailService();
    private static CustomerService customerService = new CustomerService();

    public void mainMenu()
    {
        boolean a = true;
        while (a)
        {
            try {

                System.out.println("\n\n<----Menu---->");
                System.out.println("1. Insert new customer");
                System.out.println("2. Insert new movie");
                System.out.println("3. Customer management");
                System.out.println("4. Movie management");
                System.out.println("5. Buy ticket");
                System.out.println("6. Customer transactions");
                System.out.println("7. Close");
                int menu = UserDataService.getInt("\nPlease input menu number : ");

                switch (menu)
                {
                    case 1:
                        newCustomer();
                        break;


                    case 2:
                        newMovie();
                        break;


                    case 3:
                        customerManagement();
                        break;


                    case 4:
                        movieManagement();
                        break;


                    case 5:
                        ticketMenu();
                        break;


                    case 6:
                        customerTransactions();
                        break;

                    case 7:
                        UserDataService.close();
                        a = false;
                }

            }catch (MyException e)
            {
                System.err.println(e.getExceptionInfo().getExceptionMessage());
            }
        }
    }

    public void customerManagement()
    {
        String email1;
        do {
            email1 = UserDataService.getString("\nPlease input customer's email : ");
        } while (!email1.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                || customerService.checkEmail(email1) != true);
        Customer customer = customerService.getCustomer(email1);
        int customerMenu;
        do{
            System.out.println("\n<----Customer Menu---->");
            System.out.println("1. Delete Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Back To Main Menu");
            customerMenu = UserDataService.getInt("\nPlease input menu number : ");

            switch (customerMenu) {
                case 1:
                    customerService.deleteCustomer(customer);
                    customerMenu = 3;
                    break;

                case 2:
                    updateCustomer(customer);
                    customerMenu = 3;
                    break;


                case 3:
                    customerMenu = 3;
                    break;
            }
        }while (customerMenu != 3);
    }
    public void movieManagement()
    {
        String title;
        do {
            title = UserDataService.getString("\nPlease input movie title : ").toUpperCase();
        } while (movieService.checkMovie(title) == false);

        Movie movie = movieService.getMovie(title);

        int movieMenu;
        do {
            System.out.println("\n<----Movie Menu---->");
            System.out.println("1. Delete Movie");
            System.out.println("2. Update Movie");
            System.out.println("3. Back To Main Menu");
            movieMenu = UserDataService.getInt("\nPlease input menu number : ");
            switch (movieMenu)
            {
                case 1:
                    movieService.deleteMovie(movie);
                    movieMenu = 3;
                    break;

                case 2:
                    updateMovie(movie);
                    movieMenu = 3;
                    break;

                case 3:
                    movieMenu = 3;
                    break;
            }
        } while (movieMenu != 3);
    }
    public void ticketMenu()
    {
        int account;
        String email2;

        do {
            System.out.println("Do you have account ?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            account = UserDataService.getInt("Please input menu number : ");
        }while (!Arrays.asList(1,2).contains(account));

        if (account == 2)
        {
            newCustomer();
        }

        do{
            email2 = UserDataService.getString("\nPlease input email : ");
        }while (!email2.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                || customerService.checkEmail(email2) != true);

        Customer customer1 = customerService.getCustomer(email2);
        Movie movie1 = selectMovie();
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), selectSeanceTime());


        if (customer1.getLoyaltyCard() == null)
        {
            SalesStand salesStand = SalesStand
                    .builder()
                    .customer(customer1)
                    .movie(movie1)
                    .seanceDateTime(localDateTime)
                    .build();
            cinemaService.insertSalesStand(salesStand);
            try {
                emailService.send(email2,"Message from cinema",salesStand.getMovie().toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
        else
        {
            int counter = customer1.getLoyaltyCard().getMoviesWithDiscount();
            if (counter == 0)
            {

            }
            else
            {
                customer1.getLoyaltyCard().setMoviesWithDiscount(customer1.getLoyaltyCard().getMoviesWithDiscount() - 1);
                movie1.setPrice(movie1.getPrice().multiply(customer1.getLoyaltyCard().getDiscount()));
                SalesStand salesStand = SalesStand
                        .builder()
                        .customer(customer1)
                        .movie(movie1)
                        .seanceDateTime(localDateTime)
                        .build();
                cinemaService.insertSalesStand(salesStand);
                try {
                    emailService.send(email2,"Message from cinema",salesStand.getMovie().toString());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        }
        if (loyaltyCardChecker(customer1) == true)
        {
            int choice;
            do {
                System.out.println("\nDo you want to create loyalty card ?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                choice = UserDataService.getInt("\nPlease input menu number : ");
            }while (!Arrays.asList(1,2).contains(choice));
            if (choice == 1)
            {
                LoyaltyCard loyaltyCard = LoyaltyCard
                        .builder()
                        .customer(customer1)
                        .discount(new BigDecimal(0.1))
                        .expirationDate(LocalDate.now().plusYears(1))
                        .moviesWithDiscount(15)
                        .build();
                customer1.setLoyaltyCard(loyaltyCard);
                customerService.updateCustomer(customer1);
            }
        }
    }
    public void newMovie()
    {


        String title = "";
        Genre genre = null;
        double price = 0;
        int duration = 0;
        LocalDate releaseDate = null;
        int menu;
        while (true)
        {
            try {

                System.out.println("<----Movie Update Menu---->");
                System.out.println("1. Movie Title");
                System.out.println("2. Movie Genre");
                System.out.println("3. Movie Price");
                System.out.println("4. Movie Duration");
                System.out.println("5. Movie Release Date");
                System.out.println("6. Show Movie");
                System.out.println("7. Save Movie");
                System.out.println("8. Back To Movie Management Menu");
                menu = UserDataService.getInt("\nPlease input menu number : ");
                switch (menu) {
                    case 1:
                        do {
                            title = UserDataService.getString("Please input movie title : ").toUpperCase();
                        } while (!title.matches("[A-Z\\d ]+"));
                        break;


                    case 2:
                        Genre[] genres = Genre.values();
                        int genreMenu;
                        do {
                            System.out.println("<----Movie Genre Menu---->");
                            System.out.println("1. PRZYGODOWY");
                            System.out.println("2. SCIFI");
                            System.out.println("3. DRAMAT");
                            System.out.println("4. WOJENNY");
                            System.out.println("5. KOMEDIA");
                            genreMenu = UserDataService.getInt("\nPlease enter genre number : ");
                        } while (!Arrays.asList(1, 2, 3, 4, 5).contains(genreMenu));
                        genre = genres[genreMenu - 1];
                        break;


                    case 3:
                        do {
                            price = UserDataService.getDouble("Please input movie price : ");
                        } while (price < 0);
                        break;


                    case 4:
                        do {
                            duration = UserDataService.getInt("Please input movie duration : ");
                        } while (duration < 0);
                        break;


                    case 5:
                        int year = 0;
                        int month = 0;
                        int day = 0;
                        do {
                            year = UserDataService.getInt("Please input year of release date : ");
                        } while (year > LocalDate.now().plusYears(1).getYear() || year < 1896);

                        do {
                            month = UserDataService.getInt("Please input month of release date : ");
                        } while (month <= 1 || month > 12);

                        do {
                            day = UserDataService.getInt("Please input day of release date : ");
                        } while (day <= 1 || day > 31);
                        releaseDate = LocalDate.of(year, month, day);
                        break;


                    case 6:
                        System.out.println("Movie : ");
                        System.out.println("\nTitle : \"" + title + "\"\nGenre : " + genre + "\nPrice : " + price + "\nDuration : " + duration + "\nRelease date : " + releaseDate);
                        break;


                    case 7:

                        movieService.insertMovieAndSaveToFile(Movie
                                .builder()
                                .title(title)
                                .genre(genre)
                                .releaseDate(releaseDate)
                                .duration(duration)
                                .price(new BigDecimal(price))
                                .build());
                        return;


                    case 8:
                        return;
                }

            } catch (MyException e)
            {
                System.err.println(e.getExceptionInfo().getExceptionMessage());
            }
        }
    }
    public void updateMovie(Movie movie)
    {
        int menu;
        String oldTitle = movie.getTitle();

        while (true)

            try {


                System.out.println("\n<----Movie Update Menu---->");
                System.out.println("1. Movie Title");
                System.out.println("2. Movie Genre");
                System.out.println("3. Movie Price");
                System.out.println("4. Movie Duration");
                System.out.println("5. Movie Release Date");
                System.out.println("6. Show Movie");
                System.out.println("7. Save Changes");
                System.out.println("8. Back To Movie Management Menu");
                menu = UserDataService.getInt("\nPlease input menu number : ");
                switch (menu) {
                    case 1:
                        do {
                            movie.setTitle(UserDataService.getString("\nPlease input movie title : ").toUpperCase());
                        } while (!movie.getTitle().matches("[A-Z\\d ]+"));
                        break;


                    case 2:
                        Genre[] genres = Genre.values();
                        int genreMenu;
                        do {
                            System.out.println("1. PRZYGODOWY");
                            System.out.println("2. SCIFI");
                            System.out.println("3. DRAMAT");
                            System.out.println("4. WOJENNY");
                            System.out.println("5. KOMEDIA");
                            genreMenu = UserDataService.getInt("\nPlease enter genre number : ");
                        } while (!Arrays.asList(1, 2, 3, 4, 5).contains(genreMenu));
                        movie.setGenre(genres[genreMenu - 1]);
                        break;


                    case 3:
                        do {
                            movie.setPrice(new BigDecimal(UserDataService.getDouble("\nPlease input movie price : ")));
                        } while (movie.getPrice().doubleValue() < 0);
                        break;


                    case 4:
                        do {
                            movie.setDuration(UserDataService.getInt("\nPlease input movie duration : "));
                        } while (movie.getDuration() < 0);
                        break;


                    case 5:
                        int year = 0;
                        int month = 0;
                        int day = 0;
                        do {
                            year = UserDataService.getInt("Please input year of release date : ");
                        } while (year > LocalDate.now().plusYears(1).getYear() || year < 1896);

                        do {
                            month = UserDataService.getInt("Please input month of release date : ");
                        } while (month < 1 || month > 12);

                        do {
                            day = UserDataService.getInt("Please input day of release date : ");
                        } while (day < 1 || day > 31);
                        movie.setReleaseDate(LocalDate.of(year, month, day));
                        break;


                    case 6:
                        System.out.println("Movie : ");
                        System.out.println("\nTitle : " + movie.getTitle() + "\nGenre : " + movie.getGenre() + "\nPrice : " + movie.getPrice() + "\nDuration : " + movie.getDuration() + "\nRelease date : " + movie.getReleaseDate());
                        break;


                    case 7:
                        movieService.updateMovie(movie, oldTitle);
                        return;


                    case 8:
                        return;
                }
            }catch (MyException e)
            {
                System.out.println(e.getExceptionInfo().getExceptionMessage());
            }

    }
    public void newCustomer()
    {
        String name = "";
        String surname = "";
        int age = 0;
        String email = "";
        int menu;
        while (true)
        {
            try {
                System.out.println("\n<----Customer Menu---->");
                System.out.println("1. Customer Name");
                System.out.println("2. Customer Surname");
                System.out.println("3. Customer Age");
                System.out.println("4. Customer Email");
                System.out.println("5. Show Customer");
                System.out.println("6. Save Customer");
                System.out.println("7. Back To Main Menu");
                menu = UserDataService.getInt("\nPlease enter menu number : ");

                switch (menu) {
                    case 1:
                        do {
                            name = UserDataService.getString("\nPlease enter customer name : ").toUpperCase();
                        } while (!name.matches("[A-Z]+"));
                        break;

                    case 2:
                        do {
                            surname = UserDataService.getString("\nPlease enter customer surname : ").toUpperCase();
                        } while (!surname.matches("[A-Z]+"));
                        break;

                    case 3:
                        do {
                            age = UserDataService.getInt("\nPlease enter customer age : ");
                        } while (age < 0);
                        break;

                    case 4:
                        do {
                            email = UserDataService.getString("\nPlease enter customer email : ");
                        }
                        while (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                                && customerService.checkEmail(email) == false);
                        break;

                    case 5:
                        System.out.println("\nCustomer : ");
                        System.out.println("\nName : " + name + "\nSurname : " + surname + "\nAge : " + age + "\nEmail : " + email);
                        break;

                    case 6:
                            customerService.insertCustomer(Customer
                                    .builder()
                                    .name(name.toUpperCase())
                                    .surname(surname.toUpperCase())
                                    .email(email)
                                    .age(age)
                                    .loyaltyCard(null)
                                    .build());
                       return;


                    case 7:
                        return;
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getExceptionMessage());
            }

        }
    }
    public void updateCustomer(Customer customer)
    {
        int menu;
        while (true)
        {
            try {

                System.out.println("\n<----Update Customer Menu---->");
                System.out.println("1. Customer Name");
                System.out.println("2. Customer Surname");
                System.out.println("3. Customer Age");
                System.out.println("4. Customer Email");
                System.out.println("5. Show Customer");
                System.out.println("6. Save Changes");
                System.out.println("7. Back To Customer Management Menu");

                menu = UserDataService.getInt("\nPlease enter menu number : ");
                switch (menu) {
                    case 1:
                        String name;
                        do {
                            name = UserDataService.getString("\nPlease enter customer name : ").toUpperCase();
                        } while (!name.matches("[A-Z]+"));
                        customer.setName(name.toUpperCase());
                        break;


                    case 2:
                        String surname;
                        do {
                            surname = UserDataService.getString("\nPlease enter customer surname : ").toUpperCase();
                        } while (!surname.matches("[A-Z]+"));
                        customer.setSurname(surname.toUpperCase());
                        break;


                    case 3:
                        int age;
                        do {
                            age = UserDataService.getInt("\nPlease enter customer age : ");
                        } while (age < 0);
                        customer.setAge(age);
                        break;


                    case 4:
                        String email;
                        do {
                            email = UserDataService.getString("\nPlease enter customer email : ");
                        }
                        while (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"));
                        customer.setEmail(email);
                        break;


                    case 5:
                        System.out.println("\nCustomer : ");
                        System.out.println("\nName : " + customer.getName() + "\nSurname : " + customer.getSurname() + "\nAge : " + customer.getAge() + "\nEmail : " + customer.getEmail());
                        break;


                    case 6:
                        customerService.updateCustomer(customer);
                        return;


                    case 7:
                        return;

                }
            }catch (MyException e)
            {
                System.err.println(e.getExceptionInfo().getExceptionMessage());
            }
        }


    }


    public void customerTransactions()
    {
        String email3;
        do{
            email3 = UserDataService.getString("\nPlease input email : ");
        }while (!email3.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                || customerService.checkEmail(email3) != true);
        Map<LocalDateTime,Movie> movieMap = cinemaService.getAllCustomersTransactions(customerService.getCustomer(email3));
        System.out.println("\n\nMovies watched by customer : ");
        movieMap.entrySet().stream().forEach(System.out::println);
    }
    public boolean loyaltyCardChecker(Customer customer)
    {
        List<SalesStand> salesStands = cinemaService.getSalesStandList();

        long counter = salesStands
                .stream()
                .filter(salesStand -> salesStand.getCustomer().getId().equals(customer.getId()))
                .count();

        if (counter >= 3 && customer.getLoyaltyCard() == null)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public LocalTime selectSeanceTime()
    {
        LocalTime localTime = LocalTime.now();

        int minutes = 0;
        int hours = localTime.getHour();

        if (localTime.getMinute() >= 30) {
            hours = localTime.getHour() + 1;
        } else {
            minutes = 30;
        }

        LocalTime lt = LocalTime.of(hours, minutes);
        int counter = 1;

        Map<Integer, LocalTime> localTimeMap = new HashMap<>();
        localTimeMap.put(counter, lt);
        do {
            counter++;
            lt = lt.plusMinutes(30);
            localTimeMap.put(counter, lt);
        } while (!lt.equals(LocalTime.of(22, 30)));

        int number;

        do {
            System.out.println("\n<----Seance time---->");
            localTimeMap.entrySet().stream().forEach(System.out::println);
            number = UserDataService.getInt("\nPlease input time number : ");
        }while (!localTimeMap.containsKey(number));

        return localTimeMap.get(number);

    }
    public Movie selectMovie()
    {
        List<Movie> movies = movieService.getAllMovies();
        Map<Integer,Movie> movieMap = new HashMap<>();
        int i = 1;
        for (Movie m : movies)
        {
            movieMap.put(i++,m);
        }
        int number;
        do {
            System.out.println("\n<----Movies---->");
            movieMap.entrySet().stream().forEach(System.out::println);
            number = UserDataService.getInt("\nPlease input movie number : ");
        }while (!movieMap.containsKey(number));

        return movieMap.get(number);
    }


}
