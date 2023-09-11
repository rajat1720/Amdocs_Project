package com.amdocs.petsearch.doa;

import java.sql.*;
import java.util.*;
import java.util.Scanner;

import com.amdocs.petsearch.model.PetClass;
import com.amdocs.petsearch.exception.*;


public class DatabaseManager {
    private static final String jdbcUrl = "Jdbc:Oracle:thin:@localhost:1521:orcl";
    private static final String username = "scott";
    private static final String password = "tiger";
    
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
    
    public void addPetDetails(PetClass pet) throws SQLException {
        String sql = "INSERT INTO pet (petid, petcategory, pettype,color,age,price,isVaccinated,foodhabits) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseManager.getConnection()){
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pet.getPetId());
            preparedStatement.setString(2, pet.getPetCategory().trim().toLowerCase());
            preparedStatement.setString(3, pet.getPetType().trim().toLowerCase());
            preparedStatement.setString(4, pet.getColor().trim().toLowerCase());
            preparedStatement.setInt(5, pet.getAge());
            preparedStatement.setDouble(6, pet.getPrice());
            if(pet.isVaccinated()) {
            	preparedStatement.setString(7, "y");
            }
            else {
            	preparedStatement.setString(7, "n");
            }
            preparedStatement.setString(8, pet.getFoodHabits().trim().toLowerCase());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows inserted successfully.");
        }
        }
    }
    
    public void updatePetPriceAndVaccinationStatus(int petid, double price, boolean isVaccinated) throws SQLException, PetException {
    	String sql = "Update pet set price = ?, isVaccinated = ? where petid = ?";
    	try (Connection connection = DatabaseManager.getConnection()) {
    	try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
    		PetClass pet = new PetClass(petid, "fake", "fake", "fake", 2, price, isVaccinated, "fake");
            preparedStatement.setDouble(1, pet.getPrice());
            if(isVaccinated) {
            	preparedStatement.setString(2, "y");
            }
            else {
            	preparedStatement.setString(2, "n");
            }
            preparedStatement.setInt(3,petid);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " rows updated successfully.");
            } else {
            	throw new PetException("No rows were updated. Pet not found.");
//                System.out.println("No rows were updated. Pet not found.");
            }
    	}
    	}
    }
    
    public void deletePetDetails(int id) throws SQLException, PetException {
    	String sql = "delete from pet where petid = ?";
    	try (Connection connection = DatabaseManager.getConnection()){
    	try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println(rowsDeleted + " rows updated successfully.");
            } else {
            	throw new PetException("No rows were updated. Pet not found.");
//                System.out.println("No rows were updated. Pet not found.");
            }
    	}
    	}
    }
    
    public int countPetsByCategory(String petCategory) throws SQLException, PetException {
        
        String sql = "SELECT petcategory, COUNT(*) AS count FROM pet WHERE petcategory = ? GROUP BY petcategory";
        try (Connection connection = DatabaseManager.getConnection()){
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, petCategory);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count;
//                    System.out.println("Count of pets in category '" + petCategory + "': " + count);
                } else {
                	throw new PetException("No pets found in category '" + petCategory + "'.");
//                    System.out.println("No pets found in category '" + petCategory + "'.");
                }
            }
        }
        }
    }
    
    public List<PetClass> showAllPets() throws SQLException {
    	List<PetClass> pets = new ArrayList<>();
    	String sql = "Select * from pet";
    	try (Connection connection = DatabaseManager.getConnection()){
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("List of All Pets:");
                while (resultSet.next()) {
                    int id = resultSet.getInt("Petid"); // Replace with your column names
                    String PetCategory = resultSet.getString("PetCategory");
                    String PetType = resultSet.getString("PetType");
                    String PetColor = resultSet.getString("color");
                    int age = resultSet.getInt("age");
                    double price = resultSet.getInt("price");
                    String PetVaccinated = resultSet.getString("isVaccinated");
                    boolean PetisVaccinated;
                    if(PetVaccinated == "y") {
                    	PetisVaccinated = true;
                    }
                    else {
                    	PetisVaccinated = false;
                    }
                    String foodHabits = resultSet.getString("foodHabits");
                    // Add other columns as needed

//                    System.out.println("Pet ID: " + id + ", Pet Category: " + PetCategory + ", Pet Type: " + PetType + ", Pet Color: " + PetColor + ", Age " + age+ ", Price " + price + ", Is Pet Vaccinated: " + PetisVaccinated + ", foodHabits: " + foodHabits);
                    // Print other pet details here
                    PetClass pet = new PetClass(id,PetCategory,PetType,PetColor,age,price,PetisVaccinated,foodHabits);
                    pets.add(pet);
                }
            }
//            System.out.println(pets.toString());
            return pets;
       }
    	}
    }
    
   public Map<String,Integer> countByVaccinationStatusForPetType(String petType) throws SQLException {
	   String targetVaccinationStatus = "y";
//	   List<PetClass> pets = new ArrayList<>();
	   Map<String, Integer> countsByStatus = new HashMap<>();
	   try (Connection connection = DatabaseManager.getConnection()){
//	   String sql = "SELECT petId, petCategory,pettype,color,age,price, isVaccinated,foodhabits FROM pet WHERE isVaccinated = ? and pettype = ?";
	   String sql = "SELECT isVaccinated, COUNT(*) AS count FROM pet WHERE pettype = ? GROUP BY isVaccinated";
       try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//           preparedStatement.setString(1, targetVaccinationStatus);
           preparedStatement.setString(1, petType);

           try (ResultSet resultSet = preparedStatement.executeQuery()) {
               System.out.println("Pets with Vaccination Status: " + targetVaccinationStatus);
               while (resultSet.next()) {
//                   String PetType = resultSet.getString("PetType");
                   String PetVaccinated = resultSet.getString("isVaccinated");
                   int count = resultSet.getInt("count");
                   countsByStatus.put(PetVaccinated, count);
//                   PetClass pet = new PetClass(id,PetCategory,PetType,PetColor,age,price,PetisVaccinated,foodHabits);
////                   System.out.println("Pet ID " + petId + ", Pet Category: " + petCategory + ",isVaccinated " + isVaccinated);
//                   pets.add(pet);
               }
//           }
       }
       }
}
//       System.out.print(pets.toString());
       return countsByStatus;
   }
    
    public List<PetClass> searchByPrice(double min, double max) throws SQLException {
    	Scanner sc = new Scanner(System.in);
    	List<PetClass> pets = new ArrayList<>();
    	String sql = "SELECT PetId, PetCategory, Pettype,color,age, price,isVaccinated,foodHabits FROM pet WHERE price BETWEEN ? AND ?";
    	try (Connection connection = DatabaseManager.getConnection()){
    	try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, min);
            preparedStatement.setDouble(2, max);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.println("Pets within Price Range: Rs" + min + " - Rs" + max);
                while (resultSet.next()) {
                	int id = resultSet.getInt("Petid"); // Replace with your column names
                    String PetCategory = resultSet.getString("PetCategory");
                    String PetType = resultSet.getString("PetType");
                    String PetColor = resultSet.getString("color");
                    int age = resultSet.getInt("age");
                    double price = resultSet.getInt("price");
                    String PetVaccinated = resultSet.getString("isVaccinated");
                    boolean PetisVaccinated;
                    if(PetVaccinated == "y") {
                    	PetisVaccinated = true;
                    }
                    else {
                    	PetisVaccinated = false;
                    }
                    String foodHabits = resultSet.getString("foodHabits");
                    PetClass pet = new PetClass(id,PetCategory,PetType,PetColor,age,price,PetisVaccinated,foodHabits);
                    pets.add(pet);
//                    System.out.println("Pet Id: " + PetId + ", Pet Category: " + PetCategory + ", Pet type: " + Pettype + ", Price:" + price);
                }
            }
        }
    	}
//    	System.out.print(pets.toString());
    	return pets;
    }
}