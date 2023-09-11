package com.amdocs.petsearch.doa;
import java.sql.*;
import com.amdocs.petsearch.model.*;

public class PetDOA {
    public void insertPetData(Connection connection, PetClass pet) throws SQLException {
        String sql = "INSERT INTO pet (petid, petcategory, pettype,color,age,price,isVacinated,foodhabits) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pet.getPetId());
            preparedStatement.setString(2, pet.getPetCategory());
            preparedStatement.setString(3, pet.getPetType());
            preparedStatement.setString(4, pet.getColor());
            preparedStatement.setInt(5, pet.getAge());
            preparedStatement.setDouble(6, pet.getPrice());
            preparedStatement.setBoolean(7, pet.isVaccinated());
            preparedStatement.setString(8, pet.getFoodHabits());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows inserted successfully.");
        }
    }
}
