package com.amdocs.petsearch.model;
import java.util.*;
public class PetInput {
	public PetClass takeInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Pet Id");
		int id = sc.nextInt();
		System.out.println("Enter pet category");
		String PetCategory = sc.next();
		System.out.println("Enter pet Type");
		String PetType = sc.next();
		System.out.println("Enter pet color");
		String PetColor = sc.next();
		System.out.println("Enter pet age");
		int PetAge = sc.nextInt();
		System.out.println("Enter pet Price");
		double PetPrice = sc.nextDouble();
		System.out.println("Enter if pet is vaccinated or not");
		boolean PetisVaccinated = sc.nextBoolean();
		System.out.println("Enter pet food habits");
		String PetFoodHabits = sc.next();
		return new PetClass(id,PetCategory,PetType,PetColor,PetAge,PetPrice,PetisVaccinated,PetFoodHabits);
	}
	public PetClass UpdateInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter pet Price");
		double PetPrice = sc.nextDouble();
		System.out.println("Enter if pet is vaccinated or not");
		boolean PetisVaccinated = sc.nextBoolean();
		return new PetClass(2,"fake","fake","fake",2,PetPrice,PetisVaccinated,"fake");
	}
}
