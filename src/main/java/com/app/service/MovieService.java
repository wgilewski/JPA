package com.app.service;

import com.app.converter.MovieJsonConverter;
import com.app.exceptions.MyException;
import com.app.model.Movie;
import com.app.repository.impl.MovieRepositoryImpl;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MovieService
{

    private MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();
    public List<Movie> getAllMovies()
    {
        return movieRepository.findAll();
    }

    public void insertMovieAndSaveToFile(Movie movie)
    {

        if (movie.getTitle() == "" || movie.getGenre() == null || movie.getReleaseDate() == null || movie.getPrice().doubleValue() <= 0)
        {
            throw new MyException("INSERT MOVIE - MOVIE IS NULL, PLEASE FILL ALL FIELDS");
        }
        else
        {
            MovieJsonConverter movieJsonConverter = new MovieJsonConverter(movie.getTitle().replaceAll(" ","") + ".json");
            movieJsonConverter.toJson(movie);
            System.err.println("Movie has been added successfully !");
            movieRepository.saveOrUpdate(movie);
        }
    }
    public boolean checkMovie(String title)
    {
        List<Movie> movies = movieRepository.findAll();
        Optional<Movie> movie =
                movies
                        .stream()
                        .filter(m -> m.getTitle().equals(title))
                        .findFirst();
        if (movie.isPresent())
        {
            return true;
        }
        else
        {
            throw new MyException("TITLE ERROR - THERE IS NO MOVIE WITH THAT TITLE IN DATABASE");

        }
    }
    public Movie getMovie(String title)
    {

        List<Movie> movies = movieRepository.findAll();

        Optional<Movie> movie =
                movies
                        .stream()
                        .filter(m -> m.getTitle().equals(title))
                        .findFirst();

        if (movie.isPresent())
        {
            return movie.get();
        }
        else
        {
            return null;
        }

    }
    public void deleteMovie(Movie movie)
    {
        if (movie == null)
        {
            throw new MyException("INSERT MOVIE - MOVIE IS NULL");
        }
        else
        {
            File file = new File("C://Users//wojci//Desktop//JPA//" + movie.getTitle().replaceAll(" ","") + ".json");
            file.delete();
            System.err.println("Movie file has been deleted successfully !");
            movieRepository.delete(movie.getId());
        }
    }
    public void updateMovie(Movie movie, String oldTitle)
    {

        if (movie.getTitle() == "" || movie.getGenre() == null || movie.getReleaseDate() == null || movie.getPrice().doubleValue() <= 0)
        {
            throw new MyException("UPDATE MOVIE - MOVIE IS NULL, PLEASE FILL ALL FIELDS");
        }
        else
        {
            File file = new File("C://Users//wojci//Desktop//JPA//" + oldTitle.replaceAll(" ","") + ".json");
            file.delete();
            System.err.println("Movie file has been deleted successfully !");
            insertMovieAndSaveToFile(movie);
        }
    }

}
