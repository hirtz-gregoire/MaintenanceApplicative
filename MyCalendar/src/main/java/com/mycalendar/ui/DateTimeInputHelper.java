package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Scanner;

public class DateTimeInputHelper {
    private final Scanner scanner;
    
    public DateTimeInputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public LocalDateTime inputDateTime() {
        System.out.print("Année (AAAA) : ");
        int annee = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Mois (1-12) : ");
        int mois = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Jour (1-31) : ");
        int jour = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Heure début (0-23) : ");
        int heure = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Minute début (0-59) : ");
        int minute = Integer.parseInt(scanner.nextLine());
        
        return LocalDateTime.of(annee, mois, jour, heure, minute);
    }
    
    public int inputDuration() {
        System.out.print("Durée (en minutes) : ");
        return Integer.parseInt(scanner.nextLine());
    }
    
    public int inputFrequency() {
        System.out.print("Fréquence (en jours) : ");
        return Integer.parseInt(scanner.nextLine());
    }
    
    public LocalDateTime[] inputMonthPeriod() {
        System.out.print("Entrez l'année (AAAA) : ");
        int annee = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Entrez le mois (1-12) : ");
        int mois = Integer.parseInt(scanner.nextLine());
        
        LocalDateTime debut = LocalDateTime.of(annee, mois, 1, 0, 0);
        LocalDateTime fin = debut.plusMonths(1).minusSeconds(1);
        
        return new LocalDateTime[] { debut, fin };
    }
    
    public LocalDateTime[] inputWeekPeriod() {
        System.out.print("Entrez l'année (AAAA) : ");
        int annee = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Entrez le numéro de semaine (1-52) : ");
        int semaine = Integer.parseInt(scanner.nextLine());
        
        LocalDateTime debut = LocalDateTime.now()
                .withYear(annee)
                .with(WeekFields.of(Locale.FRANCE).weekOfYear(), semaine)
                .with(WeekFields.of(Locale.FRANCE).dayOfWeek(), 1)
                .withHour(0).withMinute(0);
        
        LocalDateTime fin = debut.plusDays(7).minusSeconds(1);
        
        return new LocalDateTime[] { debut, fin };
    }
    
    public LocalDateTime[] inputDayPeriod() {
        System.out.print("Entrez l'année (AAAA) : ");
        int annee = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Entrez le mois (1-12) : ");
        int mois = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Entrez le jour (1-31) : ");
        int jour = Integer.parseInt(scanner.nextLine());
        
        LocalDateTime debut = LocalDateTime.of(annee, mois, jour, 0, 0);
        LocalDateTime fin = debut.plusDays(1).minusSeconds(1);
        
        return new LocalDateTime[] { debut, fin };
    }
}
