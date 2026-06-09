package com.gamezone.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamezone.model.Sale;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONSaleRepository {
    private final File file = new File("ventas.json");
    private final ObjectMapper mapper;

    public JSONSaleRepository() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private List<Sale> readAll() throws IOException {
        if (!file.exists()) return new ArrayList<>();
        return mapper.readValue(file, new TypeReference<List<Sale>>() {});
    }

    private void writeAll(List<Sale> sales) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, sales);
    }

    public void saveSale(Sale sale) throws IOException {
        List<Sale> sales = readAll();
        sales.add(sale);
        writeAll(sales);
    }

    public List<Sale> getAllSales() {
        try { return readAll(); } catch (IOException e) { return new ArrayList<>(); }
    }
}
