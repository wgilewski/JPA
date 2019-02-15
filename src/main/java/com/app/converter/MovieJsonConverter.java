package com.app.converter;

import com.app.model.Movie;

public class MovieJsonConverter extends JsonConverter<Movie>
{

    public MovieJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
