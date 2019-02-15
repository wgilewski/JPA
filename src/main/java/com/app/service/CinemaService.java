package com.app.service;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;
import com.app.repository.impl.SalesStandRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CinemaService
{
    private SalesStandRepositoryImpl salesStandRepository = new SalesStandRepositoryImpl();


    public List<SalesStand> getSalesStandList()
    {
        return salesStandRepository.findAll();
    }
    public void insertSalesStand(SalesStand salesStand)
    {
        if (salesStand !=null)
        {
            salesStandRepository.saveOrUpdate(salesStand);
            System.err.println("Sales stand has been added successfully !");
        }
        else
        {
            System.err.println("Adding sales stand error !");
        }
    }
    public Map<LocalDateTime,Movie> getAllCustomersTransactions(Customer customer)
    {
        long counter = salesStandRepository.findAll()
                .stream()
                .filter(salesStand -> salesStand.getCustomer().equals(customer))
                .count();

        if (counter == 0)
        {
            return null;
        }
        else
            return salesStandRepository.findAll()
                .stream()
                .filter(c -> c.getCustomer().equals(customer))
                .collect(Collectors.toMap(SalesStand::getSeanceDateTime, SalesStand::getMovie))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }


}
